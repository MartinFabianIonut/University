var firstList = document.getElementById("firstList");
var secondList = document.getElementById("secondList");

firstList.addEventListener("dblclick", function () {
    var selectedCar = firstList.options[firstList.selectedIndex];
    secondList.appendChild(selectedCar);
    firstList.removeChild(selectedCar);
});

secondList.addEventListener("dblclick", function () {
    var selectedShoe = secondList.options[secondList.selectedIndex];
    firstList.appendChild(selectedShoe);
    secondList.removeChild(selectedShoe);
});