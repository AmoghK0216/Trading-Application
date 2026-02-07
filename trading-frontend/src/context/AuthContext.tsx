import { createContext, useContext, useState, useEffect, ReactNode } from 'react'
import { storage } from '../utils/storage'

interface AuthContextType {
  token: string | null
  isAuthenticated: boolean
  login: (token: string) => void
  logout: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

interface AuthProviderProps {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [token, setToken] = useState<string | null>(null)

  useEffect(() => {
    const savedToken = storage.getToken()
    if (savedToken) {
      setToken(savedToken)
    }
  }, [])

  const login = (newToken: string) => {
    storage.setToken(newToken)
    setToken(newToken)
  }

  const logout = () => {
    storage.removeToken()
    setToken(null)
  }

  const value = {
    token,
    isAuthenticated: !!token,
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
