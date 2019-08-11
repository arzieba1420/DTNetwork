var chillerModal = document.getElementById("chillerSetsModal");
var openBtn = document.getElementById("openModal3");
var closeBtn = document.getElementById("closeChillerSets");
var input = document.getElementById("setValue");

input.addEventListener("focus",function () {
    this.placeholder = "";
})
input.addEventListener("blur",function () {
    this.placeholder = "20.0";
})

openBtn.addEventListener("click", function () {
    chillerModal.style.display = "block";
})

closeBtn.addEventListener("click", function () {
    chillerModal.style.display = "none";
})