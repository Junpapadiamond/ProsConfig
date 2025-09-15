# Code Overview

This document explains the structure of the ProsConfig Spring Boot project and summarizes each class in plain English and pseudocode-like form so you can learn how the pieces fit together.

## What is a RESTful API?
A RESTful API exposes resources over HTTP. Each controller class below maps HTTP verbs (GET, POST, PUT, DELETE, PATCH) to methods that operate on a specific type of data. Controllers delegate work to services, which apply business rules and interact with repositories. Repositories talk to the database through Spring Data JPA. DTOs carry data between layers, while entities map to database tables.

## Controllers (`src/main/java/com/sportsmotivation/prosconfig/controller`)

### GameController
```
GET /api/games              -> return all active games
GET /api/games/{id}         -> return a game by id or 404
GET /api/games/name/{name}  -> return a game by case-insensitive name
POST /api/games             -> create a new game (validates uniqueness)
PUT /api/games/{id}         -> update display name & release date
DELETE /api/games/{id}      -> mark game as inactive
```

### SettingController
```
GET /api/settings/player/{playerId}            -> verified settings for a player
GET /api/settings/game/{gameId}?page=&size=   -> recent verified settings for a game (paginated)
GET /api/settings/{id}                        -> single setting by id
POST /api/settings                            -> create setting linking player & game
PUT /api/settings/{id}                        -> update sensitivity, dpi, crosshair, keybinds, video
PATCH /api/settings/{id}/verify               -> flag setting as verified
DELETE /api/settings/{id}                     -> remove setting
GET /api/settings/unverified                  -> list all unverified settings
```

### PlayerController
```
GET /api/players?page=&size=&sortBy=           -> paginated list of active players
GET /api/players/{id}                          -> player by id
GET /api/players/search?query=&page=&size=     -> search by name or real name
POST /api/players                              -> create player
PUT /api/players/{id}                          -> update name, real name, team, country
DELETE /api/players/{id}                       -> mark player inactive
GET /api/players/country/{country}            -> list players by country
GET /api/players/team/{teamId}                -> list players by team
```

### HardwareController
```
GET /api/hardware                              -> (currently returns empty list)
GET /api/hardware/player/{playerId}           -> hardware owned by player
GET /api/hardware/category/{category}         -> hardware by category (mouse, keyboard, etc.)
GET /api/hardware/search?query=               -> search by brand or model
GET /api/hardware/brands/{category}           -> distinct brands for a category
GET /api/hardware/models?brand=&category=     -> models for brand/category pair
```

## Services (`src/main/java/com/sportsmotivation/prosconfig/service`)
* **GameService** – retrieves and manages games; ensures names are unique; marks deletions by toggling `isActive`.
* **SettingService** – fetches settings by player or game, verifies settings, and updates sensitivity, dpi, etc.
* **PlayerService** – queries active players, searches by name, updates player info, and soft-deletes via `isActive` flag.
* **HardwareService** – looks up hardware by player or category, searches, and handles CRUD operations.

## Repositories (`src/main/java/com/sportsmotivation/prosconfig/repository`)
Interfaces extending Spring Data JPA provide database access:
* **GameRepository** – custom query to find all active games ordered by display name.
* **SettingRepository** – queries for verified/unverified settings and count operations.
* **PlayerRepository** – search players by name or real name and count active players.
* **HardwareRepository** – search by brand/model and list distinct brands or models.

## DTOs (`src/main/java/com/sportsmotivation/prosconfig/dto`)
Simple data carriers validated via annotations:
* **GameDTO** – id, name, display name, release date, isActive.
* **PlayerDTO** – id, in-game name, real name, team id, country, isActive.
* **SettingDTO** – ids for player & game plus sensitivity, dpi, crosshair, keybinds, video, verification flag.

## Entities (`src/main/java/com/sportsmotivation/prosconfig/entity`)
Map Java classes to database tables:
* **Game** – `games` table; fields for name, displayName, releaseDate, isActive; related to many settings.
* **Player** – `players` table; includes name, realName, teamId, country, timestamps, isActive; related to settings and hardware.
* **Setting** – `settings` table; unique player/game pair with sensitivity, dpi, JSON fields for crosshair, keybinds, video; has verification flag and timestamps.
* **Hardware** – `hardware` table; belongs to a player, categorized enum, brand, model, and JSON specifications.

## Configuration
* **WebConfig** – enables CORS for `/api/**` endpoints so browser clients can make cross-origin requests.

## Application Entry Point
* **ProsConfigApplication** – boots the Spring application.

## Tests
* **ProsConfigApplicationTests** – ensures the Spring context starts without issues.

## How everything fits together
1. **Client** sends HTTP request to an endpoint (e.g., `/api/players`).
2. **Controller** method handles the request and calls a **Service**.
3. **Service** executes business logic and uses a **Repository** to interact with the database.
4. **Entity** objects represent database rows; **DTOs** represent data sent to/received from clients.
5. **WebConfig** allows cross-origin requests so web front-ends can consume the API.
