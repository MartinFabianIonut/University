function sortColumn(pos) {
    let table = $("#tabel");
    let rows = table.find("tr");

    function ascending(a, b) {
        if (a.sortValue < b.sortValue) {
            return -1;
        }
        if (a.sortValue > b.sortValue) {
            return 1;
        }
        return 0;
    }

    function descending(a, b) {
        if (a.sortValue > b.sortValue) {
            return -1;
        }
        if (a.sortValue < b.sortValue) {
            return 1;
        }
        return 0;
    }

    let toSort = [];
    for (let index = 1; index < rows.length; index++) {
        let toAdd = [rows[index].innerHTML];
        toAdd.sortValue = $(rows[index]).children().eq(pos).find("div").html();
        toSort.push(toAdd);
    }

    let before = toSort.toString();
    if (!isNaN(parseInt(toSort[0].sortValue))) {
        for (let index = 0; index < toSort.length; index++) {
            toSort[index].sortValue = parseInt(toSort[index].sortValue);
        }
    }
    toSort.sort(ascending);
    if (before === toSort.toString()) {
        toSort.sort(descending);
    }

    for (let index = 0; index < rows.length - 1; index++) {
        $(rows[index + 1]).html(toSort[index]);
    }

}

function sortRow(pos) {
    let table = $("#tabel2");
    let rows = table.find("tr");
    let columns = [];
    for (let index = 0; index < rows.eq(0).children().length - 1; index++) {
        columns.push([]);
    }
    for (let index = 0; index < rows.length; index++) {
        for (let nthChild = 1; nthChild < rows.eq(index).children().length; nthChild++) {
            columns[nthChild - 1].push(rows.eq(index).children().eq(nthChild).find("div").html());        
        }
    }

    function ascending(a, b) {
        if (a[pos] < b[pos]) {
            return -1;
        }
        if (a[pos] > b[pos]) {
            return 1;
        }
        return 0;
    }

    function descending(a, b) {
        if (a[pos] > b[pos]) {
            return -1;
        }
        if (a[pos] < b[pos]) {
            return 1;
        }
        return 0;
    }

    let before = columns.toString();
    if (!isNaN(parseInt(columns[0][pos]))) {
        for (let index = 0; index < columns.length; index++) {
            columns[index][pos] = parseInt(columns[index][pos]);
        }
    }
    columns.sort(ascending);
    if (before === columns.toString()) {
        columns.sort(descending);
    }

    for (let index = 0; index < rows.length; index++) {
        for (let nthChild = 0; nthChild < rows[index].children.length - 1; nthChild++) {
            rows.eq(index).children().eq(nthChild + 1).find("div").html(columns[nthChild][index]);
        }
    }
}