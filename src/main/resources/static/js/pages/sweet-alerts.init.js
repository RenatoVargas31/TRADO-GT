!function (t) {
    "use strict";

    function e() {
    }

    e.prototype.init = function () {
        t("#sa-basic").on("click", function () {
            Swal.fire({title: "Any fool can use a computer", confirmButtonColor: "#5664d2"})
        }), t("#sa-title").click(function () {
            Swal.fire({
                title: "The Internet?",
                text: "That thing is still around?",
                icon: "question",
                confirmButtonColor: "#5664d2"
            })
        }), t("#sa-success").click(function () {
            Swal.fire({
                title: "Good job!",
                text: "You clicked the button!",
                icon: "success",
                showCancelButton: !0,
                confirmButtonColor: "#5664d2",
                cancelButtonColor: "#ff3d60"
            })
        }), t("#sa-warning").click(function () {
            Swal.fire({
                title: "¿Estás seguro de solicitar reembolso?",
                text: "¡Esta acción es irreversible!",
                icon: "warning",
                showCancelButton: !0,
                confirmButtonColor: "#1cbb8c",
                cancelButtonColor: "#ff3d60",
                confirmButtonText: "Sí, solicitar reembolso"
            }).then(function (t) {
                t.value && Swal.fire("Listo", "Se te enviará un correo con la boleta de devolución", "success")
            })
        }), t("#sa-params").click(function () {
            Swal.fire({
                title: "¿Estás seguro de solicitar reembolso?",
                text: "¡Esta acción es irreversible!",
                icon: "warning",
                showCancelButton: !0,
                confirmButtonText: "Sí, solicitar reembolso",
                cancelButtonText: "No, cancelar",
                confirmButtonClass: "btn btn-success mt-2",
                cancelButtonClass: "btn btn-danger ms-2 mt-2",
                buttonsStyling: !1
            }).then(function (t) {
                t.value ? Swal.fire({
                    title: "Listo",
                    text: "Se te enviará un correo con la boleta de devolución",
                    icon: "success"
                }) : t.dismiss === Swal.DismissReason.cancel && Swal.fire({
                    title: "Cancelado",
                    text: "Tu pedido sigue en preceso",
                    icon: "error"
                })
            })
        }), $("#sa-registro").click(function (event) {
            event.preventDefault(); // Prevenir el envío automático del formulario

            Swal.fire({
                title: "¿Está seguro de postular como Agente?",
                text: "Asegúrese de haber completado bien sus datos",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Sí, postular",
                cancelButtonText: "No, cancelar",
                confirmButtonClass: "btn btn-success mt-2",
                cancelButtonClass: "btn btn-danger ms-2 mt-2",
                buttonsStyling: false
            }).then(function (result) {
                if (result.isConfirmed) {
                    // Mostrar mensaje de éxito y enviar el formulario
                    Swal.fire({
                        title: "Listo",
                        text: "Un administrador revisará tu solicitud",
                        icon: "success"
                    }).then(function () {
                        // Después de confirmar, enviamos el formulario
                        $("#registroForm").submit();
                    });
                } else if (result.dismiss === Swal.DismissReason.cancel) {
                    Swal.fire({
                        title: "Cancelado",
                        text: "Su solicitud no fue enviada",
                        icon: "error"
                    });
                }
            });
        }); t("#sa-params2").click(function () {
            Swal.fire({
                title: "¿Estás seguro de solicitar reembolso?",
                text: "¡Esta acción es irreversible!",
                icon: "warning",
                showCancelButton: !0,
                confirmButtonText: "Sí, solicitar reembolso",
                cancelButtonText: "No, cancelar",
                confirmButtonClass: "btn btn-success mt-2",
                cancelButtonClass: "btn btn-danger ms-2 mt-2",
                buttonsStyling: !1
            }).then(function (t) {
                t.value ? Swal.fire({
                    title: "Listo",
                    text: "Se te enviará un correo con la boleta de devolución",
                    icon: "success"
                }) : t.dismiss === Swal.DismissReason.cancel && Swal.fire({
                    title: "Cancelado",
                    text: "Tu pedido sigue en preceso",
                    icon: "error"
                })
            })
        })









            t("#sa-image").click(function () {
            Swal.fire({
                title: "Sweet!",
                text: "Modal with a custom image.",
                imageUrl: "assets/images/logo-dark.png",
                imageHeight: 20,
                confirmButtonColor: "#5664d2",
                animation: !1
            })
        }), t("#sa-close").click(function () {
            var t;
            Swal.fire({
                title: "Auto close alert!",
                html: "I will close in <strong></strong> seconds.",
                timer: 2e3,
                confirmButtonColor: "#5664d2",
                onBeforeOpen: function () {
                    Swal.showLoading(), t = setInterval(function () {
                        Swal.getContent().querySelector("strong").textContent = Swal.getTimerLeft()
                    }, 100)
                },
                onClose: function () {
                    clearInterval(t)
                }
            }).then(function (t) {
                t.dismiss === Swal.DismissReason.timer && console.log("I was closed by the timer")
            })
        }), t("#custom-html-alert").click(function () {
            Swal.fire({
                title: "<i>HTML</i> <u>example</u>",
                icon: "info",
                html: 'You can use <b>bold text</b>, <a href="//themesdesign.in/">links</a> and other HTML tags',
                showCloseButton: !0,
                showCancelButton: !0,
                confirmButtonClass: "btn btn-success",
                cancelButtonClass: "btn btn-danger ms-1",
                confirmButtonColor: "#47bd9a",
                cancelButtonColor: "#ff3d60",
                confirmButtonText: '<i class="fas fa-thumbs-up me-1"></i> Great!',
                cancelButtonText: '<i class="fas fa-thumbs-down"></i>'
            })
        }), t("#sa-position").click(function () {
            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "Your work has been saved",
                showConfirmButton: !1,
                timer: 1500
            })
        }), t("#custom-padding-width-alert").click(function () {
            Swal.fire({
                title: "Custom width, padding, background.",
                width: 600,
                padding: 100,
                confirmButtonColor: "#5664d2",
                background: "#fff url(//subtlepatterns2015.subtlepatterns.netdna-cdn.com/patterns/geometry.png)"
            })
        }), t("#ajax-alert").click(function () {
            Swal.fire({
                title: "Submit email to run ajax request",
                input: "email",
                showCancelButton: !0,
                confirmButtonText: "Submit",
                showLoaderOnConfirm: !0,
                confirmButtonColor: "#5664d2",
                cancelButtonColor: "#ff3d60",
                preConfirm: function (n) {
                    return new Promise(function (t, e) {
                        setTimeout(function () {
                            "taken@example.com" === n ? e("This email is already taken.") : t()
                        }, 2e3)
                    })
                },
                allowOutsideClick: !1
            }).then(function (t) {
                Swal.fire({
                    icon: "success",
                    title: "Ajax request finished!",
                    confirmButtonColor: "#5664d2",
                    html: "Submitted email: " + t
                })
            })
        }), t("#chaining-alert").click(function () {
            Swal.mixin({
                input: "text",
                confirmButtonText: "Next &rarr;",
                showCancelButton: !0,
                confirmButtonColor: "#5664d2",
                cancelButtonColor: "#74788d",
                progressSteps: ["1", "2", "3"]
            }).queue([{
                title: "Question 1",
                text: "Chaining swal2 modals is easy"
            }, "Question 2", "Question 3"]).then(function (t) {
                t.value && Swal.fire({
                    title: "All done!",
                    html: "Your answers: <pre><code>" + JSON.stringify(t.value) + "</code></pre>",
                    confirmButtonText: "Lovely!"
                })
            })
        }), t("#dynamic-alert").click(function () {
            swal.queue([{
                title: "Your public IP",
                confirmButtonColor: "#5664d2",
                confirmButtonText: "Show my public IP",
                text: "Your public IP will be received via AJAX request",
                showLoaderOnConfirm: !0,
                preConfirm: function () {
                    return new Promise(function (e) {
                        t.get("https://api.ipify.org?format=json").done(function (t) {
                            swal.insertQueueStep(t.ip), e()
                        })
                    })
                }
            }]).catch(swal.noop)
        })
    }, t.SweetAlert = new e, t.SweetAlert.Constructor = e
}(window.jQuery), function () {
    "use strict";
    window.jQuery.SweetAlert.init()
}();