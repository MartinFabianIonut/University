var matrix = [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8];

matrix.sort(function () {
    return 0.5 - Math.random();
});

var first = null;
var second = null;

var found = 0;

function verifyPair() {
    var id1 = first.id;
    var id2 = second.id;

    if (matrix[id1] === matrix[id2]) {
        first.removeEventListener("click", select);
        second.removeEventListener("click", select);
        first.style.backgroundColor = "#94dac5";
        second.style.backgroundColor = "#94dac5";

        first = null;
        second = null;

        found++;

        if (found === matrix.length / 2) {
            alert("Ai castigat!");
        }
    } else {
        setTimeout(function () {
            first.style.backgroundColor = "#a293b4";
            second.style.backgroundColor = "#a293b4";
            first.textContent = "";
            second.textContent = "";

            first = null;
            second = null;
        }, 2000);
    }
}

function select() {
    if (first && second) {
        return;
    }
    if (this === first) {
        return;
    }
    this.textContent = matrix[this.id];
    if (!first) {
        first = this;
        return;
    }
    second = this;
    verifyPair();
}

var elements = document.getElementsByTagName("td");
for (var i = 0; i < elements.length; i++) {
    elements[i].addEventListener("click", select);
}