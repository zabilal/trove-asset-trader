# TroveApp Asset Trading Application

A simple web application using Vert.x to manage assets and user portfolios. The application is designed to be a starting point for asset trading and portfolio management.

## Features

*   Asset creation and management
*   User portfolio management
*   Leaderboard of top users by gem count
*   Real-time price updates for all assets
*   RESTful API for all endpoints

## Endpoints

*   **POST /api/users**: Create a new user
    *   Request Body: `{"username": "string"}`
    *   Response: `{"userId": "string", "username": "string", "gemCount": int, "rank": int}`
*   **GET /api/ranking**: Get the leaderboard of top users by gem count
    *   Query Param: `topN=int`
    *   Response: `[{userId: "string", username: "string", gemCount: int, rank: int}]`
*   **GET /api/assets**: Get all available assets
    *   Response: `[{assetId: "string", name: "string", quantity: int, price: double}]`
*   **POST /api/assets**: Create a new asset
    *   Request Body: `{"name": "string", "initialPrice": double}`
    *   Response: `{"assetId": "string", "name": "string", "quantity": int, "price": double}`
*   **GET /api/portfolios/:userId/value**: Get the value of a user's portfolio
    *   Path Param: `userId=string`
    *   Response: `int`
*   **POST /api/trading/buy**: Buy an asset for a user
    *   Request Body: `{"userId": "string", "assetId": "string", "quantity": int}`
    *   Response: `{"userId": "string", "assetId": "string", "quantity": int}`
*   **POST /api/trading/sell**: Sell an asset for a user
    *   Request Body: `{"userId": "string", "assetId": "string", "quantity": int}`
    *   Response: `{"userId": "string", "assetId": "string", "quantity": int}`

## Running Locally

1.  Clone the repository: `git clone https://github.com/zabilal/trove-asset-trader.git`
2.  Install maven: <https://maven.apache.org/download.cgi>
3.  Run the application: `mvn clean compile exec:java`
4.  Open a web browser and navigate to: <http://localhost:8080/>

## Suggestions for Improvement

*   Add user account balance management (e.g. depositing and withdrawing funds)
*   Add more realistic asset prices (e.g. using a random walk or historical data)
*   Add more realistic user behavior (e.g. limiting the number of asset purchases per user)
*   Add more features to the leaderboard (e.g. displaying the top users by gem count and by portfolio value)
*   Add authentication and authorization to the application (e.g. using Vert.x Web Auth)
*   Add a database to store user and asset data (e.g. using Vert.x JDBC Client)
*   Add a more realistic user registration process (e.g. using email verification)
*   Add a more realistic asset creation process (e.g. using a form with validation)
