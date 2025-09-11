# ProSettings Clone - Technical Specification

## Core Features (MVP)

### 1. Player Settings Database
- Pro player profiles with current settings
- Game-specific configurations (Valorant, Apex, Overwatch)
- Hardware specifications (mouse, keyboard, monitor)
- Historical settings tracking

### 2. Search & Filter System
- Filter by game, role, team, region
- Search by player name or team
- Compare multiple players side-by-side
- Sort by popularity, recent updates

### 3. User-Generated Content
- User submissions for new players/settings
- Verification system for accuracy
- Community voting on setting authenticity

## Technical Architecture

### Backend (Java Spring Boot)
```
src/main/java/com/prosettings/
├── config/          # Database, security config
├── controller/      # REST API endpoints
├── entity/          # JPA entities
├── repository/      # Data access layer
├── service/         # Business logic
├── dto/             # Data transfer objects
└── util/            # Helper utilities
```

### Database Schema (PostgreSQL)

#### Core Tables
- **players** - Pro player information
- **games** - Game metadata
- **settings** - Player game settings
- **hardware** - Equipment specifications
- **teams** - Team information
- **submissions** - User-submitted data

### API Endpoints

#### Player Management
```
GET    /api/players              # List all players
GET    /api/players/{id}         # Get player details
GET    /api/players/search       # Search players
POST   /api/players              # Add new player (admin)
PUT    /api/players/{id}         # Update player
```

#### Settings Management
```
GET    /api/settings/player/{playerId}     # Get player settings
GET    /api/settings/game/{gameId}         # Get settings by game
POST   /api/settings                       # Submit new settings
PUT    /api/settings/{id}                  # Update settings
```

#### Games & Hardware
```
GET    /api/games                # List supported games
GET    /api/hardware            # List hardware options
GET    /api/hardware/search     # Search hardware
```

## Database Design

### Players Table
```sql
CREATE TABLE players (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    real_name VARCHAR(100),
    team_id BIGINT,
    country VARCHAR(3),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);
```

### Games Table
```sql
CREATE TABLE games (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    display_name VARCHAR(100) NOT NULL,
    release_date DATE,
    is_active BOOLEAN DEFAULT TRUE
);
```

### Settings Table
```sql
CREATE TABLE settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    sensitivity DECIMAL(10,6),
    dpi INTEGER,
    edpi DECIMAL(10,2) GENERATED ALWAYS AS (sensitivity * dpi),
    crosshair_settings JSON,
    keybinds JSON,
    video_settings JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_verified BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (player_id) REFERENCES players(id),
    FOREIGN KEY (game_id) REFERENCES games(id),
    UNIQUE KEY unique_player_game (player_id, game_id)
);
```

### Hardware Table
```sql
CREATE TABLE hardware (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    category ENUM('mouse', 'keyboard', 'monitor', 'headset', 'mousepad'),
    brand VARCHAR(50),
    model VARCHAR(100),
    specifications JSON,
    FOREIGN KEY (player_id) REFERENCES players(id)
);
```

## Development Phases

### Phase 1: Core Backend (Week 1-2)
- [x] Project setup with Spring Boot
- [ ] Database schema implementation
- [ ] Basic CRUD operations for players
- [ ] Settings management endpoints
- [ ] Basic validation and error handling

### Phase 2: Data Layer (Week 2-3)
- [ ] Seed database with popular pro players
- [ ] Data validation and constraints
- [ ] Search functionality implementation
- [ ] Filtering and sorting capabilities

### Phase 3: Advanced Features (Week 3-4)
- [ ] User submission system
- [ ] Settings comparison functionality
- [ ] Data export capabilities
- [ ] Basic admin panel

### Phase 4: Frontend Integration (Week 4-5)
- [ ] REST API documentation
- [ ] Frontend scaffolding
- [ ] API integration
- [ ] Basic UI implementation

## Competitive Advantages

### Technical Improvements
- **Real-time updates** via WebSocket
- **Mobile-optimized** responsive design
- **Advanced search** with fuzzy matching
- **Data validation** with community verification

### User Experience
- **Faster loading** with optimized queries
- **Better mobile** experience
- **Comparison tools** for multiple players
- **Historical tracking** of setting changes

### Community Features
- **User contributions** with verification
- **Rating system** for setting accuracy
- **Discussion threads** for each player
- **Setting recommendations** based on playstyle

## Next Steps

1. **Environment Setup**
   - Install Java 17, Maven, PostgreSQL
   - Create Spring Boot project structure
   - Set up database connection

2. **Initial Implementation**
   - Create entity classes
   - Implement repository layer
   - Build basic REST controllers
   - Add validation and error handling

3. **Data Population**
   - Research current pro player settings
   - Create data seeding scripts
   - Implement data validation rules