async function fetchCalificacion() {
    try {
        // Ensure the backticks are used for template literals
        const response = await fetch(`/adminzonal/api/calificacion?usuarioId=${usuarioId}`);
        const data = await response.json();

        const options = {
            chart: {
                height: 410,
                type: "pie"
            },
            series: [data.na, data.est1, data.est2, data.est3, data.est4, data.est5],
            labels: ["Sin calificar", "1 estrella", "2 estrellas", "3 estrellas", "4 estrellas", "5 estrellas"],
            colors: ["#FFDD57", "#FFA07A", "#5DADE2", "#58D68D", "#E74C3C", "#AF7AC5"],
            legend: {
                show: true,
                position: "bottom",
                horizontalAlign: "center",
                verticalAlign: "middle",
                floating: false,
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
                        show: false
                    }
                }
            }]
        };

        const chart = new ApexCharts(document.querySelector("#pie_chart_2"), options);
        chart.render();
    } catch (error) {
        console.error('Error al obtener datos de agentes:', error);
    }
}

// Ejecuta fetchCalificacion cuando el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", fetchCalificacion);


async function ProductosStock() {
    try {
        const response = await fetch('/adminzonal/api/productos'); // Llama a tu API
        const data = await response.json();

        // Suponiendo que data es una lista de objetos con propiedades 'nombre' y 'stock'
        const categories = data.map(item => item.nombre); // Extrae los nombres de las categorías
        const stockData = data.map(item => item.stock); // Extrae las cantidades de stock

        // Configuración del gráfico
        const options = {
            chart: {
                height: 380,
                type: "bar",
                toolbar: {
                    show: false // Ocultar la barra de herramientas
                }
            },
            plotOptions: {
                bar: {
                    horizontal: true // Gráfico de barras horizontal
                }
            },
            dataLabels: {
                enabled: false // Deshabilitar etiquetas de datos
            },
            series: [{
                name: "Cantidad", // Nombre de la serie
                data: stockData // Usar datos de stock y revertir si es necesario
            }],
            colors: ["#1c41bb"], // Color de las barras
            grid: {
                borderColor: "#f1f1f1", // Color del borde de la cuadrícula
                padding: {
                    bottom: 5 // Espaciado inferior
                }
            },
            xaxis: {
                categories: categories, // Usar nombres de categorías y revertir si es necesario
            },
            legend: {
                offsetY: 5 // Desplazamiento vertical de la leyenda
            }
        };

        // Crear y renderizar el gráfico
        const chart = new ApexCharts(document.querySelector("#bar_chart1_top10_3"), options);
        chart.render();
    } catch (error) {
        console.error('Error al obtener datos:', error);
    }
}

// Llama a la función cuando el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", function() {
    ProductosStock();
});

async function fetchTop10Data() {
    try {
        const response = await fetch('/adminzonal/api/top10'); // Llama a tu API
        const data = await response.json();

        // Suponiendo que data es una lista de objetos con propiedades 'nombre' y 'stock'
        const categories = data.map(item => item.nombre); // Extrae los nombres de las categorías
        const stockData = data.map(item => item.stock); // Extrae las cantidades de stock

        // Configuración del gráfico
        const options = {
            chart: {
                height: 380,
                type: "bar",
                toolbar: {
                    show: false // Ocultar la barra de herramientas
                }
            },
            plotOptions: {
                bar: {
                    horizontal: true // Gráfico de barras horizontal
                }
            },
            dataLabels: {
                enabled: false // Deshabilitar etiquetas de datos
            },
            series: [{
                name: "Cantidad", // Nombre de la serie
                data: stockData // Usar datos de stock y revertir si es necesario
            }],
            colors: ["#9779e3"], // Color de las barras
            grid: {
                borderColor: "#f1f1f1", // Color del borde de la cuadrícula
                padding: {
                    bottom: 5 // Espaciado inferior
                }
            },
            xaxis: {
                categories: categories, // Usar nombres de categorías y revertir si es necesario
            },
            legend: {
                offsetY: 5 // Desplazamiento vertical de la leyenda
            }
        };

        // Crear y renderizar el gráfico
        const chart = new ApexCharts(document.querySelector("#bar_chart1_top10_2"), options);
        chart.render();
    } catch (error) {
        console.error('Error al obtener datos:', error);
    }
}

// Llama a la función cuando el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", function() {
    fetchTop10Data();
});

async function fetchTop10CompaniesData() {
    try {
        const response = await fetch('/adminzonal/api/top10Importadores'); // Llama a tu API

        // Verificar si la respuesta fue exitosa
        if (!response.ok) {
            throw new Error('Error en la respuesta de la API: ' + response.status);
        }

        const data = await response.json();

        // Suponiendo que data es una lista de objetos con propiedades 'nombre' y 'stock'
        const categories = data.map(item => item.nombre); // Extrae los nombres de las empresas
        const stockData = data.map(item => item.stock); // Extrae las cantidades de stock

        // Configuración del gráfico
        const options = {
            chart: {
                height: 380,
                type: "bar",
                toolbar: {
                    show: false // Ocultar la barra de herramientas
                }
            },
            plotOptions: {
                bar: {
                    horizontal: true // Gráfico de barras horizontal
                }
            },
            dataLabels: {
                enabled: false // Deshabilitar etiquetas de datos
            },
            series: [{
                name: "Cantidad", // Nombre de la serie
                data: stockData // Usar datos de stock y revertir si es necesario
            }],
            colors: ["#1CBB8C"], // Color de las barras
            grid: {
                borderColor: "#f1f1f1", // Color del borde de la cuadrícula
                padding: {
                    bottom: 5 // Espaciado inferior
                }
            },
            xaxis: {
                categories: categories, // Usar nombres de las empresas y revertir si es necesario
            },
            legend: {
                offsetY: 5 // Desplazamiento vertical de la leyenda
            }
        };

        // Crear y renderizar el gráfico
        const chart = new ApexCharts(document.querySelector("#bar_chart2_top10_2"), options);
        chart.render();
    } catch (error) {
        console.error('Error al obtener datos:', error);
    }
}

// Llama a la función cuando el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", function() {
    fetchTop10CompaniesData();
});

async function fetchUsuariosData() {
    try {
        const response = await fetch('/adminzonal/api/countUsuarios');
        const data = await response.json();

        const options = {
            chart: {
                height: 410,
                type: "pie"
            },
            series: [data.active, data.inactive],
            labels: ["Usuarios activos", "Usuarios inactivos"],
            colors: ["#fcb92c", "#4aa3ff"],
            legend: {
                show: true,
                position: "bottom",
                horizontalAlign: "center",
                verticalAlign: "middle",
                floating: false,
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
                        show: false
                    }
                }
            }]
        };

        const chart = new ApexCharts(document.querySelector("#pie_chart_AdminZonal_2"), options);
        chart.render();
    } catch (error) {
        console.error('Error al obtener datos de usuarios:', error);
    }
}

// Ejecuta fetchUsuariosData cuando el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", fetchUsuariosData);