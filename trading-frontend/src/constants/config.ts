export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:5455'

export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/auth/login',
    SIGNUP: '/auth/signup',
    LOGOUT: '/auth/logout'
  },
  COINS: {
    BULK: '/api/coins/bulk',
    BY_ID: (id: string) => `/api/coins/${id}`,
    SEARCH: (query: string) => `/api/coins/search/${query}`
  },
  WATCHLIST: {
    GET: '/watchlist',
    ADD: (coinId: string) => `/watchlist/add/${coinId}`,
    DELETE: (coinId: string) => `/watchlist/delete/${coinId}`,
    CHECK: (coinId: string) => `/watchlist/check/${coinId}`
  }
} as const

export const CACHE_TIME = {
  SHORT: 60000, // 1 minute
  MEDIUM: 300000, // 5 minutes
  LONG: 3600000 // 1 hour
} as const

export const QUERY_KEYS = {
  COINS: 'coins',
  TOP_COINS: 'topCoins',
  COIN_DETAIL: 'coinDetail',
  WATCHLIST: 'watchlist',
  COIN_SEARCH: 'coinSearch'
} as const
