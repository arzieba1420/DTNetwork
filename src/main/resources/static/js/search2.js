window.onload=function () {

    var arrow5 = document.getElementById("arr5");
    var chillerSets = document.getElementById("setPoints");

    var clickCounter3 = 0;
    arrow5.addEventListener("click", function () {
        if(clickCounter3%2==0 | clickCounter3==0 ){
            chillerSets.style.marginLeft = "0";
            chillerSets.getElementsByClassName("fas fa-angle-right")[0].className = "fas fa-angle-left";

        }
        if(clickCounter3%2==1){
            chillerSets.style.marginLeft="-410px";
            chillerSets.getElementsByClassName("fas fa-angle-left")[0].className = "fas fa-angle-right";
        }

        clickCounter3++;
    })


}