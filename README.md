# AIRA Vision Dashboard

A responsive home inspection report dashboard with cloud storage support.

## Cloud Storage Setup (Free GitHub Gist Solution)

To save data across all users and persist after page refresh:

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
2. Find the `GIST_ID` constant
3. Replace `''` with your actual Gist ID:
   ```javascript
   const GIST_ID = 'abc123def456...';
   ```

### Step 5: Set Token in Browser
Each user needs to enter their GitHub token once:

1. Open browser console (F12 → Console)
2. Paste and run:
   ```javascript
   sessionStorage.setItem('github_token', 'your_personal_access_token_here');
   ```
3. Or use this bookmarklet:
   ```javascript
   const token = prompt('Enter your GitHub Personal Access Token:');
   if (token) sessionStorage.setItem('github_token', token);
   ```

### Step 6: Sync Data
1. Make edits to dashboard cards
2. Upload images
3. Click the **🔄 Sync** button
4. Data is now saved to your GitHub Gist

### Step 7: Load on Other Devices
Any user can access the data by:
1. Setting the same GitHub token in `sessionStorage`
2. Visiting the dashboard
3. Data loads automatically

---

## Local Development

No backend required - works as static files on GitHub Pages, Netlify, or any static hosting.

## Features

- ✅ Responsive design (mobile-friendly)
- ✅ Inline editing for dashboard fields
- ✅ Image upload with compression
- ✅ Delete uploaded images
- ✅ Auto-save to GitHub Gist
- ✅ Sync button for manual sync
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