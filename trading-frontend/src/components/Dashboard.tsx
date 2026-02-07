import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { getTopCoins, Coin } from '../api'

export default function Dashboard() {
  const [coins, setCoins] = useState<Coin[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    fetchCoins()
  }, [])

  const fetchCoins = async () => {
    try {
      setLoading(true)
      setError('')
      const token = localStorage.getItem('token')
      if (!token) {
        navigate('/login')
        return
      }
      const data = await getTopCoins(token)
      setCoins(data)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch coins')
    } finally {
      setLoading(false)
    }
  }

  const handleLogout = () => {
    localStorage.removeItem('token')
    navigate('/login')
  }

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(price)
  }

  const formatPercentage = (percentage: number) => {
    return `${percentage >= 0 ? '+' : ''}${percentage.toFixed(2)}%`
  }

  return (
    <div className="min-h-screen bg-primary-900">
      {/* Header */}
      <header className="bg-primary-800 border-b border-primary-700">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-primary-50">Crypto Trading</h1>
          <button
            onClick={handleLogout}
            className="px-4 py-2 bg-primary-700 text-primary-100 rounded-lg hover:bg-primary-600 transition-colors"
          >
            Logout
          </button>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-primary-50 mb-2">Market Overview</h2>
          <p className="text-primary-300">Top 3 cryptocurrencies by market cap</p>
        </div>

        {loading && (
          <div className="text-center py-12">
            <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-accent-400 border-r-transparent"></div>
            <p className="mt-4 text-primary-300">Loading coins...</p>
          </div>
        )}

        {error && (
          <div className="bg-error/10 border border-error text-error px-4 py-3 rounded-lg">
            {error}
          </div>
        )}

        {!loading && !error && (
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
      </main>
    </div>
  )
}
