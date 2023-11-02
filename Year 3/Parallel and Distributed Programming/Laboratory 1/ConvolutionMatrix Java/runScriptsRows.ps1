# run_all_scripts.ps1
Write-Host "Running .\scriptJSequential.ps1 MainSequential `"N=M=10 si n=m=3`" 10 0 0"
& .\scriptJSequential.ps1 MainSequential "N=M=10 si n=m=3" 10 0 0

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=M=10 si n=m=3`" 4 10 0 0"
& .\scriptJ.ps1 MainParallel "N=M=10 si n=m=3" 4 10 0 0

Write-Host "Running .\scriptJSequential.ps1 MainSequential `"N=M=1000 si n=m=5`" 10 1 1"
& .\scriptJSequential.ps1 MainSequential "N=M=1000 si n=m=5" 10 1 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=M=1000 si n=m=5`" 2 10 1 1"
& .\scriptJ.ps1 MainParallel "N=M=1000 si n=m=5" 2 10 1 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=M=1000 si n=m=5`" 4 10 1 1"
& .\scriptJ.ps1 MainParallel "N=M=1000 si n=m=5" 4 10 1 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=M=1000 si n=m=5`" 8 10 1 1"
& .\scriptJ.ps1 MainParallel "N=M=1000 si n=m=5" 8 10 1 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=M=1000  si n=m=5`" 16 10 1 1"
& .\scriptJ.ps1 MainParallel "N=M=1000  si n=m=5" 16 10 1 1

Write-Host "Running .\scriptJSequential.ps1 MainSequential `"N=10 M=10000 si n=m=5`" 10 2 1"
& .\scriptJSequential.ps1 MainSequential "N=10 M=10000 si n=m=5" 10 2 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=10 M=10000 si n=m=5`" 2 10 2 1"
& .\scriptJ.ps1 MainParallel "N=10 M=10000 si n=m=5" 2 10 2 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=10 M=10000 si n=m=5`" 4 10 2 1"
& .\scriptJ.ps1 MainParallel "N=10 M=10000 si n=m=5" 4 10 2 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=10 M=10000 si n=m=5`" 8 10 2 1"
& .\scriptJ.ps1 MainParallel "N=10 M=10000 si n=m=5" 8 10 2 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=10 M=10000 si n=m=5`" 16 10 2 1"
& .\scriptJ.ps1 MainParallel "N=10 M=10000 si n=m=5" 16 10 2 1

Write-Host "Running .\scriptJSequential.ps1 MainSequential `"N=10000 M=10 si n=m=5`" 10 3 1"
& .\scriptJSequential.ps1 MainSequential "N=10000 M=10 si n=m=5" 10 3 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=10000 M=10 si n=m=5`" 2 10 3 1"
& .\scriptJ.ps1 MainParallel "N=10000 M=10 si n=m=5" 2 10 3 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=10000 M=10 si n=m=5`" 4 10 3 1"
& .\scriptJ.ps1 MainParallel "N=10000 M=10 si n=m=5" 4 10 3 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=10000 M=10 si n=m=5`" 8 10 3 1"
& .\scriptJ.ps1 MainParallel "N=10000 M=10 si n=m=5" 8 10 3 1

Write-Host "Running .\scriptJ.ps1 MainParallel `"N=10000 M=10 si n=m=5`" 16 10 3 1"
& .\scriptJ.ps1 MainParallel "N=10000 M=10 si n=m=5" 16 10 3 1
