window.onload = () => {
    loadDirectory("C:/xampp/htdocs")
}

let loadDirectory = (directory) => {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            document.getElementById("fileList").innerHTML = this.responseText;
        }
    };
    xmlhttp.open("GET", "directory-manager.php?directory=" + directory, true);
    xmlhttp.send();
}