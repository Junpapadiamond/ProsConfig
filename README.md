# ProsConfig 🎮

A comprehensive platform for tracking and sharing professional esports player settings, inspired by ProSettings but built with modern architecture and enhanced features.

## 🚀 Features

### Core Functionality
- **Player Database**: Comprehensive profiles of professional esports players
- **Game Settings**: Track sensitivity, keybinds, crosshair settings, and video configurations
- **Hardware Specs**: Monitor setups, peripherals, and equipment details
- **Advanced Search**: Filter by game, role, team, region, and more
- **Setting Comparisons**: Side-by-side analysis of multiple players
- **Community Verification**: User submissions with validation system

### Supported Games
- Valorant
- Apex Legends  
- Counter-Strike
- Overwatch
- *More games coming soon...*

## 🏗️ Architecture

### Backend (Spring Boot)
```
src/main/java/com/sportsmotivation/prosconfig/
├── config/          # Configuration classes
├── controller/      # REST API endpoints
├── entity/          # JPA entities
├── repository/      # Data access layer
├── service/         # Business logic
├── dto/             # Data transfer objects
└── util/            # Helper utilities
```

### Tech Stack
- **Backend**: Java 21, Spring Boot 3.5.5, Spring Data JPA
- **Database**: PostgreSQL with JSON support
- **Build Tool**: Maven
- **API**: RESTful endpoints with validation

## 🛠️ Setup & Installation

### Prerequisites
- Java 21+
- PostgreSQL 13+
- Maven 3.6+

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/ProsConfig.git
   cd ProsConfig
   ```

2. **Setup PostgreSQL Database**
   ```bash
   # Create database
   createdb prosconfig
   
   # Update application.properties with your credentials
   spring.datasource.url=jdbc:postgresql://localhost:5432/prosconfig
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API**
   - API Base URL: `http://localhost:8080/api`
   - Health Check: `http://localhost:8080/api/games`

## 📡 API Endpoints

### Player Management
- `GET /api/players` - List all active players
- `GET /api/players/{id}` - Get player details
- `GET /api/players/search?query=player_name` - Search players
- `POST /api/players` - Create new player (admin)
- `PUT /api/players/{id}` - Update player

### Settings Management  
- `GET /api/settings/player/{playerId}` - Get player settings
- `GET /api/settings/game/{gameId}` - Get settings by game
- `POST /api/settings` - Submit new settings
- `PUT /api/settings/{id}` - Update settings
- `PATCH /api/settings/{id}/verify` - Verify settings

### Games & Hardware
- `GET /api/games` - List supported games
- `GET /api/hardware/player/{playerId}` - Get player hardware
- `GET /api/hardware/search?query=mouse_name` - Search hardware

## 🗄️ Database Schema

### Key Tables
- **players** - Player profiles and metadata
- **games** - Supported games information  
- **settings** - Player-specific game configurations
- **hardware** - Equipment specifications
- **teams** - Team information

### Example Setting Structure
```json
{
  "playerId": 1,
  "gameId": 1,
  "sensitivity": 0.35,
  "dpi": 800,
  "crosshairSettings": {
    "color": "cyan",
    "thickness": 2,
    "size": 4
  },
  "keybinds": {
    "jump": "SPACE",
    "crouch": "CTRL"
  }
}
```

## 🔄 Development Workflow

### Running Tests
```bash
./mvnw test
```

### Build for Production
```bash
./mvnw clean package
```

### Database Migrations
The application uses Spring Boot's automatic schema generation. For production, consider using Flyway or Liquibase for controlled migrations.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### Code Style
- Follow Java naming conventions
- Use proper validation annotations
- Write comprehensive tests
- Document API endpoints

## 📊 Project Status

### Phase 1: Core Backend ✅
- [x] Project setup with Spring Boot
- [x] Database schema implementation
- [x] Basic CRUD operations for players
- [x] Settings management endpoints
- [x] Validation and error handling

### Phase 2: Data Layer (In Progress)
- [ ] Seed database with popular pro players
- [ ] Advanced search functionality
- [ ] Data validation and constraints
- [ ] Filtering and sorting capabilities

### Phase 3: Advanced Features (Planned)
- [ ] User submission system
- [ ] Settings comparison functionality
- [ ] Data export capabilities
- [ ] Admin panel

### Phase 4: Frontend Integration (Planned)
- [ ] REST API documentation
- [ ] Frontend scaffolding
- [ ] API integration
- [ ] UI implementation

## 🚀 Deployment

### Using Docker (Coming Soon)
```bash
docker-compose up -d
```

### Environment Variables
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=prosconfig
DB_USER=postgres
DB_PASS=password
```

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Inspired by the original ProSettings website
- Built with modern Spring Boot architecture
- Community-driven approach to data accuracy

---

**Made with ❤️ for the esports community**