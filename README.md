# AIRA Vision Dashboard

A responsive home inspection report dashboard with cloud storage support.

## Cloud Storage Setup - One-Time Setup

### Step 1: Create a GitHub Gist
1. Go to https://gist.github.com
2. Click "New Gist"
3. Add this file:
   - **Filename**: `dashboard-data.json`
   - **Content**:
   ```json
   {
     "dashboardFields": {
       "client-name": "",
       "project": "",
       "inspected-by": "",
       "inspection-date": ""
     },
     "dimensionData": [],
     "images": {}
   }
   ```
4. Click "Create public gist"

### Step 2: Get Your Gist ID
1. Open your new gist
2. Copy the ID from the URL: `https://gist.github.com/YOUR_USERNAME/abc123def456...`
3. The Gist ID is `abc123def456...`

### Step 3: Create a GitHub Personal Access Token
1. Go to https://github.com/settings/tokens
2. Click "Generate new token"
3. Give it a name (e.g., "Dashboard Sync")
4. Select scope: `gist` permission
5. Generate and copy the token

### Step 4: Configure the Dashboard ✅ DONE
1. Open `app.js`
2. Find the `GIST_ID` constant and replace with your Gist ID:
   ```javascript
   const GIST_ID = 'b5946a18f759e1526fd9a287edcdb91d'; // Already configured!
   ```
3. Find the `GITHUB_TOKEN` constant on line 251

⚠️ **IMPORTANT**: Your GitHub token needs to be updated:
- If you see `ghp_...` or a long alphanumeric string, you're good!
- If you see `⟦SECRET_REDACTED⟧` or `YOUR_TOKEN_HERE`, replace it with your real token:
   ```javascript
   const GITHUB_TOKEN = 'ghp_your_actual_token_here';
   ```

🔧 **Get your token at**: https://github.com/settings/tokens
- Click "Generate new token" → "Fine-grained tokens"
- Give it a name → "Dashboard"
- Select repository: "Read and write"
- Generate and copy the token

That's it! The dashboard is now fully configured. No user setup required.

---

## How It Works

- **Auto-save**: Data saves automatically when you edit fields or upload images
- **Images**: Photos are compressed for mobile-friendly sizes
- **Auto-load**: Data loads automatically from GitHub Gist when the page opens
- **Cross-device**: Works on any device with internet access

## Local Development

No backend required - works as static files on GitHub Pages, Netlify, or any static hosting.

## Features

- ✅ Responsive design (mobile-friendly)
- ✅ Inline editing for dashboard fields
- ✅ Image upload with compression
- ✅ Delete uploaded images
- ✅ **Automatic save** to GitHub Gist (no sync button needed!)
- ✅ **No user authentication** required
- ✅ Progress indicator

## File Structure

```
├── index.html      # Dashboard UI
├── app.js          # Application logic
├── styles.css      # Styling
├── data.json       # Default data template
├── README.md       # This file
└── netlify.toml    # Netlify configuration
```