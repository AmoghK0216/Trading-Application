interface SkeletonProps {
  className?: string
}

export function Skeleton({ className = '' }: SkeletonProps) {
  return (
    <div className={`animate-pulse bg-primary-700 rounded ${className}`}></div>
  )
}

export function CoinCardSkeleton() {
  return (
    <div className="bg-primary-800 rounded-lg p-6 border border-primary-700">
      <div className="flex items-center gap-3 mb-4">
        <Skeleton className="w-12 h-12 rounded-full" />
        <div className="flex-1">
          <Skeleton className="h-6 w-24 mb-2" />
          <Skeleton className="h-4 w-16" />
        </div>
      </div>
      <div className="space-y-2">
        <Skeleton className="h-8 w-32" />
        <Skeleton className="h-4 w-20" />
      </div>
    </div>
  )
}
