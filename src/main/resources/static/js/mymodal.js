
    // Get the modal
    var modal = document.getElementById("modalWindow");
    var modal2 = document.getElementById("modalWindow2");
// Get the button that opens the modal
    var btn = document.getElementById("openModal");
    var btn2 = document.getElementById("openModal2");
// Get the <span> element that closes the modal
   var closePosts = document.getElementById("closePosts");
   var closeDocs = document.getElementById("closeDocs");

    closePosts.addEventListener(type="click",function () {
        modal.style.display = "none";
        modal2.style.display = "none";
    })
// When the user clicks on the button, open the modal
    btn.addEventListener(type="click", function () {
        modal.style.display = "block";
    })

    btn2.addEventListener(type="click", function () {
        modal2.style.display = "block";
    })

// When the user clicks on <span> (x), close the modal


    closeDocs.addEventListener(type="click",function () {
        modal2.style.display = "none";
    })













