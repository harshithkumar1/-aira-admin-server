/**
 * Netlify Function: syncData
 * Stores and retrieves dashboard data
 * 
 * To use properly, deploy with a database like:
 * - Supabase (recommended)
 * - Firebase
 * - MongoDB Atlas
 * - Or simple file-based storage
 */

// In-memory storage (resets on deploy/restart)
// For production, use a proper database connection
let dataStore = {
    dashboardFields: {},
    dimensionData: [],
    images: {}
};

exports.handler = async (event, context) => {
    // Only allow POST
    if (event.httpMethod === 'POST') {
        try {
            const body = JSON.parse(event.body);
            
            // Store all incoming data
            dataStore = {
                dashboardFields: body.dashboardFields || {},
                dimensionData: body.dimensionData || [],
                images: body.images || {}
            };
            
            return {
                statusCode: 200,
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*'
                },
                body: JSON.stringify({
                    success: true,
                    message: 'Data saved successfully',
                    fieldsCount: Object.keys(dataStore.dashboardFields).length,
                    imagesCount: Object.keys(dataStore.images).length
                })
            };
        } catch (error) {
            return {
                statusCode: 500,
                body: JSON.stringify({
                    success: false,
                    error: error.message
                })
            };
        }
    }
    
    // Handle GET to retrieve data
    if (event.httpMethod === 'GET') {
        return {
            statusCode: 200,
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(dataStore)
        };
    }
    
    // Handle OPTIONS for CORS
    if (event.httpMethod === 'OPTIONS') {
        return {
            statusCode: 200,
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Headers': 'Content-Type',
                'Access-Control-Allow-Methods': 'GET, POST, OPTIONS'
            }
        };
    }
    
    return {
        statusCode: 405,
        body: JSON.stringify({ error: 'Method not allowed' })
    };
};