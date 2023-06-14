var currentId;
var formValuesChanged = false;
var firstnameChanged, lastnameChanged, emailChanged, phoneChanged;

function getIds() {
    $.ajax({
        url: 'get-ids.php',
        method: 'GET',
        async: false,
        success: function(response) {
            $('#idsList').html(response);
        },
        error: function(xhr) {
            alert('Error: ' + xhr.status);
        }
    });
}

function ifFormValuesChanged(id) {
    $.ajax({
        url: 'get-info.php',
        method: 'GET',
        data: { id: id },
        success: function(responseText) {
            let parser = new DOMParser();
            let xmlDoc = parser.parseFromString(responseText,"text/xml");
            firstnameChanged = $(xmlDoc).find('first_name').eq(0).text();    
            lastnameChanged = $(xmlDoc).find('last_name').eq(0).text();
            emailChanged = $(xmlDoc).find('email').eq(0).text();
            phoneChanged = $(xmlDoc).find('phone').eq(0).text();
            $('#firstname').val(firstnameChanged);
            $('#lastname').val(lastnameChanged);
            $('#email').val(emailChanged);
            $('#phone').val(phoneChanged);
        },
        error: function(xhr) {
            alert('Error: ' + xhr.status);
        }
    });
}

function buttonState() {
    let submitButton = $('#submit')[0];
    let firstnameValue = $('#firstname').val();
    let lastnameValue = $('#lastname').val();
    let emailValue = $('#email').val();
    let phoneValue = $('#phone').val();
    if (
        firstnameValue != firstnameChanged ||
        lastnameValue != lastnameChanged ||
        emailValue != emailChanged ||
        phoneValue != phoneChanged
    ) {
        submitButton.disabled = false;
    } else {
        submitButton.disabled = true;
    }
}

function updateEntity() {
    let first_name = $('#firstname').val();
    let last_name = $('#lastname').val();
    let email = $('#email').val();
    let phone = $('#phone').val();
    $.ajax({
        type: 'POST',
        url: 'update-table.php?id=' + currentId,
        data: 'first_name=' + first_name + '&last_name=' + last_name + '&phone=' + phone+ '&email=' + email ,
        success: function(responseText) {
            firstnameChanged = first_name;
            lastnameChanged = last_name;
            emailChanged = email;
            phoneChanged = phone;
            buttonState();
        },
        error: function(xhr) {
            console.log('Error code: ' + xhr.status);
        },
        dataType: 'application/x-www-form-urlencoded'
    });
}

function main() {
    getIds();
    $('#idsList').find('li').each(function(){
        $(this).click(function() {
            let submitButton = $('#submit')[0];
            if (submitButton.disabled === true) {
                currentId = $(this).text();
                ifFormValuesChanged(currentId);
            } else {
                if (confirm('Are you sure?')) {
                    currentId = $(this).text();
                    ifFormValuesChanged(currentId);
                    submitButton.disabled = true;
                }
            }
        })
    });
    
    $('#firstname, #lastname, #email, #phone').change(buttonState);
}

$(document).ready(function() {
    main();
});
