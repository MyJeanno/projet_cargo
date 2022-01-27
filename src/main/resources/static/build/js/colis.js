
function prixColisMaritime(){
    var carton = document.getElementById("typeCarton");
    var pays = document.getElementById("pays");
    if(carton.value == 1 && pays.value == 1){
     document.getElementById("prix").value = 95000;
    }
}