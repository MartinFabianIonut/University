# run_all_scripts.ps1

$scriptDir = $PSScriptRoot

# List of ordered scripts and their respective parameters
$staticSequential = Join-Path -Path $scriptDir -ChildPath "\Static\Sequential"
$staticSequentialScript = $staticSequential + "\scriptCstaticSequential.ps1"
$staticSequentialExe1 = $staticSequential + "\main10x10.exe"
$staticSequentialExe2 = $staticSequential + "\main1000x1000.exe"
$staticSequentialExe3 = $staticSequential + "\main10x10000.exe"
$staticSequentialExe4 = $staticSequential + "\main10000x10.exe"


$staticParallel = Join-Path -Path $scriptDir -ChildPath "\Static\Parallel"
$staticParallelScript = $staticParallel + "\scriptCstaticParallel.ps1"
$staticParallelRows1 = $staticParallel + "\main10x10rows.exe"
$staticParallelRows2 = $staticParallel + "\main1000x1000rows.exe"
$staticParallelRows3 = $staticParallel + "\main10x10000rows.exe"
$staticParallelRows4 = $staticParallel + "\main10000x10rows.exe"
$staticParallelCols1 = $staticParallel + "\main10x10cols.exe"
$staticParallelCols2 = $staticParallel + "\main1000x1000cols.exe"
$staticParallelCols3 = $staticParallel + "\main10x10000cols.exe"
$staticParallelCols4 = $staticParallel + "\main10000x10cols.exe"
$staticParallelBlocks1 = $staticParallel + "\main10x10blocks.exe"
$staticParallelBlocks2 = $staticParallel + "\main1000x1000blocks.exe"
$staticParallelBlocks3 = $staticParallel + "\main10x10000blocks.exe"
$staticParallelBlocks4 = $staticParallel + "\main10000x10blocks.exe"


$dynamicSequential = Join-Path -Path $scriptDir -ChildPath "\Dynamic\Sequential"
$dynamicSequentialScript = $dynamicSequential + "\scriptCdynamicSequential.ps1"
$dynamicSequentialExe = $dynamicSequential + "\main.exe"

$dynamicParallel = Join-Path -Path $scriptDir -ChildPath "\Dynamic\Parallel"
$dynamicParallelScript = $dynamicParallel + "\scriptCdynamicParallel.ps1"
$dynamicParallelRows = $dynamicParallel + "\mainRows.exe"
$dynamicParallelCols = $dynamicParallel + "\mainCols.exe"
$dynamicParallelBlocks = $dynamicParallel + "\mainBlocks.exe"


# List of ordered scripts and their respective parameters
$scriptOrder = @(
    # Rows
    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe1, "N=M=10 si n=m=3", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows1, "N=M=10 si n=m=3", "4", "10") } ,
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=M=10 si n=m=3", "10" , "10", "10", "3") }, # 10 runs, 10 rows, 10 cols, 3 rows/cols convolution matrix
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=10 si n=m=3", "4", "10", "10", "10", "3") } , # 4 threads, 10 runs, 10 rows, 10 cols, 3 rows/cols convolution matrix
    
    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe2, "N=M=1000 si n=m=5", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows2, "N=M=1000 si n=m=5", "2", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows2, "N=M=1000 si n=m=5", "4", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows2, "N=M=1000 si n=m=5", "8", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows2, "N=M=1000 si n=m=5", "16", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=M=1000 si n=m=5", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=1000 si n=m=5", "2", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=1000 si n=m=5", "4", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=1000 si n=m=5", "8", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=M=1000 si n=m=5", "16", "10", "1000", "1000", "5") },
    
    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe3, "N=10 M=10000 si n=m=5", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows3, "N=10 M=10000 si n=m=5", "2", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows3, "N=10 M=10000 si n=m=5", "4", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows3, "N=10 M=10000 si n=m=5", "8", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows3, "N=10 M=10000 si n=m=5", "16", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=10 M=10000 si n=m=5", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=10 M=10000 si n=m=5", "2", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=10 M=10000 si n=m=5", "4", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=10 M=10000 si n=m=5", "8", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=10 M=10000 si n=m=5", "16", "10", "10", "10000", "5") },

    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe4, "N=10000 M=10 si n=m=5", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows4, "N=10000 M=10 si n=m=5", "2", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows4, "N=10000 M=10 si n=m=5", "4", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows4, "N=10000 M=10 si n=m=5", "8", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelRows4, "N=10000 M=10 si n=m=5", "16", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=10000 M=10 si n=m=5", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=10000 M=10 si n=m=5", "2", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=10000 M=10 si n=m=5", "4", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=10000 M=10 si n=m=5", "8", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelRows, "N=10000 M=10 si n=m=5", "16", "10", "10000", "10", "5") },

    # # # Cols
    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe1, "N=M=10 si n=m=3", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols1, "N=M=10 si n=m=3", "4", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=M=10 si n=m=3", "10", "10", "10", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=M=10 si n=m=3", "4", "10", "10", "10", "3") },

    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe2, "N=M=1000 si n=m=5", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols2, "N=M=1000 si n=m=5", "2", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols2, "N=M=1000 si n=m=5", "4", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols2, "N=M=1000 si n=m=5", "8", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols2, "N=M=1000 si n=m=5", "16", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=M=1000 si n=m=5", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=M=1000 si n=m=5", "2", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=M=1000 si n=m=5", "4", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=M=1000 si n=m=5", "8", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=M=1000 si n=m=5", "16", "10", "1000", "1000", "5") },

    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe3, "N=10 M=10000 si n=m=5", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols3, "N=10 M=10000 si n=m=5", "2", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols3, "N=10 M=10000 si n=m=5", "4", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols3, "N=10 M=10000 si n=m=5", "8", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols3, "N=10 M=10000 si n=m=5", "16", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=10 M=10000 si n=m=5", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=10 M=10000 si n=m=5", "2", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=10 M=10000 si n=m=5", "4", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=10 M=10000 si n=m=5", "8", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=10 M=10000 si n=m=5", "16", "10", "10", "10000", "5") },

    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe4, "N=10000 M=10 si n=m=5", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols4, "N=10000 M=10 si n=m=5", "2", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols4, "N=10000 M=10 si n=m=5", "4", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols4, "N=10000 M=10 si n=m=5", "8", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelCols4, "N=10000 M=10 si n=m=5", "16", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=10000 M=10 si n=m=5", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=10000 M=10 si n=m=5", "2", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=10000 M=10 si n=m=5", "4", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=10000 M=10 si n=m=5", "8", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelCols, "N=10000 M=10 si n=m=5", "16", "10", "10000", "10", "5") },


    # # # Blocks
    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe1, "N=M=10 si n=m=3", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks1, "N=M=10 si n=m=3", "4", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=M=10 si n=m=3", "10", "10", "10", "3") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=M=10 si n=m=3", "4", "10", "10", "10", "3") }

    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe2, "N=M=1000 si n=m=5", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks2, "N=M=1000 si n=m=5", "2", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks2, "N=M=1000 si n=m=5", "4", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks2, "N=M=1000 si n=m=5", "8", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks2, "N=M=1000 si n=m=5", "16", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=M=1000 si n=m=5", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=M=1000 si n=m=5", "2", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=M=1000 si n=m=5", "4", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=M=1000 si n=m=5", "8", "10", "1000", "1000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=M=1000 si n=m=5", "16", "10", "1000", "1000", "5") },

    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe3, "N=10 M=10000 si n=m=5", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks3, "N=10 M=10000 si n=m=5", "2", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks3, "N=10 M=10000 si n=m=5", "4", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks3, "N=10 M=10000 si n=m=5", "8", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks3, "N=10 M=10000 si n=m=5", "16", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=10 M=10000 si n=m=5", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=10 M=10000 si n=m=5", "2", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=10 M=10000 si n=m=5", "4", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=10 M=10000 si n=m=5", "8", "10", "10", "10000", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=10 M=10000 si n=m=5", "16", "10", "10", "10000", "5") },

    @{ Script = $staticSequentialScript; Params = @($staticSequentialExe4, "N=10000 M=10 si n=m=5", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks4, "N=10000 M=10 si n=m=5", "2", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks4, "N=10000 M=10 si n=m=5", "4", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks4, "N=10000 M=10 si n=m=5", "8", "10") },
    @{ Script = $staticParallelScript; Params = @($staticParallelBlocks4, "N=10000 M=10 si n=m=5", "16", "10") },
    @{ Script = $dynamicSequentialScript; Params = @($dynamicSequentialExe, "N=10000 M=10 si n=m=5", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=10000 M=10 si n=m=5", "2", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=10000 M=10 si n=m=5", "4", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=10000 M=10 si n=m=5", "8", "10", "10000", "10", "5") },
    @{ Script = $dynamicParallelScript; Params = @($dynamicParallelBlocks, "N=10000 M=10 si n=m=5", "16", "10", "10000", "10", "5") }

)


foreach ($scriptInfo in $scriptOrder) {
    $scriptFullPath = $scriptInfo["Script"]
    $params = $scriptInfo["Params"]

    Write-Host "Running script: $scriptFullPath with parameters: $($params -join ' ')"
    & $scriptFullPath @params
}
