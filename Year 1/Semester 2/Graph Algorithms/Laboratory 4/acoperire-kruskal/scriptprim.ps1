$exec = ".\prim.exe"

$files = Get-ChildItem -Path .\ -Filter *.in

foreach ($file in $files) {
    Write-Host $("Running " + $file.Name)
    $output = $file.Name.Replace(".in", "verif.out")
    $time = & $exec $file.Name $output 2>&1 
    Write-Host $time
    $firstline = Get-Content $output | Select-Object -First 1
    $firstline2 = Get-Content $file.Name.Replace(".in", ".out") | Select-Object -First 1
    if ($firstline -ne $firstline2) {
        Write-Host "Not the same"
    }
    else {
        Write-Host "OK"
    }
    Write-Host "-------------------"
}