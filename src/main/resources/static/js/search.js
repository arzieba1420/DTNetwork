window.onload=function () {


    var input = document.getElementById("search");
    var searchBox = document.getElementById("searchBar");
    var arrow1 = document.getElementById("arr1");
    var arrow2 = document.getElementById("arr2");

    var tests = document.getElementById("tests");
    input.addEventListener("focus", function (event) {
        input.placeholder = "";

    });

    input.addEventListener(type="blur",function (ev) {
        if(input.textContent==""){
            input.placeholder="Szukana fraza...";
        }
    })

    var clickCounter1 = 0;
    arrow1.addEventListener("click", function () {
        if(clickCounter1%2==0 | clickCounter1==0 ){
        searchBox.style.marginLeft = "0";
        searchBox.getElementsByClassName("fas fa-angle-right")[0].className = "fas fa-angle-left";

        }
        if(clickCounter1%2==1){
            searchBox.style.marginLeft="-410px";
            searchBox.getElementsByClassName("fas fa-angle-left")[0].className = "fas fa-angle-right";
        }

        clickCounter1++;
    })

    var clickCounter2 = 0;
    arrow2.addEventListener("click", function () {
        if(clickCounter2%2==0 | clickCounter2==0 ){
            tests.style.marginLeft = "0";
            tests.getElementsByClassName("fas fa-angle-right")[0].className = "fas fa-angle-left";

        }
        if(clickCounter2%2==1){
            tests.style.marginLeft="-410px";
            tests.getElementsByClassName("fas fa-angle-left")[0].className = "fas fa-angle-right";
        }

        clickCounter2++;
    })



}