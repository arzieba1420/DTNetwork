var drycoolerModal = document.getElementById("drycoolerSetsModal");
var openBtn = document.getElementById("openModal5");
var closeBtn = document.getElementById("closeDrycoolerSets");
var input1 = document.getElementById("setValue_CWL");
var input3 = document.getElementById("setValue_CWR");
var input2 = document.getElementById("setValue_AmbL");
var input4 = document.getElementById("setValue_AmbR");

input1.addEventListener("focus",function () {
    this.placeholder = "";
})
input1.addEventListener("blur",function () {
    this.placeholder = "20.0";
})

input2.addEventListener("focus",function () {
    this.placeholder = "";
})
input2.addEventListener("blur",function () {
    this.placeholder = "20.0";
})
input3.addEventListener("focus",function () {
    this.placeholder = "";
})
input3.addEventListener("blur",function () {
    this.placeholder = "20.0";
})
input4.addEventListener("focus",function () {
    this.placeholder = "";
})
input4.addEventListener("blur",function () {
    this.placeholder = "20.0";
})


openBtn.addEventListener("click", function () {
    drycoolerModal.style.display = "block";
})

closeBtn.addEventListener("click", function () {
    drycoolerModal.style.display = "none";
})