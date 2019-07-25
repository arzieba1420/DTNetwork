


// Get the modal
    var modal = document.getElementById("modalWindow");
    var modal2 = document.getElementById("modalWindow2");
// Get the button that opens the modal
    var btn = document.getElementById("openModal");
    var btn2 = document.getElementById("openModal2");
// Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];
    var span1 = document.getElementsByClassName("close")[1];

// When the user clicks on the button, open the modal
    btn.addEventListener(type="click", function () {
        modal.style.display = "block";
    })

    btn2.addEventListener(type="click", function () {
    modal2.style.display = "block";
})

// When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
        modal2.style.display = "none";
    }

    span1.onclick = function () {
    modal.style.display = "none";
    modal2.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
    window.onclick = function () {
        if (event.target == modal) {
            modal.style.display = "none";
            modal2.style.display="none";
        }
    }







