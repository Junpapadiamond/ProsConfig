// Global state
let currentPage = 'home';
let currentPlayer = null;
let currentGame = null;
let players = [];
let games = [];
let settings = [];

// API Base URL
const API_BASE = '/api';

// Country flags mapping
const COUNTRY_FLAGS = {
    'UA': '🇺🇦',
    'FR': '🇫🇷',
    'RU': '🇷🇺',
    'US': '🇺🇸',
    'DE': '🇩🇪',
    'BR': '🇧🇷',
    'SE': '🇸🇪',
    'DK': '🇩🇰',
    'NO': '🇳🇴',
    'FI': '🇫🇮',
    'PL': '🇵🇱',
    'CZ': '🇨🇿'
};

// Sample teams data
const TEAMS = {
    1: { name: 'NAVI', color: '#FFD700' },
    2: { name: 'G2 Esports', color: '#FF6B6B' },
    3: { name: 'Gambit', color: '#4ECDC4' },
    4: { name: 'FaZe Clan', color: '#FF4757' },
    5: { name: 'Astralis', color: '#3742FA' }
};

// Utility Functions
function getCountryFlag(countryCode) {
    return COUNTRY_FLAGS[countryCode] || '🌍';
}

function getTeamName(teamId) {
    return TEAMS[teamId]?.name || 'Free Agent';
}

function calculateEDPI(sensitivity, dpi) {
    return sensitivity && dpi ? (sensitivity * dpi).toFixed(0) : 'N/A';
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// API Functions
async function apiRequest(endpoint, options = {}) {
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('API request failed:', error);
        throw error;
    }
}

// Data Loading Functions
async function loadPlayers() {
    try {
        const data = await apiRequest('/players');
        players = data.content || [];
        return players;
    } catch (error) {
        console.error('Failed to load players:', error);
        return [];
    }
}

async function loadGames() {
    try {
        games = await apiRequest('/games');
        return games;
    } catch (error) {
        console.error('Failed to load games:', error);
        return [];
    }
}

async function loadPlayerSettings(playerId) {
    try {
        return await apiRequest(`/settings/player/${playerId}`);
    } catch (error) {
        console.error('Failed to load player settings:', error);
        return [];
    }
}

async function loadPlayerHardware(playerId) {
    try {
        return await apiRequest(`/hardware/player/${playerId}`);
    } catch (error) {
        console.error('Failed to load player hardware:', error);
        return [];
    }
}

// Page Navigation
function showPage(pageId) {
    // Hide all pages
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('active');
    });

    // Show selected page
    document.getElementById(pageId).classList.add('active');

    // Update navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });

    currentPage = pageId;
}

function showHome() {
    showPage('home-page');
    document.querySelector('[onclick="showHome()"]').classList.add('active');
    renderPlayersGrid();
}

function showGamesPage() {
    showPage('games-page');
    document.querySelector('[onclick="showGamesPage()"]').classList.add('active');
    renderGamesGrid();
}

function showTeamsPage() {
    showPage('teams-page');
    document.querySelector('[onclick="showTeamsPage()"]').classList.add('active');
    renderTeamsGrid();
}

function showBrowsePage() {
    showPage('browse-page');
    document.querySelector('[onclick="showBrowsePage()"]').classList.add('active');
    renderBrowsePage();
}

// Player Profile
async function showPlayerProfile(playerId) {
    currentPlayer = players.find(p => p.id === playerId);
    if (!currentPlayer) return;

    showPage('player-page');

    // Update player info
    document.getElementById('player-name').textContent = currentPlayer.name;
    document.getElementById('player-real-name').textContent = currentPlayer.realName || 'Real name not available';
    document.getElementById('player-country').textContent = `${getCountryFlag(currentPlayer.country)} ${currentPlayer.country || 'Unknown'}`;
    document.getElementById('player-team').textContent = getTeamName(currentPlayer.teamId);

    // Load and display settings
    const playerSettings = await loadPlayerSettings(playerId);
    renderPlayerSettings(playerSettings);

    // Load and display hardware
    const playerHardware = await loadPlayerHardware(playerId);
    renderPlayerHardware(playerHardware);
}

// Rendering Functions
function renderPlayersGrid() {
    const grid = document.getElementById('players-grid');
    if (!grid) return;

    if (players.length === 0) {
        grid.innerHTML = '<div class="loading">Loading players...</div>';
        return;
    }

    grid.innerHTML = players.map(player => `
        <div class="player-card fade-in" onclick="showPlayerProfile(${player.id})">
            <div class="player-card-header">
                <div class="player-avatar">
                    ${player.name.charAt(0).toUpperCase()}
                </div>
                <div class="player-info">
                    <h3>${player.name}</h3>
                    <p>${player.realName || 'Professional Player'}</p>
                    <div class="player-meta">
                        <span>${getCountryFlag(player.country)} ${player.country || 'Global'}</span>
                        <span>${getTeamName(player.teamId)}</span>
                    </div>
                </div>
            </div>
            <div class="player-stats">
                <div class="stat-item">
                    <span class="stat-value">3</span>
                    <span class="stat-label">Games</span>
                </div>
                <div class="stat-item">
                    <span class="stat-value">12</span>
                    <span class="stat-label">Settings</span>
                </div>
            </div>
        </div>
    `).join('');
}

function renderGamesGrid() {
    const grid = document.getElementById('games-grid');
    if (!grid) return;

    if (games.length === 0) {
        grid.innerHTML = '<div class="loading">Loading games...</div>';
        return;
    }

    grid.innerHTML = games.map(game => `
        <div class="game-card fade-in" onclick="showGameSettings('${game.name}')">
            <div class="game-icon">
                🎮
            </div>
            <h3>${game.displayName}</h3>
            <p>Released: ${formatDate(game.releaseDate)}</p>
            <div class="game-stats">
                <div class="game-stat">
                    <span class="game-stat-value">${Math.floor(Math.random() * 50) + 10}</span>
                    <span class="game-stat-label">Players</span>
                </div>
                <div class="game-stat">
                    <span class="game-stat-value">${Math.floor(Math.random() * 200) + 50}</span>
                    <span class="game-stat-label">Settings</span>
                </div>
            </div>
        </div>
    `).join('');
}

function renderTeamsGrid() {
    const grid = document.getElementById('teams-grid');
    if (!grid) return;

    const teamsData = Object.entries(TEAMS).map(([id, team]) => ({
        id: parseInt(id),
        ...team,
        players: players.filter(p => p.teamId === parseInt(id))
    }));

    grid.innerHTML = teamsData.map(team => `
        <div class="game-card fade-in" onclick="filterByTeam(${team.id})">
            <div class="game-icon" style="background: ${team.color}">
                🏆
            </div>
            <h3>${team.name}</h3>
            <p>${team.players.length} active players</p>
            <div class="game-stats">
                <div class="game-stat">
                    <span class="game-stat-value">${team.players.length}</span>
                    <span class="game-stat-label">Players</span>
                </div>
                <div class="game-stat">
                    <span class="game-stat-value">${team.players.length * 3}</span>
                    <span class="game-stat-label">Settings</span>
                </div>
            </div>
        </div>
    `).join('');
}

function renderPlayerSettings(playerSettings) {
    const content = document.getElementById('settings-content');
    if (!content) return;

    if (playerSettings.length === 0) {
        content.innerHTML = '<p style="color: #888; text-align: center; padding: 2rem;">No verified settings available for this player.</p>';
        return;
    }

    // Group settings by game
    const settingsByGame = playerSettings.reduce((acc, setting) => {
        if (!acc[setting.gameName]) acc[setting.gameName] = [];
        acc[setting.gameName].push(setting);
        return acc;
    }, {});

    content.innerHTML = Object.entries(settingsByGame).map(([gameName, gameSettings]) => `
        <div class="game-settings">
            <h3 style="color: #007AFF; margin-bottom: 1rem;">${gameName}</h3>
            <div class="settings-grid">
                ${gameSettings.map(setting => `
                    <div class="setting-item">
                        <span class="setting-value">${setting.sensitivity || 'N/A'}</span>
                        <span class="setting-label">Sensitivity</span>
                    </div>
                    <div class="setting-item">
                        <span class="setting-value">${setting.dpi || 'N/A'}</span>
                        <span class="setting-label">DPI</span>
                    </div>
                    <div class="setting-item">
                        <span class="setting-value">${calculateEDPI(setting.sensitivity, setting.dpi)}</span>
                        <span class="setting-label">eDPI</span>
                    </div>
                    <div class="setting-item">
                        <span class="setting-value">${setting.isVerified ? '✓' : '?'}</span>
                        <span class="setting-label">Verified</span>
                    </div>
                `).join('')}
            </div>
        </div>
    `).join('');
}

function renderPlayerHardware(playerHardware) {
    const grid = document.getElementById('hardware-grid');
    if (!grid) return;

    if (playerHardware.length === 0) {
        grid.innerHTML = '<p style="color: #888; text-align: center; padding: 2rem;">No hardware information available.</p>';
        return;
    }

    grid.innerHTML = playerHardware.map(hardware => `
        <div class="hardware-item">
            <div class="hardware-category">${hardware.category}</div>
            <div class="hardware-name">${hardware.brand || 'Unknown Brand'}</div>
            <div class="hardware-model">${hardware.model || 'Model not specified'}</div>
        </div>
    `).join('');
}

function renderBrowsePage() {
    // Populate filters
    const gameSelect = document.getElementById('browse-game');
    const countrySelect = document.getElementById('browse-country');

    if (gameSelect) {
        gameSelect.innerHTML = '<option value="">All Games</option>' +
            games.map(game => `<option value="${game.id}">${game.displayName}</option>`).join('');
    }

    if (countrySelect) {
        const countries = [...new Set(players.map(p => p.country).filter(Boolean))];
        countrySelect.innerHTML = '<option value="">All Countries</option>' +
            countries.map(country => `<option value="${country}">${getCountryFlag(country)} ${country}</option>`).join('');
    }

    // DPI range slider
    const dpiRange = document.getElementById('dpi-range');
    const dpiValue = document.getElementById('dpi-value');

    if (dpiRange && dpiValue) {
        dpiRange.addEventListener('input', (e) => {
            dpiValue.textContent = e.target.value;
        });
    }

    // Initial results
    renderBrowseResults();
}

function renderBrowseResults() {
    const results = document.getElementById('browse-results');
    if (!results) return;

    results.innerHTML = `
        <div class="settings-grid">
            ${players.slice(0, 6).map(player => `
                <div class="setting-item" onclick="showPlayerProfile(${player.id})">
                    <div style="display: flex; align-items: center; margin-bottom: 1rem;">
                        <div class="player-avatar" style="width: 40px; height: 40px; margin-right: 1rem; font-size: 1rem;">
                            ${player.name.charAt(0)}
                        </div>
                        <div>
                            <div style="font-weight: 600;">${player.name}</div>
                            <div style="color: #888; font-size: 0.8rem;">${getCountryFlag(player.country)} ${player.country}</div>
                        </div>
                    </div>
                    <div class="stat-value">Click to view</div>
                    <div class="stat-label">Profile</div>
                </div>
            `).join('')}
        </div>
    `;
}

// Filter Functions
function filterByGame(gameId) {
    const buttons = document.querySelectorAll('.game-filter');
    buttons.forEach(btn => btn.classList.remove('active'));
    document.querySelector(`[data-game="${gameId}"]`).classList.add('active');

    if (gameId === 'all') {
        renderPlayersGrid();
    } else {
        const filteredPlayers = players; // In a real app, filter by game
        renderPlayersGrid();
    }
}

function filterByTeam(teamId) {
    const filteredPlayers = players.filter(p => p.teamId === teamId);
    players = filteredPlayers;
    showHome();
}

// Search Functions
function globalSearch(event) {
    if (event.key === 'Enter') {
        performGlobalSearch();
    }
}

function performGlobalSearch() {
    const query = document.getElementById('global-search').value.toLowerCase();
    if (!query) return;

    const results = players.filter(player =>
        player.name.toLowerCase().includes(query) ||
        (player.realName && player.realName.toLowerCase().includes(query)) ||
        (player.country && player.country.toLowerCase().includes(query))
    );

    players = results.length > 0 ? results : players;
    showHome();
}

// Settings Tab Functions
function showSettings(tabName) {
    const tabs = document.querySelectorAll('.settings-tab');
    tabs.forEach(tab => tab.classList.remove('active'));
    event.target.classList.add('active');

    // In a real implementation, this would show different setting categories
    // For now, it just highlights the active tab
}

// Game-specific functions
function showGameSettings(gameName) {
    const game = games.find(g => g.name === gameName);
    if (!game) return;

    currentGame = game;
    // Could implement a game-specific settings page here
    console.log(`Showing settings for ${game.displayName}`);
}

// Initialization
async function initializeApp() {
    try {
        // Load data
        await Promise.all([
            loadPlayers(),
            loadGames()
        ]);

        // Initialize home page
        renderPlayersGrid();

        console.log('App initialized successfully');
    } catch (error) {
        console.error('Failed to initialize app:', error);

        // Show error state
        const grid = document.getElementById('players-grid');
        if (grid) {
            grid.innerHTML = `
                <div style="text-align: center; color: #888; padding: 3rem;">
                    <h3>Unable to load data</h3>
                    <p>Please check your connection and try again.</p>
                    <button onclick="location.reload()" style="margin-top: 1rem; padding: 0.5rem 1rem; background: #007AFF; border: none; border-radius: 6px; color: white; cursor: pointer;">
                        Retry
                    </button>
                </div>
            `;
        }
    }
}

// Event Listeners
document.addEventListener('DOMContentLoaded', initializeApp);

// Smooth scrolling for better UX
document.addEventListener('click', (e) => {
    if (e.target.matches('[onclick*="show"]')) {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
});

// Keyboard shortcuts
document.addEventListener('keydown', (e) => {
    if (e.ctrlKey || e.metaKey) {
        switch (e.key) {
            case 'k':
                e.preventDefault();
                document.getElementById('global-search').focus();
                break;
            case '1':
                e.preventDefault();
                showHome();
                break;
            case '2':
                e.preventDefault();
                showGamesPage();
                break;
            case '3':
                e.preventDefault();
                showBrowsePage();
                break;
        }
    }
});

// Export for global access
window.ProsConfig = {
    showHome,
    showGamesPage,
    showTeamsPage,
    showBrowsePage,
    showPlayerProfile,
    filterByGame,
    globalSearch,
    performGlobalSearch,
    showSettings
};