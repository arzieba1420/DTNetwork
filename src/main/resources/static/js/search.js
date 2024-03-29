window.onload=function () {


    var input = document.getElementById("search");
    var searchBox = document.getElementById("searchBar");
    var diary = document.getElementById("diary");
    var diaryText = document.getElementById("diaryText");
    var calendarText = document.getElementById("calendarText");
    var arrow1 = document.getElementById("arr1");
    var arrow2 = document.getElementById("arr2");
    var arrow3 = document.getElementById("arr3");
    var arrow4 = document.getElementById("arr4");

    var openNote = document.getElementById("openNotes");
    var openCalendar = document.getElementById("openCalendar");
    var sid2 = document.getElementById("mySidenav2");
    var sid3 = document.getElementById("mySidenav3");

    var tests = document.getElementById("tests");
    var downloadButton = document.getElementById("instrukcja");
    var scheduleButton = document.getElementById("grafik");
    var month = new Date().getMonth();



    switch (month) {
        case 0:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=0");
            break;
        case 1:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=1768327251");
            break;
        case 2:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=367811845");
            break;
        case 3:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=1254235562");
            break;
        case 4:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=900428102");
            break;
        case 5:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=2025169371");
            break;
        case 6:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=1363080914");
            break;
        case 7:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=870286028");
            break;
        case 8:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=1932820850");
            break;
        case 9:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=2076927743");
            break;
        case 10:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=67999994");
            break;
        case 11:
            scheduleButton.setAttribute("href","https://docs.google.com/spreadsheets/d/1_joVLxWDabAU4PRAvlFkeQaKH9j9-iqq7r9KYuE40Tw/edit#gid=397658345");
            break;


    }

    input.addEventListener("focus", function (event) {
        input.placeholder = "";

    });



    diaryText.addEventListener("focus",function () {

        if(this.value==""){
            this.placeholder="";
        }
    })

    calendarText.addEventListener("focus",function () {
        if(this.value==""){
            this.placeholder="";
        }
    })

    diaryText.addEventListener("blur",function () {
        if(this.value==""){
            this.placeholder="Kliknij aby dodać wpis...";
        }
    })

    calendarText.addEventListener("blur",function () {
        if(this.value==""){
            this.placeholder="Kliknij aby dodać wpis...";
        }
    })

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


    arrow3.addEventListener("click", function () {
        sid2.style.visibility="hidden";
        downloadButton.style.visibility= "visible";
    })

    arrow4.addEventListener("click", function () {
        sid3.style.visibility="hidden";
        downloadButton.style.visibility= "visible";
    })

    openNote.addEventListener("click", function (ev) {
        sid3.style.visibility = "hidden";
        sid2.style.visibility = "visible"
        downloadButton.style.visibility= "hidden";
    })

    openCalendar.addEventListener("click",function () {
        sid2.style.visibility = "hidden";
        sid3.style.visibility = "visible";
        downloadButton.style.visibility= "hidden";
    })




}