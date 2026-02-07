# Trading Application - Development Context & Rules

**Date Created:** February 7, 2026  
**Repository:** AmoghK0216/Trading-Application  
**Current Branch:** dev  

---

## Project Overview

A cryptocurrency trading application with Spring Boot backend and React frontend. Users can track cryptocurrency prices, manage watchlists, and view market data via CoinGecko API integration.

---

## Backend Status (Completed)

### Tech Stack
- Spring Boot 3.5.6 (Java 21)
- PostgreSQL Database
- Spring Security with JWT Authentication
- WebFlux for reactive HTTP client
- CoinGecko API Integration

### Implemented Features
1. **Authentication**
   - User signup/login with JWT tokens
   - Role-based access control (ROLE_USER)
   - BCrypt password encryption
   - 30-hour token expiration

2. **Cryptocurrency Data**
   - `GET /api/coins/{coinId}` - Fetch coin details
   - `GET /api/coins/search/{query}` - Search coins
   - Integration with CoinGecko API
   - 10-second request timeout
   - Comprehensive error handling

3. **Watchlist Management** (Authenticated)
   - `POST /watchlist/add/{coinId}` - Add coin
   - `GET /watchlist` - Get user's watchlist
   - `DELETE /watchlist/delete/{coinId}` - Remove coin
   - `GET /watchlist/check/{coinId}` - Check if coin exists
   - Max 50 coins per user
   - Live price data integration

4. **Database Schema**
   - Users (id, name, email, password, timestamps)
   - Roles (id, name)
   - User_Roles (junction table)
   - Watchlists (id, user_id, coinId, coinName, coinSymbol, addedAt)

---

## Frontend Requirements

### Current Features (Phase 1)
1. **Landing Page**
   - Display top coins with performance metrics
   - Public access (no authentication required)

2. **User Authentication**
   - Login page
   - Signup page
   - JWT token management

3. **Dashboard (Post-Login)**
   - User's watchlist with live prices
   - Add/remove coins from watchlist

4. **Global Search**
   - Search any coin by name/symbol
   - View coin data
   - Add to watchlist directly from search

### Future Features (Phase 2+)
1. **Coin Detail Page**
   - Performance graphs with timeframe selection (1D, 7D, 30D, 1Y)
   - Detailed coin statistics

2. **News Tab**
   - Scrape top 10 cryptocurrency news articles
   - Display with images, titles, sources, dates

---

## Technology Stack (Frontend)

### Core
- **React** with **TypeScript**
- **Vite** (build tool)
- **React Router** (navigation)
- **Tailwind CSS** (styling)
- **Axios** (HTTP client)
- **React Query / SWR** (data fetching & caching)

### Future Integration
- **Chart.js** or **Recharts** (for graphs)
- **News API** or web scraping service

---

## Architecture Decisions

### Folder Structure (Feature-Based)
```
src/
├── components/
│   ├── common/           # Reusable UI (Button, Input, Modal, Spinner)
│   ├── coin/             # CoinCard, CoinList, CoinChart, AddToWatchlistButton
│   ├── layout/           # Header, Footer, Sidebar
│   └── search/           # SearchBar, SearchResults
├── pages/
│   ├── Landing/          # Public landing page
│   ├── Login/            # Authentication
│   ├── Dashboard/        # Watchlist view
│   ├── CoinDetail/       # Future: graphs & details
│   └── News/             # Future: news feed
├── services/             # API abstraction layer
│   ├── api.ts            # Axios config & interceptors
│   ├── authService.ts
│   ├── coinService.ts
│   └── watchlistService.ts
├── hooks/                # Custom React hooks
│   ├── useAuth.ts
│   ├── useCoins.ts
│   ├── useWatchlist.ts
│   └── useDebounce.ts
├── context/
│   ├── AuthContext.tsx   # User state management
│   └── ThemeContext.tsx  # Dark/light mode
├── utils/
│   ├── formatters.ts     # Price, date, percentage formatting
│   ├── storage.ts        # localStorage wrappers
│   └── validators.ts
├── types/                # TypeScript definitions
│   ├── coin.ts
│   ├── user.ts
│   └── watchlist.ts
└── constants/
    └── config.ts         # API URLs, constants
```

### Route Structure
```
/                → Landing (top coins)
/login           → Login page
/signup          → Signup page
/dashboard       → User dashboard (watchlist)
/coin/:coinId    → Future: Coin detail with graphs
/news            → Future: News feed
```

### State Management
- **AuthContext:** User, token, isAuthenticated
- **React Query:** Data fetching, caching, optimistic updates
- **No Redux needed:** Context + React Query sufficient

---

## Development Ground Rules

### 1. Code Reusability (CRITICAL)
- **Zero code duplication**
- Create reusable components for repeated UI patterns
- Extract common logic into custom hooks
- Shared utilities in `/utils` folder

### 2. Theme System (CRITICAL)
- **Define color variables** in Tailwind config or CSS variables
- Use semantic naming: `primary`, `secondary`, `accent`, `success`, `error`, `warning`
- **Never hardcode colors** (e.g., `#3B82F6`)
- Always use theme variables: `bg-primary`, `text-accent`
- Enables easy dark mode and theme switching

### 3. Database Efficiency (CRITICAL)
- Optimize for free-tier deployment (Supabase, Vercel, Railway)
- **Connection pooling** for PostgreSQL
- **Minimize queries:** Use joins, batch operations
- **Implement caching:** Redis or in-memory for frequently accessed data
- **Pagination:** Never fetch all records
- **Indexes:** On frequently queried columns (userId, email, coinId)

### 4. Environment Configuration
- All secrets in `.env` files (never committed)
- Separate configs: `.env.development`, `.env.production`
- Backend URL configurable (localhost → deployed API)
- Database connection strings externalized

### 5. Error Handling
- React Error Boundaries for graceful failures
- User-friendly error messages (not raw API responses)
- Fallback UI when data unavailable
- Consistent error response format

### 6. Loading States
- Every async operation shows loading UI
- Use skeleton loaders (better than spinners)
- No blank/frozen screens during fetches
- Optimistic UI updates where possible

### 7. Code Quality & Consistency
- **ESLint + Prettier** (auto-format on save)
- TypeScript **strict mode** enabled
- Naming conventions:
  - Components: `PascalCase` (e.g., `CoinCard.tsx`)
  - Functions/variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
- Meaningful variable names (no `temp`, `x`, `data1`)

### 8. Responsive Design
- **Mobile-first** approach (design for mobile, enhance for desktop)
- Touch-friendly targets (min 44px for buttons)
- Test on mobile, tablet, desktop breakpoints
- Tailwind responsive modifiers: `sm:`, `md:`, `lg:`, `xl:`

### 9. Accessibility
- Semantic HTML (`<button>`, `<nav>`, `<article>`)
- Keyboard navigation support (Tab, Enter, Esc)
- ARIA labels for screen readers
- Sufficient color contrast (WCAG AA minimum)

### 10. Performance
- **Lazy load routes** with `React.lazy()`
- Memoize components with `React.memo()` when appropriate
- Virtual scrolling for long lists (react-window)
- Optimize images (WebP format, lazy loading)
- Tree-shaking unused code

---

## CoinGecko API Optimization (Free Tier)

### Rate Limits
- **10-50 calls/minute** on free tier
- Must implement aggressive caching

### Optimization Strategies

#### 1. Backend as Proxy
- All CoinGecko calls routed through backend
- Backend implements caching layer (Redis or in-memory)
- Reduces direct API calls from frontend

#### 2. React Query Caching
```typescript
useQuery('topCoins', fetchTopCoins, {
  staleTime: 60000,      // Consider data fresh for 60s
  cacheTime: 300000,     // Keep in cache for 5 minutes
  refetchOnWindowFocus: false
})
```

#### 3. Batch Requests
- Use `/coins/markets` endpoint for multiple coins
- Backend endpoint: `GET /api/coins/batch?ids=bitcoin,ethereum`
- Reduces N+1 query problems

#### 4. Debounced Search
- Wait 500ms after user stops typing
- Prevents API spam during typing

#### 5. Smart Polling
- Landing page: Fetch once on load (manual refresh button)
- Dashboard: Refresh every 60 seconds
- Display "Last updated: X mins ago"

#### 6. Pagination
- Load 20-50 coins at a time
- Infinite scroll or "Load More" button

---

## Data Flow Example

### Adding Coin to Watchlist
```
User clicks "Add to Watchlist"
    ↓
watchlistService.addCoin(coinId)
    ↓
POST /watchlist/add/{coinId} (JWT in header)
    ↓
Backend validates token, checks limit, fetches coin data
    ↓
Save to database, return WatchlistItemDto
    ↓
React Query invalidates cache
    ↓
Watchlist automatically refetches
    ↓
Optimistic UI update (instant feedback)
    ↓
UI shows new coin with live price
```

---

## Future-Proofing Considerations

### Chart Integration (Phase 2)
- `CoinChart` component already in structure
- Backend endpoint: `GET /api/coins/{id}/chart?timeframe=7d`
- Uses CoinGecko `/coins/{id}/market_chart`
- Data cached for 1 hour (historical data doesn't change)

### News Integration (Phase 2)
- **Separate from CoinGecko** (use NewsAPI, Cryptopanic, or scraping)
- Backend endpoint: `GET /api/news?limit=10`
- Cache for 1 hour minimum
- `NewsCard` component (image, title, source, link, date)

### Deployment Strategy
- **Frontend:** Vercel, Netlify, or Cloudflare Pages
- **Backend:** Railway, Render, or Fly.io (free tier)
- **Database:** Supabase (Postgres) or PlanetScale (MySQL)
- **Caching:** Redis Cloud (free 30MB) or Upstash
- **Environment variables** properly configured per environment

---

## Key Files to Reference

### Backend
- `SecurityConfig.java` - Security & JWT setup
- `AuthService.java` - Login/signup logic
- `WatchlistService.java` - Watchlist operations
- `CoinGeckoClient.java` - API integration
- `GlobalExceptionHandler.java` - Error handling

### Frontend (To Be Created)
- `src/services/api.ts` - Axios configuration
- `src/context/AuthContext.tsx` - Authentication state
- `src/hooks/useWatchlist.ts` - Watchlist operations
- `tailwind.config.js` - Theme colors
- `.env.development` - Local config

---

## Success Criteria

✅ All features working as specified  
✅ No code duplication  
✅ Theme system fully implemented  
✅ Responsive on all devices  
✅ Proper error handling & loading states  
✅ TypeScript strict mode with no `any` types  
✅ CoinGecko API calls optimized (< 10/min average)  
✅ Fast load times (< 3s initial load)  
✅ Code formatted with Prettier  
✅ Ready for deployment with minimal configuration  

---

## Notes for Future Sessions

When resuming development, provide this file as context. Current status:
- ✅ Backend fully implemented and functional
- ⏳ Frontend to be created from scratch
- 📋 Planning phase completed
- 🎯 Ready to start Phase 1 implementation

**Next Steps:**
1. Create Vite + React + TypeScript project
2. Configure Tailwind CSS with theme
3. Set up folder structure
4. Create API service layer
5. Implement authentication flow
6. Build landing page
7. Build dashboard with watchlist
8. Implement search functionality

---

*This document should be provided at the start of each development session to maintain context and consistency.*
