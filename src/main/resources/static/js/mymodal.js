


// Get the modal
    var modal = document.getElementById("modalWindow");

// Get the button that opens the modal
    var btn = document.getElementById("openModal");

// Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

// When the user clicks on the button, open the modal
    btn.addEventListener(type="click", function () {
        modal.style.display = "block";
    })

// When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
    }

// When the user clicks anywhere outside of the modal, close it
    window.onclick = function () {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }




