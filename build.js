#!/usr/bin/env node

/**
 * Build script for AIRA Dashboard
 * Injects environment variables into app.js for deployment
 */

const fs = require('fs');
const path = require('path');

// Load environment variables
require('dotenv').config();

const GIST_ID = process.env.GIST_ID || 'b5946a18f759e1526fd9a287edcdb91d';
const GITHUB_TOKEN = process.env.GITHUB_TOKEN || '';

console.log('🔧 AIRA Dashboard Build Script');
console.log('================================');

if (!GITHUB_TOKEN) {
    console.log('⚠️  Warning: GITHUB_TOKEN not set in environment');
    console.log('   Images will not auto-save to GitHub Gist');
}

// Read app.js template
let appContent = fs.readFileSync('app.js', 'utf8');

// Replace placeholders with actual values
appContent = appContent.replace(
    /const GIST_ID = '[^']*';/,
    `const GIST_ID = '${GIST_ID}';`
);

appContent = appContent.replace(
    /const GITHUB_TOKEN = '[^']*';/,
    GITHUB_TOKEN 
        ? `const GITHUB_TOKEN = '${GITHUB_TOKEN}';`
        : "const GITHUB_TOKEN = ''; // Set via build process"
);

// Write to dist folder
if (!fs.existsSync('dist')) {
    fs.mkdirSync('dist');
}

fs.writeFileSync('dist/app.js', appContent);

// Copy other static files
const filesToCopy = ['index.html', 'styles.css', 'data.json', 'README.md'];
filesToCopy.forEach(file => {
    if (fs.existsSync(file)) {
        fs.copyFileSync(file, `dist/${file}`);
    }
});

console.log('✅ Build complete!');
console.log(`📁 Files written to /dist`);
console.log(`🔗 Gist ID: ${GIST_ID.substring(0, 8)}...`);
console.log(`🔐 Token: ${GITHUB_TOKEN ? '✓ Configured' : '✗ Missing'}`);