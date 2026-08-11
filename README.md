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

### Step 4: Configure the Dashboard
1. Open `app.js`
2. Find the `GIST_ID` constant and replace with your Gist ID:
   ```javascript
   const GIST_ID = 'your_gist_id_here';
   ```
3. Find the `GITHUB_TOKEN` constant and replace with your token:
   ```javascript
   const GITHUB_TOKEN = 'your_token_here';
   ```

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