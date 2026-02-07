import axios from 'axios'
import { API_ENDPOINTS } from './constants/config'
import type { Coin } from './types/coin'
import type { LoginCredentials, SignupCredentials } from './types/user'

const api = axios.create({
  timeout: 10000,
  withCredentials: true, // Send cookies with requests
})

// Response interceptor for error handling
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authApi = {
  login: async (credentials: LoginCredentials): Promise<void> => {
    await api.post(API_ENDPOINTS.AUTH.LOGIN, credentials)
  },

  signup: async (credentials: SignupCredentials): Promise<void> => {
    await api.post(API_ENDPOINTS.AUTH.SIGNUP, credentials)
  },

  logout: async (): Promise<void> => {
    await api.post(API_ENDPOINTS.AUTH.LOGOUT)
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

