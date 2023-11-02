# run_all_scripts.ps1
Write-Host "Running .\scriptJSequential.ps1 MainSequential `"N=M=10 si n=m=3`" 10 0 0"
& .\scriptJSequential.ps1 MainSequential "N=M=10 si n=m=3" 10 0 0

Write-Host "Running .\scriptJParallel.ps1 MainParallel `"N=M=10 si n=m=3`" 2 10 0 0"
& .\scriptJParallel.ps1 MainParallel "N=M=10 si n=m=3" 2 10 0 0

Write-Host "Running .\scriptJSequential.ps1 MainSequential `"N=M=1000 si n=m=3`" 10 1 0"
& .\scriptJSequential.ps1 MainSequential "N=M=1000 si n=m=3" 10 1 0

Write-Host "Running .\scriptJParallel.ps1 MainParallel `"N=M=1000 si n=m=3`" 2 10 1 0"
& .\scriptJParallel.ps1 MainParallel "N=M=1000 si n=m=3" 2 10 1 0

Write-Host "Running .\scriptJParallel.ps1 MainParallel `"N=M=1000 si n=m=3`" 4 10 1 0"
& .\scriptJParallel.ps1 MainParallel "N=M=1000 si n=m=3" 4 10 1 0

Write-Host "Running .\scriptJParallel.ps1 MainParallel `"N=M=1000 si n=m=3`" 8 10 1 0"
& .\scriptJParallel.ps1 MainParallel "N=M=1000 si n=m=3" 8 10 1 0

Write-Host "Running .\scriptJParallel.ps1 MainParallel `"N=M=1000  si n=m=3`" 16 10 1 0"
& .\scriptJParallel.ps1 MainParallel "N=M=1000  si n=m=3" 16 10 1 0

Write-Host "Running .\scriptJSequential.ps1 MainSequential `"N=M=10000 si n=m=3`" 10 1 0"
& .\scriptJSequential.ps1 MainSequential "N=M=10000 si n=m=3" 10 2 0

Write-Host "Running .\scriptJParallel.ps1 MainParallel `"N=M=10000 si n=m=3`" 2 10 1 0"
& .\scriptJParallel.ps1 MainParallel "N=M=10000 si n=m=3" 2 10 2 0

Write-Host "Running .\scriptJParallel.ps1 MainParallel `"N=M=10000 si n=m=3`" 4 10 1 0"
& .\scriptJParallel.ps1 MainParallel "N=M=10000 si n=m=3" 4 10 2 0

Write-Host "Running .\scriptJParallel.ps1 MainParallel `"N=M=10000 si n=m=3`" 8 10 1 0"
& .\scriptJParallel.ps1 MainParallel "N=M=10000 si n=m=3" 8 10 2 0

Write-Host "Running .\scriptJParallel.ps1 MainParallel `"N=M=10000  si n=m=3`" 16 10 1 0"
& .\scriptJParallel.ps1 MainParallel "N=M=10000  si n=m=3" 16 10 2 0
