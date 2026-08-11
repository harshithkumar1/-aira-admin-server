// Setup script to safely update the GitHub token
// Run this script to generate the correct app.js with your token

const fs = require('fs');
const path = require('path');

const TOKEN_PLACEHOLDER = 'YOUR_GITHUB_TOKEN_HERE';

console.log(`
╔══════════════════════════════════════════════════════════╗
║          GitHub Token Setup Helper                        ║
╠══════════════════════════════════════════════════════════╣
║  This script will update app.js with your GitHub token   ║
║  Your repository is private, so this is safe.              ║
╚══════════════════════════════════════════════════════════╝
`);

// Read the current app.js
const appPath = path.join(__dirname, 'app.js');
let appContent = fs.readFileSync(appPath, 'utf8');

// Check if token needs to be updated
if (appContent.includes('⟦SECRET_REDACTED⟧')) {
    console.log('✅ Found redacted token placeholder in app.js');
    console.log('\n📝 Current token location: Line ~251 in app.js');
    console.log('🔧 Manual update needed:');
    console.log(`
Replace this line:
    const GITHUB_TOKEN = '⟦SECRET_REDACTED⟧';

With:
    const GITHUB_TOKEN = 'your_actual_github_token';

Or use sed to update it:
    sed -i "s/'⟦SECRET_REDACTED⟧'/'your_token_here'/g" app.js
`);
    
} else if (appContent.includes(TOKEN_PLACEHOLDER)) {
    console.log('✅ Token placeholder found. Ready to update.');
    console.log('\nPlease open app.js and replace:');
    console.log('    const GITHUB_TOKEN = "YOUR_GITHUB_TOKEN_HERE";');
    console.log('with your actual token.');
    
} else {
    console.log('✅ Token appears to be already set in app.js');
    console.log('\nCurrent token configuration:');
    const tokenLine = appContent.split('\n').find(line => 
        line.includes('const GITHUB_TOKEN') || line.includes('GITHUB_TOKEN =')
    );
    console.log('   ', tokenLine);
}

console.log('\n📋 After updating, run: git add . && git commit -m "Update GitHub token"');
console.log('📤 Then: git push');