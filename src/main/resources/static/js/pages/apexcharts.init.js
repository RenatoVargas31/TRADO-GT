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
        series: [{
            name: "Creado",
            data: [12, 45, 18, 70, 30, 55, 65, 20, 75, 40, 50, 80]
        },
            {
                name: "En validación",
                data: [15, 50, 22, 60, 28, 48, 72, 30, 63, 50, 40, 78]
            },
            {
                name: "En proceso",
                data: [20, 38, 25, 42, 30, 70, 55, 35, 65, 45, 75, 78]
            },
            {
                name: "Arribo al País",
                data: [18, 35, 24, 40, 29, 45, 50, 32, 60, 38, 70, 75]
            },
            {
                name: "En Aduanas",
                data: [22, 38, 28, 43, 33, 48, 53, 35, 63, 40, 73, 78]
            },
            {
                name: "En ruta",
                data: [25, 40, 30, 45, 35, 50, 55, 38, 65, 42, 75, 80]
            },
            {
                name: "Recibido",
                data: [28, 43, 33, 48, 38, 53, 58, 40, 68, 45, 78, 80]
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
            min: 10,
            max: 90
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
    },
    chart = new ApexCharts(document.querySelector("#line_chart_datalabel"), options),


    options = (chart.render(), {
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
        colors: ["#5664d2", "#fcb92c", "#1cbb8c"],
        dataLabels: {
            enabled: !1
        },
        stroke: {
            width: [3, 4, 3],
            curve: "straight",
            dashArray: [0, 8, 5]
        },
        series: [{
            name: "Satisfechos",
            data: [45, 52, 38, 24, 33, 26, 21, 20, 6, 8, 15, 10]

        }, {
            name: "No satisfechos",
            data: [89, 56, 74, 98, 72, 38, 64, 46, 84, 58, 46, 49]
        }],
        title: {
            text: "Curva de satisfaccion",
            align: "left"
        },
        markers: {
            size: 0,
            hover: {
                sizeOffset: 6
            }
        },
        xaxis: {
            categories: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"]
        },
        tooltip: {
            y: [{
                title: {
                    formatter: function(e) {
                        return e + " (mins)"
                    }
                }
            }, {
                title: {
                    formatter: function(e) {
                        return e + " per session"
                    }
                }
            }, {
                title: {
                    formatter: function(e) {
                        return e
                    }
                }
            }]
        },
        grid: {
            borderColor: "#f1f1f1",
            padding: {
                bottom: 5
            }
        },
        legend: {
            offsetY: 5
        }
    }),
    options = ((chart = new ApexCharts(document.querySelector("#line_chart_dashed"), options)).render(), {
        chart: {
            height: 350,
            type: "area"
        },
        dataLabels: {
            enabled: !1
        },
        stroke: {
            curve: "smooth",
            width: 3
        },
        series: [{
            name: "series1",
            data: [34, 40, 28, 52, 42, 109, 100]
        }, {
            name: "series2",
            data: [32, 60, 34, 46, 34, 52, 41]
        }],
        colors: ["#5664d2", "#1cbb8c"],
        xaxis: {
            type: "datetime",
            categories: ["2018-09-19T00:00:00", "2018-09-19T01:30:00", "2018-09-19T02:30:00", "2018-09-19T03:30:00", "2018-09-19T04:30:00", "2018-09-19T05:30:00", "2018-09-19T06:30:00"]
        },
        grid: {
            borderColor: "#f1f1f1",
            padding: {
                bottom: 15
            }
        },
        tooltip: {
            x: {
                format: "dd/MM/yy HH:mm"
            }
        },
        legend: {
            offsetY: 7
        }
    }),
    options = ((chart = new ApexCharts(document.querySelector("#spline_area"), options)).render(), {
        chart: {
            height: 350,
            type: "bar",
            toolbar: {
                show: !1
            }
        },
        plotOptions: {
            bar: {
                horizontal: !1,
                columnWidth: "45%",
                endingShape: "rounded"
            }
        },
        dataLabels: {
            enabled: !1
        },
        stroke: {
            show: !0,
            width: 2,
            colors: ["transparent"]
        },
        series: [{
            name: "1 Estrella",
            data: [46, 57, 59, 54, 62, 58, 64, 60, 66, 10, 61, 42]
        }, {
            name: "2 Estrella",
            data: [74, 83, 102, 97, 86, 106, 93, 114, 94, 23, 20, 65]
        }, {
            name: "3 Estrella",
            data: [74, 83, 10, 97, 26, 36, 93, 46, 94, 58, 92, 13]
        },
            {
                name: "4 Estrella",
                data: [50, 83, 72, 97, 20, 106, 93, 34, 94, 53, 76, 91]
            },
            {
                name: "5 Estrella",
                data: [37, 42, 38, 26, 47, 50, 54, 55, 43, 61, 72, 43]
            }
        ],
        colors: ["#5664d2", "#1cbb8c", "#fcb92c"],
        xaxis: {
            categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
        },
        yaxis: {
            title: {
                text: "Cantidad de Usuarios"
            }
        },
        grid: {
            borderColor: "#f1f1f1",
            padding: {
                bottom: 10
            }
        },
        fill: {
            opacity: 1
        },
        tooltip: {
            y: {
                formatter: function(e) {
                    return e + " stars"
                }
            }
        },
        legend: {
            offsetY: 7
        }
    }),
    options = ((chart = new ApexCharts(document.querySelector("#column_chart"), options)).render(), {
        chart: {
            height: 325,
            type: "bar",
            toolbar: {
                show: !1
            }
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    position: "top"
                }
            }
        },
        dataLabels: {
            enabled: !0,
            formatter: function(e) {
                return e + " unidades";
            },
            offsetY: -20,
            style: {
                fontSize: "12px",
                colors: ["#304758"]
            }
        },
        series: [{
            name: "Stock",
            data: [5, 3, 8, 2, 4, 6, 1, 3, 5, 2, 1, 4] // Ejemplo de stock bajo para productos
        }],
        colors: ["#FF5733"], // Color que refleja atención o bajo stock
        grid: {
            borderColor: "#f1f1f1",
            padding: {
                bottom: 10
            }
        },
        xaxis: {
            categories: ["Producto A", "Producto B", "Producto C", "Producto D", "Producto E", "Producto F", "Producto G", "Producto H", "Producto I", "Producto J", "Producto K", "Producto L"], // Nombres de los productos
            position: "top",
            labels: {
                offsetY: -18
            },
            axisBorder: {
                show: !1
            },
            axisTicks: {
                show: !1
            },
            crosshairs: {
                fill: {
                    type: "gradient",
                    gradient: {
                        colorFrom: "#D8E3F0",
                        colorTo: "#BED1E6",
                        stops: [0, 100],
                        opacityFrom: .4,
                        opacityTo: .5
                    }
                }
            },
            tooltip: {
                enabled: !0,
                offsetY: -35
            }
        },
        fill: {
            gradient: {
                shade: "light",
                type: "horizontal",
                shadeIntensity: .25,
                gradientToColors: void 0,
                inverseColors: !0,
                opacityFrom: 1,
                opacityTo: 1,
                stops: [50, 0, 100, 100]
            }
        },
        yaxis: {
            axisBorder: {
                show: !1
            },
            axisTicks: {
                show: !1
            },
            labels: {
                show: !1,
                formatter: function(e) {
                    return e + " unidades";
                }
            }
        },
        title: {
            text: "Productos con bajo stock",
            floating: !0,
            offsetY: 320,
            align: "center",
            style: {
                color: "#444"
            }
        },
        legend: {
            offsetY: 7
        }

    }),
    options = ((chart = new ApexCharts(document.querySelector("#column_chart_Stock"), options)).render(), {
        chart: {
            height: 350,
            type: "bar",
            toolbar: {
                show: !1
            }
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    position: "top"
                }
            }
        },
        dataLabels: {
            enabled: !0,
            formatter: function(e) {
                return e + "%"
            },
            offsetY: -20,
            style: {
                fontSize: "12px",
                colors: ["#304758"]
            }
        },
        series: [{
            name: "Inflation",
            data: [2.5, 3.2, 5, 10.1, 4.2, 3.8, 3, 2.4, 4, 1.2, 3.5, .8]
        }],
        colors: ["#5664d2"],
        grid: {
            borderColor: "#f1f1f1",
            padding: {
                bottom: 10
            }
        },
        xaxis: {
            categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
            position: "top",
            labels: {
                offsetY: -18
            },
            axisBorder: {
                show: !1
            },
            axisTicks: {
                show: !1
            },
            crosshairs: {
                fill: {
                    type: "gradient",
                    gradient: {
                        colorFrom: "#D8E3F0",
                        colorTo: "#BED1E6",
                        stops: [0, 100],
                        opacityFrom: .4,
                        opacityTo: .5
                    }
                }
            },
            tooltip: {
                enabled: !0,
                offsetY: -35
            }
        },
        fill: {
            gradient: {
                shade: "light",
                type: "horizontal",
                shadeIntensity: .25,
                gradientToColors: void 0,
                inverseColors: !0,
                opacityFrom: 1,
                opacityTo: 1,
                stops: [50, 0, 100, 100]
            }
        },
        yaxis: {
            axisBorder: {
                show: !1
            },
            axisTicks: {
                show: !1
            },
            labels: {
                show: !1,
                formatter: function(e) {
                    return e + "%"
                }
            }
        },
        title: {
            text: "Monthly Inflation in Argentina, 2002",
            floating: !0,
            offsetY: 320,
            align: "center",
            style: {
                color: "#444"
            }
        },
        legend: {
            offsetY: 7
        }
    }),
    options = ((chart = new ApexCharts(document.querySelector("#column_chart_datalabel"), options)).render(), {
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
            data: [380, 430, 450, 475, 550, 584, 780, 1100, 1220, 1365].reverse()
        }],
        colors: ["#bb1c41"],
        grid: {
            borderColor: "#f1f1f1",
            padding: {
                bottom: 5
            }
        },
        xaxis: {
            categories: [
                "Electrónica",
                "Libros",
                "Ropa",
                "Electrodomésticos",
                "Juguetes",
                "Equipo Deportivo",
                "Productos de Belleza",
                "Comestibles",
                "Muebles",
                "Automotriz"
            ]
        },
        legend: {
            offsetY: 5
        }
    }),
    options = ((chart = new ApexCharts(document.querySelector("#bar_chart"), options)).render(), {
        chart: {
            height: 350,
            type: "line",
            stacked: !1,
            toolbar: {
                show: !1
            }
        },
        stroke: {
            width: [0, 2, 4],
            curve: "smooth"
        },
        plotOptions: {
            bar: {
                columnWidth: "50%"
            }
        },
        colors: ["#1cbb8c", "#fcb92c", "#5664d2"],
        series: [{
            name: "Team A",
            type: "column",
            data: [23, 11, 22, 27, 13, 22, 37, 21, 44, 22, 30]
        }, {
            name: "Team B",
            type: "area",
            data: [44, 55, 41, 67, 22, 43, 21, 41, 56, 27, 43]
        }, {
            name: "Team C",
            type: "line",
            data: [30, 25, 36, 30, 45, 35, 64, 52, 59, 36, 39]
        }],
        fill: {
            opacity: [.85, .25, 1],
            gradient: {
                inverseColors: !1,
                shade: "light",
                type: "vertical",
                opacityFrom: .85,
                opacityTo: .55,
                stops: [0, 100, 100, 100]
            }
        },
        labels: ["01/01/2003", "02/01/2003", "03/01/2003", "04/01/2003", "05/01/2003", "06/01/2003", "07/01/2003", "08/01/2003", "09/01/2003", "10/01/2003", "11/01/2003"],
        markers: {
            size: 0
        },
        xaxis: {
            type: "datetime"
        },
        yaxis: {
            title: {
                text: "Points"
            }
        },
        tooltip: {
            shared: !0,
            intersect: !1,
            y: {
                formatter: function(e) {
                    return void 0 !== e ? e.toFixed(0) + " points" : e
                }
            }
        },
        grid: {
            borderColor: "#f1f1f1",
            padding: {
                bottom: 10
            }
        },
        legend: {
            offsetY: 7
        }
    }),
    options = ((chart = new ApexCharts(document.querySelector("#mixed_chart"), options)).render(), {
        chart: {
            height: 350,
            type: "radialBar"
        },
        plotOptions: {
            radialBar: {
                dataLabels: {
                    name: {
                        fontSize: "22px"
                    },
                    value: {
                        fontSize: "16px"
                    },
                    total: {
                        show: !0,
                        label: "Total",
                        formatter: function(e) {
                            return 249
                        }
                    }
                }
            }
        },
        series: [44, 55, 67, 83],
        labels: ["Computer", "Tablet", "Laptop", "Mobile"],
        colors: ["#5664d2", "#fcb92c", "#1cbb8c", "#ff3d60"],
        legend: {
            offsetY: 5
        }
    }),
    options = ((chart = new ApexCharts(document.querySelector("#radial_chart"), options)).render(), {
        chart: {
            height: 320,
            type: "pie"
        },
        series: [44, 55, 41, 17, 15],
        labels: ["Series 1", "Series 2", "Series 3", "Series 4", "Series 5"],
        colors: ["#1cbb8c", "#5664d2", "#fcb92c", "#4aa3ff", "#ff3d60"],
        legend: {
            show: !0,
            position: "bottom",
            horizontalAlign: "center",
            verticalAlign: "middle",
            floating: !1,
            fontSize: "14px",
            offsetX: 0,
            offsetY: 5
        },
        responsive: [{
            breakpoint: 600,
            options: {
                chart: {
                    height: 240
                },
                legend: {
                    show: !1
                }
            }
        }]
    }),
    options = ((chart = new ApexCharts(document.querySelector("#pie_chart"), options)).render(), {
        chart: {
            height: 410,
            type: "pie"
        },
        series: [69.24, 30.76],
        labels: ["Usuarios activos", "Usuarios inactivos"],
        colors: ["#fcb92c", "#4aa3ff"],
        legend: {
            show: !0,
            position: "bottom",
            horizontalAlign: "center",
            verticalAlign: "middle",
            floating: !1,
            fontSize: "14px",
            offsetX: 0,
            offsetY: 5
        },
        responsive: [{
            breakpoint: 600,
            options: {
                chart: {
                    height: 240
                },
                legend: {
                    show: !1
                }
            }
        }]
    }),
    options = ((chart = new ApexCharts(document.querySelector("#pie_chart_AdminZonal"), options)).render(), {
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
            data: [380, 430, 450, 475, 550, 584, 780, 1100, 1220, 1365].reverse()
        }],
        colors: ["#1c41bb"],
        grid: {
            borderColor: "#f1f1f1",
            padding: {
                bottom: 5
            }
        },
        xaxis: {
            categories: [
                "Electrónica",
                "Libros",
                "Ropa",
                "Electrodomésticos",
                "Juguetes",
                "Equipo Deportivo",
                "Productos de Belleza",
                "Comestibles",
                "Muebles",
                "Automotriz"
            ]
        },
        legend: {
            offsetY: 5
        }
    }),
    options = ((chart = new ApexCharts(document.querySelector("#bar_chart1_top10"), options)).render(), {
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
            data: [370, 420, 460, 510, 590, 640, 850, 1150, 1280, 1450].reverse()
        }],
        colors: ["#1CBB8C"],
        grid: {
            borderColor: "#f1f1f1",
            padding: {
                bottom: 5
            }
        },
        xaxis: {
            categories: [
                "TradeMaster01",
                "GlobalCargoX",
                "ImportKing",
                "MundoComercio",
                "LogisticsPro",
                "OceanFreight",
                "TransWorldInc",
                "MegaImporter",
                "DirectTradeHub",
                "UniversalFreight"
            ]
        },
        legend: {
            offsetY: 5
        }
    }),
    options = ((chart = new ApexCharts(document.querySelector("#bar_chart2_top10"), options)).render(), {
        chart: {
            height: 320,
            type: "donut"
        },
        series: [44, 55, 41, 17, 15],
        labels: ["Series 1", "Series 2", "Series 3", "Series 4", "Series 5"],
        colors: ["#1cbb8c", "#5664d2", "#fcb92c", "#4aa3ff", "#ff3d60"],
        legend: {
            show: !0,
            position: "bottom",
            horizontalAlign: "center",
            verticalAlign: "middle",
            floating: !1,
            fontSize: "14px",
            offsetX: 0,
            offsetY: 5
        },
        responsive: [{
            breakpoint: 600,
            options: {
                chart: {
                    height: 240
                },
                legend: {
                    show: !1
                }
            }
        }]
    });
(chart = new ApexCharts(document.querySelector("#donut_chart"), options)).render();
