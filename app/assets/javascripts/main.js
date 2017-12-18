var create = function() {
    var project = $('#project').val()
    postAndReload('/createProject',JSON.stringify({project: project}))
}

var update = function(id){
    var name = $('#name' +id).val()
    var status = $('#status' +id).val()
    postAndReload('updateProject',JSON.stringify({id:id,name:name,status:status}))
}

var postAndReload = function (url,data) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function (result) {
            window.location.reload()
        },
        error: function (result) {
            console.log("error")
        }
    });
}
