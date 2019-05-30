window.onload=function () {


    var input = document.getElementById("search");

    input.addEventListener("focus", function (event) {
        input.placeholder = "";
    });

    input.addEventListener(type="blur",function (ev) {
        if(input.textContent==""){
            input.placeholder="Szukana fraza...";
        }
    })

}