const express = require('express');
const cors = require('cors');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// --- JSON File Storage ---
const DB_FILE = path.join(__dirname, 'clients.json');
const ACTIVITY_FILE = path.join(__dirname, 'activity.json');

function loadDB() {
    if (!fs.existsSync(DB_FILE)) return [];
    try { return JSON.parse(fs.readFileSync(DB_FILE, 'utf8')); } catch { return []; }
}

function saveDB(data) {
    fs.writeFileSync(DB_FILE, JSON.stringify(data, null, 2), 'utf8');
}

function loadActivity() {
    if (!fs.existsSync(ACTIVITY_FILE)) return [];
    try { return JSON.parse(fs.readFileSync(ACTIVITY_FILE, 'utf8')); } catch { return []; }
}

function saveActivity(data) {
    // Keep only last 1000 activity entries
    const trimmed = data.slice(-1000);
    fs.writeFileSync(ACTIVITY_FILE, JSON.stringify(trimmed, null, 2), 'utf8');
}

function logActivity(deviceId, action, details = {}) {
    const activities = loadActivity();
    activities.push({
        id: Date.now(),
        device_id: deviceId,
        action: action,
        timestamp: now(),
        ...details
    });
    saveActivity(activities);
}

function now() {
    return new Date().toISOString().replace('T', ' ').slice(0, 19);
}

function isOnline(lastSeenAt) {
    if (!lastSeenAt) return false;
    const lastSeen = new Date(lastSeenAt.replace(' ', 'T') + 'Z');
    const fiveMinAgo = new Date(Date.now() - 5 * 60 * 1000);
    return lastSeen > fiveMinAgo;
}

// --- Auth middleware ---
const ADMIN_TOKEN = 'hadj2026';

function authMiddleware(req, res, next) {
    if (req.headers['x-admin-token'] !== ADMIN_TOKEN) {
        return res.status(401).json({ error: 'Unauthorized' });
    }
    next();
}

// --- App-facing API ---

// Health check
app.get('/', (req, res) => {
    res.json({ status: 'ok', server: 'AIRA Admin Server' });
});

// Uptime ping endpoint - keeps server awake
app.get('/ping', (req, res) => {
    res.json({ status: 'ok', timestamp: now() });
});

// Register device
app.post('/api/device/register', (req, res) => {
    const { device_id, device_model, app_version } = req.body;
    if (!device_id) return res.status(400).json({ error: 'device_id required' });

    const clients = loadDB();
    const idx = clients.findIndex(c => c.device_id === device_id);

    if (idx >= 0) {
        clients[idx].device_model = device_model || clients[idx].device_model;
        clients[idx].app_version = app_version || clients[idx].app_version;
        clients[idx].last_seen_at = now();
        saveDB(clients);
        logActivity(device_id, 'register', { device_model, app_version, status: 'updated' });
        return res.json({ status: 'updated', blocked: clients[idx].is_blocked });
    }

    clients.push({
        device_id,
        device_model: device_model || '',
        app_version: app_version || '',
        registered_at: now(),
        last_seen_at: now(),
        is_blocked: false,
        block_type: '',
        block_reason: '',
        blocked_at: '',
        block_expires_at: ''
    });
    saveDB(clients);
    logActivity(device_id, 'register', { device_model, app_version, status: 'new' });
    res.json({ status: 'registered', blocked: false });
});

// Heartbeat - updates last_seen_at
app.post('/api/device/heartbeat', (req, res) => {
    const { device_id } = req.body;
    if (!device_id) return res.status(400).json({ error: 'device_id required' });

    const clients = loadDB();
    const client = clients.find(c => c.device_id === device_id);
    if (client) {
        client.last_seen_at = now();
        saveDB(clients);
        logActivity(device_id, 'heartbeat');
    }
    res.json({ ok: true });
});

// Check block status
app.post('/api/device/check-block', (req, res) => {
    const { device_id } = req.body;
    if (!device_id) return res.status(400).json({ error: 'device_id required' });

    const clients = loadDB();
    const client = clients.find(c => c.device_id === device_id);
    if (!client || !client.is_blocked) {
        logActivity(device_id, 'check_block', { result: 'not_blocked' });
        return res.json({ blocked: false });
    }

    if (client.block_type === 'temporary' && client.block_expires_at) {
        if (new Date() > new Date(client.block_expires_at)) {
            client.is_blocked = false;
            client.block_type = '';
            client.block_expires_at = '';
            saveDB(clients);
            logActivity(device_id, 'check_block', { result: 'block_expired' });
            return res.json({ blocked: false });
        }
    }

    logActivity(device_id, 'check_block', { result: 'blocked', block_type: client.block_type });
    return res.json({
        blocked: true,
        block_type: client.block_type,
        reason: client.block_reason
    });
});

// --- Admin API ---

// Get all clients with online/offline status
app.get('/api/admin/clients', authMiddleware, (req, res) => {
    const clients = loadDB();
    const result = clients.map(c => ({
        ...c,
        is_online: isOnline(c.last_seen_at)
    }));
    res.json(result);
});

// Get only blocked clients
app.get('/api/admin/blocked', authMiddleware, (req, res) => {
    const clients = loadDB();
    const blocked = clients
        .filter(c => c.is_blocked)
        .map(c => ({
            ...c,
            is_online: isOnline(c.last_seen_at)
        }));
    res.json(blocked);
});

// Get activity logs
app.get('/api/admin/activity', authMiddleware, (req, res) => {
    const activities = loadActivity();
    const limit = parseInt(req.query.limit) || 100;
    const deviceId = req.query.device_id;
    
    let filtered = activities;
    if (deviceId) {
        filtered = filtered.filter(a => a.device_id === deviceId);
    }
    
    // Return most recent first
    res.json(filtered.reverse().slice(0, limit));
});

// Block a client
app.post('/api/admin/block', authMiddleware, (req, res) => {
    const { device_id, block_type, reason, duration_hours } = req.body;
    if (!device_id || !block_type) return res.status(400).json({ error: 'device_id and block_type required' });

    const clients = loadDB();
    const client = clients.find(c => c.device_id === device_id);
    if (!client) return res.status(404).json({ error: 'Client not found' });

    client.is_blocked = true;
    client.block_type = block_type;
    client.block_reason = reason || '';
    client.blocked_at = now();

    if (block_type === 'temporary' && duration_hours) {
        const d = new Date();
        d.setHours(d.getHours() + duration_hours);
        client.block_expires_at = d.toISOString();
    } else {
        client.block_expires_at = '';
    }

    saveDB(clients);
    logActivity(device_id, 'blocked', { block_type, reason, duration_hours });
    res.json({ success: true });
});

// Unblock a client
app.post('/api/admin/unblock', authMiddleware, (req, res) => {
    const { device_id } = req.body;
    if (!device_id) return res.status(400).json({ error: 'device_id required' });

    const clients = loadDB();
    const client = clients.find(c => c.device_id === device_id);
    if (!client) return res.status(404).json({ error: 'Client not found' });

    client.is_blocked = false;
    client.block_type = '';
    client.block_reason = '';
    client.blocked_at = '';
    client.block_expires_at = '';

    saveDB(clients);
    logActivity(device_id, 'unblocked');
    res.json({ success: true });
});

// Delete a client
app.delete('/api/admin/client/:deviceId', authMiddleware, (req, res) => {
    let clients = loadDB();
    const deviceId = req.params.deviceId;
    clients = clients.filter(c => c.device_id !== deviceId);
    saveDB(clients);
    logActivity(deviceId, 'deleted');
    res.json({ success: true });
});

// --- Start ---
app.listen(PORT, () => {
    console.log(`AIRA Admin Server running on port ${PORT}`);
});
