interface SpinnerProps {
  size?: 'sm' | 'md' | 'lg'
  className?: string
}

export function Spinner({ size = 'md', className = '' }: SpinnerProps) {
  const sizes = {
    sm: 'h-4 w-4 border-2',
    md: 'h-8 w-8 border-4',
    lg: 'h-12 w-12 border-4'
  }
  
  return (
    <div
      className={`inline-block animate-spin rounded-full border-solid border-accent-400 border-r-transparent ${sizes[size]} ${className}`}
      role="status"
      aria-label="Loading"
    >
      <span className="sr-only">Loading...</span>
    </div>
  )
}

export function LoadingScreen({ message = 'Loading...' }: { message?: string }) {
  return (
    <div className="min-h-screen bg-primary-900 flex flex-col items-center justify-center">
      <Spinner size="lg" />
      <p className="mt-4 text-primary-300">{message}</p>
    </div>
  )
}
