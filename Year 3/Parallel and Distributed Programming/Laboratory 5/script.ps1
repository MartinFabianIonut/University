
$param1 = $args[0] # No of runs
Write-Host "Numar de rulari: $param1"

$parentFolder = $PSScriptRoot
Write-Host "Parent folder: $parentFolder"

$outCsvPath = Join-Path -Path $parentFolder -ChildPath 'outC.csv'

# Creare fisier .csv
if (!(Test-Path $outCsvPath)) {
    New-Item $outCsvPath -ItemType File
    Set-Content $outCsvPath 'Nr readers, Nr workers,Timp executie total'
}

$sum1 = 0
$sum2 = 0
$difference = 0

for ($i = 0; $i -lt $param1; $i++) {
    $time = & "$parentFolder\sequential.exe" 2>&1
    $sum1 += $time
}
$average1 = $sum1 / $param1
Write-Host "Timp de executie mediu secvential:" $average1
Write-Host ""

Add-Content $outCsvPath "Secvential,cu timpul,$average1"

$listOfWorkers = @(2, 4, 12)
$readers = 4


Add-Content $outCsvPath "Secvential,cu timpul,$average1"

for ($j = 0; $j -lt $listOfWorkers.Length; $j++) {
    $sum2 = 0
    for ($i = 0; $i -lt $param1; $i++) {
        $time = & "$parentFolder\parallelS.exe" $($listOfWorkers[$j]) $($readers) 2>&1
        $sum2 += $time
    
        $fileA = "$parentFolder\Outputs\Clasament.txt"
        $fileB = "$parentFolder\Outputs\ClasamentParalel.txt"
        if (Compare-Object -ReferenceObject $(Get-Content $fileA) -DifferenceObject $(Get-Content $fileB)) {
            $difference += 1
            Write-Host "Diferente"
            $destinationPath = Join-Path -Path $parentFolder -ChildPath 'Outputs\Diferente.txt'
            Copy-Item -Path $fileB -Destination $destinationPath -Force
        }
    }
    $average2 = $sum2 / $param1
    Write-Host "Timp de executie mediu paralel cu $($listOfWorkers[$j]) thread-uri si 4 cititori:" $average2
    Write-Host ""
    Add-Content $outCsvPath "$($readers),$($listOfWorkers[$j]),$average2"
}
