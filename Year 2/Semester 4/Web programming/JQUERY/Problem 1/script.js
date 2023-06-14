$("#firstList").on("dblclick", function(event){
    if (event.target != $("#firstList")[0])
        $("#secondList").append(event.target);
});
$("#secondList").on("dblclick", function(event){
    if (event.target != $("#secondList")[0])
        $("#firstList").append(event.target);
});