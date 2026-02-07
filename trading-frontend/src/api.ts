import axios from 'axios'

const api = axios.create({
  timeout: 10000,
})

export const login = async (email: string, password: string): Promise<string> => {
  const response = await api.post<string>('/auth/login', { email, password })
  return response.data
}

export const signup = async (email: string, password: string, fullName: string): Promise<string> => {
  const response = await api.post('/auth/signup', { email, password, fullName })
  return response.data
}

export interface Coin {
  id: string
  symbol: string
  name: string
  current_price: number
  price_change_percentage_24h: number
  timestamp: string | null
}

// Fetch specific coins to avoid rate limits (single bulk API call)
export const getTopCoins = async (token: string): Promise<Coin[]> => {
  const response = await api.get('/api/coins/bulk?ids=bitcoin,ethereum,cardano', {
    headers: { Authorization: `Bearer ${token}` }
  })
  return response.data
}

export default api
