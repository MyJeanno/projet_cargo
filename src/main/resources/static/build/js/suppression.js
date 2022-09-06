
$(document).ready(function(){
$('.table .dbtn').on('click',function(event){
    event.preventDefault();
    var href = $(this).attr('href');
    $('#deleteform #delRef').attr('href', href);
    $('#deleteform').modal();
});
});

$(document).ready(function(){
$('.table .showbtn').on('click',function(event){
    event.preventDefault();
    var href = $(this).attr('href');
    $('#showform #showRef').attr('href', href);
    $('#showform').modal();
});
});