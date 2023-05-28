var currentId;
var formValuesChanged = false;
var firstnameChanged, lastnameChanged, emailChanged, phoneChanged;

function getIds(){
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(request.readyState == 4){
            if(request.status == 200){
                document.getElementById('idsList').innerHTML = request.responseText;
            }
            else
                alert('Error: ' + request.status);
        }
    };
    request.open('GET', 'get-ids.php', false);
    request.send('');
}

function ifFormValuesChanged(id){
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(request.readyState == 4){
            if(request.status == 200){
                let parser = new DOMParser();
                let xmlDoc = parser.parseFromString(request.responseText,"text/xml");
                firstnameChanged = xmlDoc.getElementsByTagName('first_name')[0].childNodes[0].nodeValue;
                lastnameChanged = xmlDoc.getElementsByTagName('last_name')[0].childNodes[0].nodeValue;
                emailChanged = xmlDoc.getElementsByTagName('email')[0].childNodes[0].nodeValue;
                phoneChanged = xmlDoc.getElementsByTagName('phone')[0].childNodes[0].nodeValue;
                document.getElementById('firstname').value = firstnameChanged;
                document.getElementById('lastname').value = lastnameChanged;
                document.getElementById('email').value = emailChanged;
                document.getElementById('phone').value = phoneChanged;
            }
            else
                alert('Error: ' + request.status);
        }
    };
    request.open('GET', 'get-info.php?id=' + id, true);
    request.send('');
}

function buttonState(){
    let submitButton = document.getElementById('submit');
    let firstnameValue = document.getElementById('firstname').value;
    let lastnameValue = document.getElementById('lastname').value;
    let emailValue = document.getElementById('email').value;
    let phoneValue = document.getElementById('phone').value;
    if(firstnameValue != firstnameChanged || 
        lastnameValue != lastnameChanged || 
        emailValue != emailChanged || 
        phoneValue != phoneChanged){
        submitButton.disabled = false;    }
    else
        submitButton.disabled = true;
      
}

function updateEntity(){
    first_name = document.getElementById('firstname').value;
    last_name = document.getElementById('lastname').value;
    email = document.getElementById('email').value;
    phone = document.getElementById('phone').value;
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(request.readyState == 4){
            if(request.status == 200){ 
                firstnameChanged = first_name;
                lastnameChanged = last_name;
                emailChanged = email;
                phoneChanged = phone;
                buttonState();
            }
            else
                console.log('Eroare cod ' + request.status);
        };
    }
    request.open('POST', 'update-table.php?id=' + currentId, true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    request.send('first_name=' + first_name + '&last_name=' + last_name + '&email=' + email + '&phone=' + phone);
}

function main(){
    getIds();
    var listItems = document.getElementsByTagName('li');
    for(let i=0; i < listItems.length; i++){
        let id = listItems[i].innerHTML;
        listItems[i].addEventListener('click', function(){
            let submitButton = document.getElementById("submit");
            if(submitButton.disabled == true){
                currentId = id;
                ifFormValuesChanged(id);
            }
            else{
                //means that are some changes that will remain uncommitted
                if (confirm("Are you sure?")) {
                    currentId = id;
                    ifFormValuesChanged(id);
                    submitButton.disabled = true;
                } 
            }
        });
    }
    document.getElementById('firstname').addEventListener('change', buttonState);
    document.getElementById('lastname').addEventListener('change', buttonState);
    document.getElementById('email').addEventListener('change', buttonState);
    document.getElementById('phone').addEventListener('change', buttonState);
}

main();