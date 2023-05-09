const labels = document.querySelectorAll('.form-control label')
const btn = document.getElementById('btn')

function nameValidation(name) {
    if (name == '')
        return false;
    var regex = /^[a-zA-Z]+ [a-zA-Z]+$/;
    return regex.test(name);
}

function birthValidation(birth) {
    if (birth == '')
        return false;
    return !isNaN(Date.parse(birth)) && new Date(birth) < new Date();
}

function ageValidation(age) {
    if (age == '')
        return false;
    var regex = /^[0-9]+$/;
    return regex.test(age);
}

function emailValidation(email) {
    if (email == '')
        return false;
    var regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return regex.test(email);
}

btn.addEventListener('click', () => {
    index = 0
    const inputs = document.querySelectorAll('.form-control input')
    myName = inputs[0].value
    birthdate = inputs[1].value
    age = inputs[2].value
    email = inputs[3].value
    error = ''

    if (!nameValidation(myName)) {
        error += 'Name is not valid! '
        inputs[0].style.border = '2px solid red'
    } else {
        inputs[0].style.border = '2px solid #fff'
    }
    if (!birthValidation(birthdate)) {
        error += 'Birthdate is not valid! '
        inputs[1].style.border = '2px solid red'
    } else {
        inputs[1].style.border = '2px solid #fff'
    }
    if (!ageValidation(age)) {
        error += 'Age is not valid! '
        inputs[2].style.border = '1px solid red'
    } else {
        inputs[2].style.border = '2px solid #fff'
    }
    if (!emailValidation(email)) {
        error += 'Email is not valid! '
        inputs[3].style.border = '2px solid red'
    } else {
        inputs[3].style.border = '2px solid #fff'
    }
    if (error)
        alert(error)
    else {
        alert('Success!')
        inputs.forEach(input => {
            input.value = ''
            input.style.border = '2px #fff solid'
        })
    }
})

labels.forEach(label => {
    // se transforma HTML-ul in span-uri
    label.innerHTML = label.innerText //se ia fiecare litera
    // din label
    .split('') //se imparte intr-un array
    .map((letter, idx) => `<span 
    style="transition-delay:${idx * 50}ms">${letter}</span>`) //array of
    // spans
    .join('')
})