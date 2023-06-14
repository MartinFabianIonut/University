var matrix = [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8];

matrix.sort(function () {
    return 0.5 - Math.random();
});

var first = null;
var second = null;

var found = 0;

function verifyPair() {
    var id1 = $(first).attr("id");
    var id2 = $(second).attr("id");

    if (matrix[id1] === matrix[id2]) {
        $(first).off("click", select);
        $(second).off("click", select);
        $(first).css("background-color", "#94dac5");
        $(second).css("background-color", "#94dac5");

        first = null;
        second = null;

        found++;

        if (found === matrix.length / 2) {
            alert("Ai castigat!");
        }
    } else {
        setTimeout(function () {
            $(first).css("background-color", "#a293b4");
            $(second).css("background-color", "#a293b4");
            $(first).text("");
            $(second).text("");

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

var elements = $("td");
for (var i = 0; i < elements.length; i++) {
    $(elements[i]).on("click", select);
}