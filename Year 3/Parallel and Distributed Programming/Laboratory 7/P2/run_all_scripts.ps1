
$param1 = $args[0] # No of runs
Write-Host "Numar de rulari: $param1"

$parentFolder = $PSScriptRoot
Write-Host "Parent folder: $parentFolder"

$grandParentFolder = Split-Path -Path $parentFolder -Parent
$grandGrandParentFolder = Split-Path -Path $grandParentFolder -Parent

$outCsvPath = Join-Path -Path $grandGrandParentFolder 'outC.csv'

# Creare fisier .csv
if (!(Test-Path $outCsvPath)) {
    New-Item $outCsvPath -ItemType File
    Set-Content $outCsvPath 'N = M,K,P,Timp executie total'
}

$listOfN = @(10, 1000, 10000)
$listOfP = @(4, 8, 16, 32)
$difference = 0

for ($dim = 0; $dim -lt $listOfN.Length; $dim++) {
    $sum1 = 0
    for ($i = 0; $i -lt $param1; $i++) {
        $time = & "$parentFolder\sequential.exe" $($listOfN[$dim]) $($listOfN[$dim]) "3" 2>&1
        $sum1 += $time
    }
    $average1 = $sum1 / $param1
    Write-Host "Timp de executie mediu secvential cu $($listOfN[$dim]) elemente:" $average1
    Write-Host ""
    Add-Content $outCsvPath "$($listOfN[$dim]),3,Sequential,$average1"
    for ($par = 0; $par -lt $listOfP.Length; $par++) {
        $sum2 = 0
        for ($i = 0; $i -lt $param1; $i++) {
            $time = & "$parentFolder\P2.exe" $($listOfP[$par]) $($listOfN[$dim]) $($listOfN[$dim]) "3" 2>&1
            $sum2 += $time
            $fileA = "$grandGrandParentFolder\Outputs\parallel.txt"
            $fileB = "$grandGrandParentFolder\Outputs\sequential.txt"
            if (Compare-Object -ReferenceObject $(Get-Content $fileA) -DifferenceObject $(Get-Content $fileB)) {
                $difference += 1
                Write-Host "Diferente"
                $destinationPath = Join-Path -Path $grandGrandParentFolder -ChildPath 'Outputs\Diferente.txt'
                Copy-Item -Path $fileB -Destination $destinationPath -Force
            }
        }
        $average2 = $sum2 / $param1
        Write-Host "Timp de executie mediu paralel cu $($listOfN[$dim]) elemente si $($listOfP[$par]) thread-uri:" $average2
        Write-Host ""
        Add-Content $outCsvPath "$($listOfN[$dim]),3,$($listOfP[$par]),$average2"
    }
}
