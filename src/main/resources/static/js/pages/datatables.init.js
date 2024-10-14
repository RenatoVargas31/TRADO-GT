$(document).ready(function () {
    $("#datatable").DataTable({
        language: {
            paginate: {
                previous: "<i class='mdi mdi-chevron-left'>",
                next: "<i class='mdi mdi-chevron-right'>"
            }
        },
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded");
        }
    });
});
$(document).ready(function () {
    var a = $("#datatable-buttons").DataTable({
        lengthChange: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
    });
    a.buttons().container().appendTo("#datatable-buttons_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//DATATABLES - SUPERADMIN
//AdminZonal - Inicio
$(document).ready(function () {
    var a = $("#datatable-admzonal-inicio").DataTable({
        lengthChange: !1,
        pageLength: 5,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-admzonal-inicio_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Agente de Compra - Inicio
$(document).ready(function () {
    var a = $("#datatable-agentcompra-inicio").DataTable({
        lengthChange: !1,
        pageLength: 5,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-agentcompra-inicio_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Importador - Inicio
$(document).ready(function () {
    var a = $("#datatable-importador-inicio").DataTable({
        lengthChange: !1,
        pageLength: 5,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-importador-inicio_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//AdminZonal - Activos/Inactivos
$(document).ready(function () {
    var a = $("#datatable-admzonal-activo").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-admzonal-activo_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-admzonal-inactivo").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-admzonal-inactivo_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Agente de Compra - Postula/Activos/Inactivos
$(document).ready(function () {
    var a = $("#datatable-agentcompra-postula").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-agentcompra-postula_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-agentcompra-activo").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-agentcompra-activo_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-agentcompra-inactivo").DataTable({
        lengthChange: !1,
        pageLength: 5,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-agentcompra-inactivo_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Importador - Solicitud/Activos/Inactivos
$(document).ready(function () {
    var a = $("#datatable-importador-solicitud").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-importador-solicitud_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-importador-activo").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-importador-activo_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-importador-inactivo").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-importador-inactivo_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Productos - Reposiciones
$(document).ready(function () {
    var a = $("#datatable-producto-repo").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-producto-repo_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Productos - Listado
$(document).ready(function () {
    var a = $("#datatable-producto-lista-almacen").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false },// Deshabilitar búsqueda en la última columna
        ]
    });

    a.buttons().container().appendTo("#datatable-producto-lista_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-producto-lista-norte").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-producto-lista_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-producto-lista-sur").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-producto-lista_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-producto-lista-este").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-producto-lista_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-producto-lista-oeste").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-producto-lista_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Proveedores - Listado
$(document).ready(function () {
    var a = $("#datatable-proveedor-lista").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["excel", "pdf", "csv"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-proveedor-lista_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});

//DATATABLES - ADMINZONAL
//Tabla de fechas de arribo
$(document).ready(function () {
    var a = $("#datatable-fechaArribo").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["csv", "excel", "pdf"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-fechaArribo_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tabla de reposicón de productos
$(document).ready(function () {
    var a = $("#datatable-reposicionProducto").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["csv", "excel", "pdf"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-reposicionProducto_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});


//DATATABLES - AGENTE
//Tabla de todos las órdenes
$(document).ready(function () {
    var a = $("#datatable-ordenesCompraAll").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: [
            {
                extend: 'csv',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'excel',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'pdf',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            }
        ],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-ordenesCompraAll_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tablas de órdenes sin asignar
$(document).ready(function () {
    var a = $("#datatable-ordenesCompraSinAsignar").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: [
            {
                extend: 'csv',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'excel',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'pdf',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            }
        ],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-ordenesCompraSinAsignar_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tablas de órdenes pendientes
$(document).ready(function () {
    var a = $("#datatable-ordenesCompraPendientes").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: [
            {
                extend: 'csv',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'excel',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'pdf',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            }
        ],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-ordenesCompraPendientes_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tablas de órdenes en proceso
$(document).ready(function () {
    var a = $("#datatable-ordenesCompraEnProceso").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: [
            {
                extend: 'csv',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'excel',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'pdf',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            }
        ],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-ordenesCompraEnProceso_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tablas de órdenes resueltas
$(document).ready(function () {
    var a = $("#datatable-ordenesCompraResueltas").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: [
            {
                extend: 'csv',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'excel',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'pdf',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            }
        ],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-ordenesCompraResueltas_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tablas de todos los usuarios
$(document).ready(function () {
    var a = $("#datatable-usuariosTotales").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: [
            {
                extend: 'csv',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'excel',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'pdf',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            }
        ],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-usuariosTotales_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tablas de usuarios habilitados
$(document).ready(function () {
    var a = $("#datatable-usuariosHabilitados").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: [
            {
                extend: 'csv',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'excel',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'pdf',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            }
        ],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-usuariosHabilitados_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tablas de usuarios bloqueados
$(document).ready(function () {
    var a = $("#datatable-usuariosBaneados").DataTable({
        lengthChange: !1,
        pageLength: 7,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: [
            {
                extend: 'csv',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'excel',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'pdf',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            }
        ],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-usuariosBaneados_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});

//Tabla de productos de usuarios (esta tabla cambiará de info dependiendo del usuario, manejar en el
//html supongo
$(document).ready(function () {
    var a = $("#datatable-productoUsuario").DataTable({
        lengthChange: !1,
        pageLength: 3,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: [
            {
                extend: 'csv',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'excel',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            },
            {
                extend: 'pdf',
                exportOptions: {
                    columns: ':not(:last-child)' // Excluir la última columna
                }
            }
        ],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-productoUsuario_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
$(document).ready(function () {
    var a = $("#datatable-historialComportamiento").DataTable({
        lengthChange: !1,
        pageLength: 3,
        info: !1,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
        buttons: ["csv", "excel", "pdf"],
        columnDefs: [
            { targets: -1, searchable: false } // Deshabilitar búsqueda en la última columna
        ]
    });
    a.buttons().container().appendTo("#datatable-historialComportamiento_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tabla para los proovedores que se ven en las órdenes de agente
$(document).ready(function () {
    var a = $("#datatable-proveedores").DataTable({
        lengthChange: !1,
        pageLength: 3,
        info: !1,
        searching: false,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
    });
    a.buttons().container().appendTo("#datatable-proveedores_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});
//Tabla para los productos que se ven en las órdenes de agente
$(document).ready(function () {
    var a = $("#datatable-productosOrdenInfo").DataTable({
        lengthChange: !1,
        pageLength: 3,
        info: !1,
        searching: false,
        language: {paginate: {previous: "<i class='mdi mdi-chevron-left'>", next: "<i class='mdi mdi-chevron-right'>"}},
        drawCallback: function () {
            $(".dataTables_paginate > .pagination").addClass("pagination-rounded")
        },
    });
    a.buttons().container().appendTo("#datatable-productosOrdenInfo_wrapper .col-md-6:eq(0)");
    $(".dataTables_length select").addClass("form-select form-select-sm");
});