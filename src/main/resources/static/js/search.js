window.onload=function () {


    var input = document.getElementById("search");
    var searchBox = document.getElementById("blog")

    input.addEventListener("focus", function (event) {
        input.placeholder = "";

    });

    input.addEventListener(type="blur",function (ev) {
        if(input.textContent==""){
            input.placeholder="Szukana fraza...";
        }
    })

    var clickCounter = 0;
    searchBox.addEventListener("click", function () {
        if(clickCounter%2==0 | clickCounter==0 ){
        searchBox.style.marginLeft = "0";

        }
        if(clickCounter%2==1){
            searchBox.style.marginLeft="-430px";

        }

        clickCounter++;
    })


}