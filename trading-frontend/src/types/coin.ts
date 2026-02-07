export interface Coin {
  id: string
  symbol: string
  name: string
  current_price: number
  price_change_percentage_24h: number
  timestamp: string | null
}

export interface CoinSearchResult {
  id: string
  name: string
  symbol: string
  marketCapRank: number
  thumb: string
}
