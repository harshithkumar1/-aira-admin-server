@echo off
cls
echo ============================================================
echo     GitHub Token Update Script for AIRA Dashboard
echo ============================================================
echo.
echo This script will update your GitHub token in app.js
echo Your repository is private - this is secure!
echo.
echo ============================================================

set /p GITHUB_TOKEN=Enter your GitHub Personal Access Token: 

REM Update the token in app.js
powershell -Command "(Get-Content -Path 'app.js') -replace '⟦SECRET_REDACTED⟧', '%GITHUB_TOKEN%' | Set-Content -Path 'app.js'"

echo.
echo ✅ Token updated successfully!
echo.
echo Next steps:
echo 1. Test the dashboard to make sure it works
echo 2. git add .
echo 3. git commit -m "Update GitHub token"
echo 4. git push
echo.
pause