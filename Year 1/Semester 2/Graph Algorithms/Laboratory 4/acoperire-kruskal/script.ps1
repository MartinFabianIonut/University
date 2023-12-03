$exec = ".\kruskal0.exe"

$files = Get-ChildItem -Path .\ -Filter *.in

foreach ($file in $files) {
    Write-Host $("Running " + $file.Name)
    $output = $file.Name.Replace(".in", "verif.out")
    $time = & $exec $file.Name $output 2>&1 
    Write-Host $time
    if (Compare-Object -ReferenceObject $(Get-Content $output) -DifferenceObject $(Get-Content $file.Name.Replace(".in", ".out"))) {
        Write-Host "Not the same"
    }
    else {
        Write-Host "OK"
    }
    Write-Host "-------------------"
}