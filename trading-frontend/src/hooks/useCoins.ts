import { useQuery } from '@tanstack/react-query'
import { coinApi } from '../api'
import { QUERY_KEYS, CACHE_TIME } from '../constants/config'
import { Coin } from '../types/coin'

export function useTopCoins() {
  return useQuery<Coin[]>({
    queryKey: [QUERY_KEYS.TOP_COINS],
    queryFn: () => coinApi.getTopCoins(),
    staleTime: CACHE_TIME.SHORT,
    gcTime: CACHE_TIME.MEDIUM,
    refetchOnWindowFocus: false
  })
}

export function useCoinById(coinId: string) {
  return useQuery<Coin>({
    queryKey: [QUERY_KEYS.COIN_DETAIL, coinId],
    queryFn: () => coinApi.getCoinById(coinId),
    staleTime: CACHE_TIME.SHORT,
    gcTime: CACHE_TIME.MEDIUM,
    enabled: !!coinId
  })
}
