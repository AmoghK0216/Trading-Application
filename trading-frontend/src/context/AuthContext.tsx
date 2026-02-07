import { createContext, useContext, useState, ReactNode } from 'react'
import { authApi } from '../api'

interface AuthContextType {
  isAuthenticated: boolean
  isLoading: boolean
  login: () => void
  logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

interface AuthProviderProps {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [isAuthenticated, setIsAuthenticated] = useState(true) // Assume authenticated by default
  const [isLoading, setIsLoading] = useState(false)

  const login = () => {
    setIsAuthenticated(true)
  }

  const logout = async () => {
    try {
      await authApi.logout()
      setIsAuthenticated(false)
      window.location.href = '/login'
    } catch (error) {
      console.error('Logout failed:', error)
    }
  }

  const value = {
    isAuthenticated,
    isLoading,
    login,
    logout
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
