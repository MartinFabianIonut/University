# run_all_scripts.ps1

$scriptDir = $PSScriptRoot

# List of ordered scripts and their respective parameters


$dynamicSequential = Join-Path -Path $scriptDir -ChildPath "\Dynamic\Sequential"
$dynamicSequentialScript = $dynamicSequential + "\scriptCdynamicSequential.ps1"
$dynamicSequentialExe = $dynamicSequential + "\main.exe"

$dynamicParallel = Join-Path -Path $scriptDir -ChildPath "\Dynamic\Parallel"
$dynamicParallelScript = $dynamicParallel + "\scriptCdynamicParallel.ps1"
$dynamicParallelRows = $dynamicParallel + "\main_buffer.exe"


# List of ordered scripts and their respective parameters
$scriptOrder = @(
    # Rows
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=M=10 si n=m=3", "10" , "10", "10", "3") }, # 10 runs, 10 rows, 10 cols, 3 rows/cols convolution matrix
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=10 si n=m=3", "2", "10", "10", "10", "3") }, # 2 threads, 10 runs, 10 rows, 10 cols, 3 rows/cols convolution matrix
    
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=M=1000 si n=m=3", "10", "1000", "1000", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=1000 si n=m=3", "2", "10", "1000", "1000", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=1000 si n=m=3", "4", "10", "1000", "1000", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=1000 si n=m=3", "8", "10", "1000", "1000", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=1000 si n=m=3", "16", "10", "1000", "1000", "3") }

    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=M=10000 si n=m=3", "10", "10000", "10000", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=10000 si n=m=3", "2", "10", "10000", "10000", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=10000 si n=m=3", "4", "10", "10000", "10000", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=10000 si n=m=3", "8", "10", "10000", "10000", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=10000 si n=m=3", "16", "10", "10000", "10000", "3") }
)


foreach ($scriptInfo in $scriptOrder) {
    $scriptFullPath = $scriptInfo["Script"]
    $params = $scriptInfo["Params"]

    Write-Host "Running script: $scriptFullPath with parameters: $($params -join ' ')"
    & $scriptFullPath @params
}
