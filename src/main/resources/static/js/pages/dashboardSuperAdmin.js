document.addEventListener("DOMContentLoaded", function() {
    // Estado de Usuarios Mensual
    $.ajax({
        url: '/superAdmin/UsuariosMes',
        method: 'GET',
        success: function(data) {
            // Calcular la suma de los elementos de cada lista
            const sumaRegistrados = data.usuariosRegistradosPorMes.reduce((a, b) => a + b, 0);
            const sumaActivos = data.usuariosActivosPorMes.reduce((a, b) => a + b, 0);
            const sumaInactivos = data.usuariosInactivosPorMes.reduce((a, b) => a + b, 0);

            // Actualizar el contenido de los elementos HTML con las sumas calculadas
            document.getElementById('registrados').textContent = sumaRegistrados;
            document.getElementById('activos').textContent = sumaActivos;
            document.getElementById('inactivos').textContent = sumaInactivos;
            // Ocultar el indicador de carga
            //document.getElementById("loadingIndicator").style.display = "none";
            console.log("Datos recibidos:", data);
            // Configuración de ApexCharts con los datos recibidos
            var options = {
                series: [
                    {
                        name: "Registrados",
                        type: "column",
                        data: data.usuariosRegistradosPorMes // Suma de Activo e Inactivo
                    },
                    {
                        name: "Activo",
                        type: "column",
                        data: data.usuariosActivosPorMes
                    },
                    {
                        name: "Inactivo",
                        type: "column",
                        data: data.usuariosInactivosPorMes
                    }
                ],
                chart: {height: 320, type: "line", toolbar: {show: !1}},
                stroke: {width: [0, 0, 0], curve: "smooth"},
                plotOptions: {bar: {horizontal: !1, columnWidth: "65%"}},
                dataLabels: {enabled: !1},
                legend: {show: !1},
                colors: ["#5664d2", "#1cbb8c", "#fcb92c"],
                labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
            };

            // Renderizar la gráfica
            var chartUsuarioMes = new ApexCharts(document.querySelector("#UsuariosMensual"), options);
            chartUsuarioMes.render();
        },
        error: function(xhr, status, error) {
            console.error("Error al cargar los datos:", error);
        }
    });

    // Seguimiento de Órdenes
    $.ajax({
        url: '/superAdmin/OrdenEstado',
        method: 'GET',
        success: function(data) {
            // Ocultar el indicador de carga
            //document.getElementById("loadingIndicator").style.display = "none";
            console.log("Datos recibidos:", data);
            // Configuración de ApexCharts con los datos recibidos
            var options = {
                chart: {
                    height: 380,
                    type: "line",
                    zoom: {
                        enabled: !1
                    },
                    toolbar: {
                        show: !1
                    }
                },
                colors: ["#5664d2", "#1cbb8c", "#fcb92c", "#ff5733", "#33ff57", "#3357ff", "#ff33a1"], // Añadido nuevos colores
                dataLabels: {
                    enabled: !1
                },
                stroke: {
                    width: [3, 3, 3, 3, 3, 3, 3],
                    curve: "straight"
                }, // Añadido nuevos anchos de línea
                series: [
                    {
                        name: "Creado",
                        data: data.ordenesCreadas
                    },
                    {
                        name: "En validación",
                        data: data.ordenesValidadas
                    },
                    {
                        name: "En proceso",
                        data: data.ordenesEnProceso
                    },
                    {
                        name: "Arribo al País",
                        data: data.ordenesArribo
                    },
                    {
                        name: "En Aduanas",
                        data: data.ordenesEnAduanas
                    },
                    {
                        name: "En ruta",
                        data: data.ordenesEnRuta
                    },
                    {
                        name: "Recibido",
                        data: data.ordenesRecibido
                    }
                ],
                title: {
                    text: ".",
                    align: "left"
                },
                grid: {
                    row: {
                        colors: ["transparent", "transparent"],
                        opacity: .2
                    },
                    borderColor: "#f1f1f1"
                },
                markers: {
                    style: "inverted",
                    size: 6
                },
                xaxis: {
                    categories: ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"],
                    title: {
                        text: "Meses"
                    }
                },
                yaxis: {
                    title: {
                        text: "N° Productos"
                    },
                    min: 0,
                    max: 50,
                },
                legend: {
                    position: "top",
                    horizontalAlign: "right",
                    floating: !0,
                    offsetY: -25,
                    offsetX: -5
                },
                responsive: [{
                    breakpoint: 600,
                    options: {
                        chart: {
                            toolbar: {
                                show: !1
                            }
                        },
                        legend: {
                            show: !1
                        }
                    }
                }]
            };

            // Renderizar la gráfica
            var chartOrdenEstado = new ApexCharts(document.querySelector("#OrdenEstado"), options);
            chartOrdenEstado.render();
        },
    });
    // Productos más vendidos (Top 5)
    $.ajax({
        url: '/superAdmin/ProductosMasVendidos',
        method: 'GET',
        success: function(data) {
            // Ocultar el indicador de carga
            //document.getElementById("loadingIndicator").style.display = "none";
            console.log("Datos recibidos:", data);
            // Configuración de ApexCharts con los datos recibidos
            var options = {
                chart: {
                    height: 380,
                    type: "bar",
                    toolbar: {
                        show: !1
                    }
                },
                plotOptions: {
                    bar: {
                        horizontal: !0
                    }
                },
                dataLabels: {
                    enabled: !1
                },
                series: [{
                    data: data.cantidadProductos
                }],
                colors: ["#bb1c41"],
                grid: {
                    borderColor: "#f1f1f1",
                    padding: {
                        bottom: 5
                    }
                },
                xaxis: {
                    categories: data.nombresProductos
                },
                legend: {
                    offsetY: 5
                }
            };

            // Renderizar la gráfica
            var chartProductosMasVendidos = new ApexCharts(document.querySelector("#ProductosMasVendidos"), options);
            chartProductosMasVendidos.render();
        },
    })
});