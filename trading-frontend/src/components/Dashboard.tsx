import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useTopCoins } from '../hooks/useCoins'
import { formatPrice, formatPercentage } from '../utils/formatters'
import { Button } from './common/Button'
import { CoinCardSkeleton } from './common/Skeleton'

export default function Dashboard() {
  const navigate = useNavigate()
  const { logout } = useAuth()
  const { data: coins, isLoading, error } = useTopCoins()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-primary-900">
      {/* Header */}
      <header className="bg-primary-800 border-b border-primary-700">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-primary-50">Crypto Trading</h1>
          <Button variant="secondary" onClick={handleLogout}>
            Logout
          </Button>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-primary-50 mb-2">Market Overview</h2>
          <p className="text-primary-300">Top 3 cryptocurrencies by market cap</p>
        </div>

        {isLoading && (
          <div className="grid gap-6 md:grid-cols-3">
            <CoinCardSkeleton />
            <CoinCardSkeleton />
            <CoinCardSkeleton />
          </div>
        )}

        {error && (
          <div className="bg-error/10 border border-error text-error px-4 py-3 rounded-lg">
            {(error as Error).message || 'Failed to fetch coins'}
          </div>
        )}

        {!isLoading && !error && coins && (
          <div className="grid gap-6 md:grid-cols-3">
            {coins.map(coin => (
              <div
                key={coin.id}
                className="bg-primary-800 rounded-lg p-6 border border-primary-700 hover:border-accent-400 transition-colors"
              >
                <div className="flex items-center gap-3 mb-4">
                  <div className="w-12 h-12 bg-accent-500 rounded-full flex items-center justify-center text-2xl font-bold text-white">
                    {coin.symbol.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <h3 className="text-xl font-bold text-primary-50">{coin.name}</h3>
                    <p className="text-primary-400 uppercase">{coin.symbol}</p>
                  </div>
                </div>

                <div className="space-y-2">
                  <div>
                    <p className="text-2xl font-bold text-primary-50">
                      {formatPrice(coin.current_price)}
                    </p>
                  </div>

                  <div className="flex items-center gap-2">
                    <span
                      className={`text-sm font-medium ${
                        coin.price_change_percentage_24h >= 0
                          ? 'text-success'
                          : 'text-error'
                      }`}
                    >
                      {formatPercentage(coin.price_change_percentage_24h)}
                    </span>
                    <span className="text-primary-400 text-sm">24h</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        <div className="mt-8 text-center text-primary-400 text-sm">
          <p>Only fetching 3 popular coins to avoid CoinGecko API rate limits ⚡</p>
        </div>
      </main>
    </div>
  )
}
