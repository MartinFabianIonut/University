const imageList = document.getElementById('image-list');
const listItems = imageList.getElementsByTagName('li');
listItems[0].style.display = 'none';
let currentIndex = 0;
let intervalId = null;

function showNext() {
    listItems[currentIndex].style.display = 'none';
    currentIndex = (currentIndex + 1) % listItems.length;
    listItems[currentIndex].style.display = 'block';
}

function showPrevious() {
    listItems[currentIndex].style.display = 'none';
    currentIndex = (currentIndex - 1 + listItems.length) % listItems.length;
    listItems[currentIndex].style.display = 'block';
}

function startInterval() {
    intervalId = setInterval(showNext, 3000);
}

const nextButton = document.getElementById('next-button');
nextButton.addEventListener('click', showNext);
const previousButton = document.getElementById('previous-button');
previousButton.addEventListener('click', showPrevious);

startInterval();