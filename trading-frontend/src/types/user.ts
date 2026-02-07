export interface User {
  id: string
  email: string
  fullName: string
}

export interface AuthResponse {
  token: string
}

export interface LoginCredentials {
  email: string
  password: string
}

export interface SignupCredentials {
  email: string
  password: string
  fullName: string
}
