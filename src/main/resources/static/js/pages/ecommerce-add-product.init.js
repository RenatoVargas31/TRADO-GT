$(document).ready(function () {
    $("#addproduct-nav-pills-wizard").bootstrapWizard({tabClass: "nav nav-pills nav-justified"})
}), $(".select2").select2(); $('.select2-multiple').select2({
    multiple: true
});
var triggerTabList = [].slice.call(document.querySelectorAll(".twitter-bs-wizard-nav .nav-link"));
triggerTabList.forEach(function (t) {
    var a = new bootstrap.Tab(t);
    t.addEventListener("click", function (t) {
        t.preventDefault(), a.show()
    })
});
