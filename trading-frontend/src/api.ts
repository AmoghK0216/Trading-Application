import axios from 'axios'
import { API_ENDPOINTS } from './constants/config'
import { storage } from './utils/storage'
import type { Coin } from './types/coin'
import type { LoginCredentials, SignupCredentials } from './types/user'

const api = axios.create({
  timeout: 10000,
})

// Request interceptor to add auth token
api.interceptors.request.use(config => {
  // Don't add token to login/signup requests
  const isAuthEndpoint = config.url?.includes('/auth/login') || config.url?.includes('/auth/signup')
  
  if (!isAuthEndpoint) {
    const token = storage.getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
  }
  
  return config
})

// Response interceptor for error handling
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      storage.removeToken()
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authApi = {
  login: async (credentials: LoginCredentials): Promise<string> => {
    const response = await api.post<string>(API_ENDPOINTS.AUTH.LOGIN, credentials)
    return response.data
  },

  signup: async (credentials: SignupCredentials): Promise<string> => {
    const response = await api.post<string>(API_ENDPOINTS.AUTH.SIGNUP, credentials)
    return response.data
  }
}

export const coinApi = {
  getTopCoins: async (): Promise<Coin[]> => {
    const response = await api.get<Coin[]>(`${API_ENDPOINTS.COINS.BULK}?ids=bitcoin,ethereum,cardano`)
    return response.data
  },

  getCoinById: async (coinId: string): Promise<Coin> => {
    const response = await api.get<Coin>(API_ENDPOINTS.COINS.BY_ID(coinId))
    return response.data
  },

  searchCoins: async (query: string) => {
    const response = await api.get(API_ENDPOINTS.COINS.SEARCH(query))
    return response.data
  }
}

export default api

