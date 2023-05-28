$(document).ready(() => {
    loadDirectory("C:/xampp/htdocs")
});

let loadDirectory = (directory) => {
    $.ajax({
        url: "directory-manager.php",
        type: "GET",
        data: {directory: directory},
        success: (response) => {
            $("#fileList").html(response);
        },
        error: (xhr, status, error) => {
            console.log(error);
        }
    });
}