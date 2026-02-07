export interface WatchlistItem {
  id: string
  coinId: string
  coinName: string
  coinSymbol: string
  addedAt: string
  currentPrice?: number
  priceChangePercentage24h?: number
}
