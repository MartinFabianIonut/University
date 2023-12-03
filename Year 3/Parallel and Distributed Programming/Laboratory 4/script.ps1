
$param1 = $args[0] # No of runs
Write-Host "Numar de rulari: $param1"

# Get the grandparent folder of the script's folder
$parentFolder = $PSScriptRoot
Write-Host "Parent folder: $parentFolder"

# Specify the path for outCstatic.csv in the grandparent folder
$outCsvPath = Join-Path -Path $parentFolder -ChildPath 'outC.csv'

# Creare fisier .csv
if (!(Test-Path $outCsvPath)) {
    New-Item $outCsvPath -ItemType File
    #Scrie date in csv
    Set-Content $outCsvPath 'Nr readers, Nr workers,Timp executie total'
}

$sum1 = 0
$sum2 = 0
$difference = 0

$listOfNumbers = @(4, 6, 8, 16)

for ($i = 0; $i -lt $param1; $i++) {
    $time = & "$parentFolder\sequential.exe" 2>&1
    $sum1 += $time
}
$average1 = $sum1 / $param1
Write-Host "Timp de executie mediu secvential:" $average1
Write-Host ""

Add-Content $outCsvPath "Secvential,cu timpul,$average1"


for ($k = 1; $k -le 2; $k++) {
    for ($j = 0; $j -lt $listOfNumbers.Length; $j++) {
        $sum2 = 0
        for ($i = 0; $i -lt $param1; $i++) {
            $time = & "$parentFolder\parallel.exe" $listOfNumbers[$j] $k 2>&1
            $sum2 += $time
    
            $fileA = "$parentFolder\Outputs\Clasament.txt"
            $fileB = "$parentFolder\Outputs\ClasamentParalel.txt"
            if (Compare-Object -ReferenceObject $(Get-Content $fileA) -DifferenceObject $(Get-Content $fileB)) {
                $difference += 1
                Write-Host "Diferente"
            }
        }
        $average2 = $sum2 / $param1
        Write-Host "Timp de executie mediu paralel cu $($listOfNumbers[$j]) thread-uri si $k cititori:" $average2
        Write-Host ""
        $numberOfWorkers = $listOfNumbers[$j] - $k
        Add-Content $outCsvPath "$($k),$numberOfWorkers,$average2"
    }
}