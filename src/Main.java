public class Main {
    private static final VistaConsola vista = new VistaConsola();
    private static final Registro registro = new Registro();

    public static void main(String[] args) {
        vista.mostrar("TecniHogar - Laboratorio 3");
        vista.mostrar("El enunciado no indica el monto de la tarifa base del mantenimiento.");
        MantenimientoAireAcondicionado.setTarifaBase(vista.leerFloatPositivo("Ingrese esa tarifa base: Q"));
        cargarIniciales();
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.leerEntero("Opcion: ");
            switch (opcion) {
                case 1: cotizarYRegistrar(); break;
                case 2: registrarCuadrilla(); break;
                case 3: crearJornada(); break;
                case 4: asignarOrden(); break;
                case 5: retirarOrden(); break;
                case 6: iniciarJornada(); break;
                case 7: registrarResultado(); break;
                case 8: consultarOrdenes(); break;
                case 9: consultarCuadrillas(); break;
                case 10: consultarJornadas(); break;
                case 11: registro.mostrarReportes(); break;
                case 0: vista.mostrar("Hasta luego."); break;
                default: vista.mostrar("Opcion no disponible.");
            }
        } while (opcion != 0);
    }

    private static void cargarIniciales() {
        registro.agregarInicial(new Diagnostico(101, "Ana", "Zona 1", 1f, true));
        registro.agregarInicial(new Diagnostico(102, "Luis", "San Lucas", 1.5f, false));
        registro.agregarInicial(new Instalacion(103, "Marta", "Zona 10", 2.5f, true, 5));
        registro.agregarInicial(new Instalacion(104, "Carlos", "Mixco", 1.25f, false, 1));
        registro.agregarInicial(new InstalacionElectricaCertificada(105, "Sara", "Zona 4", 2.5f, true, 5, 2000f));
        registro.agregarInicial(new InstalacionElectricaCertificada(106, "Pablo", "Fraijanes", 1f, false, 2, 750f));
        registro.agregarInicial(new MantenimientoAireAcondicionado(107, "Julia", "Zona 14", 1.5f, true, 12000));
        registro.agregarInicial(new MantenimientoAireAcondicionado(108, "Mario", "Zona 15", 2f, true, 24000));
        registro.registrarCuadrilla(new Cuadrilla(1, 8f, false));
        registro.registrarCuadrilla(new Cuadrilla(2, 6f, true));
        registro.registrarCuadrilla(new Cuadrilla(3, 4f, false));
    }

    private static int indicarCategoria() {
        vista.mostrar("1. Diagnostico\n2. Instalacion\n3. Instalacion electrica certificada\n4. Mantenimiento de aire acondicionado");
        return vista.leerEntero("Categoria: ");
    }

    private static Orden indicarDatos(int categoria) {
        int codigo = vista.leerEnteroPositivo("Codigo: ");
        if (registro.buscarOrden(codigo) != null) {
            vista.mostrar("Ese codigo ya pertenece a otra orden.");
            return null;
        }
        String cliente = vista.leerTexto("Nombre del cliente: ");
        String direccion = vista.leerTexto("Direccion: ");
        float horas = vista.leerFloatPositivo("Horas estimadas: ");
        int zona = vista.leerOpcion("Zona (1 metropolitana, 2 periferica): ", 1, 2);
        boolean metropolitana = zona == 1;
        try {
            switch (categoria) {
                case 1:
                    return new Diagnostico(codigo, cliente, direccion, horas, metropolitana);
                case 2:
                    return new Instalacion(codigo, cliente, direccion, horas, metropolitana,
                            vista.leerEnteroPositivo("Puntos de instalacion: "));
                case 3:
                    return new InstalacionElectricaCertificada(codigo, cliente, direccion,
                            horas, metropolitana, vista.leerEnteroPositivo("Puntos de instalacion: "),
                            vista.leerFloatPositivo("Valor declarado de materiales: Q"));
                case 4:
                    return new MantenimientoAireAcondicionado(codigo, cliente, direccion,
                            horas, metropolitana, vista.leerEnteroPositivo("Capacidad del equipo en BTU: "));
                default:
                    return null;
            }
        } catch (IllegalArgumentException ex) {
            vista.mostrar("Orden rechazada: " + ex.getMessage());
            return null;
        }
    }

    private static void cotizarYRegistrar() {
        int categoria = indicarCategoria();
        if (categoria < 1 || categoria > 4) {
            vista.mostrar("Categoria no disponible.");
            return;
        }
        Orden orden = indicarDatos(categoria);
        if (orden == null) return;
        vista.mostrar("Costo: " + VistaConsola.dinero(orden.calcularCosto()));
        vista.mostrar("Visita estimada en " + orden.calcularTiempoEstimado() + " dias habiles.");
        if (vista.leerSiNo("Acepta la cotizacion y registra la orden? (s/n): ")) {
            if (registro.registrarOrden(orden)) vista.mostrar("Orden registrada y cobrada. Estado: PENDIENTE.");
            else vista.mostrar("No se pudo registrar la orden. No se cobro.");
        } else {
            vista.mostrar("Cotizacion terminada. No se registro ningun cobro.");
        }
    }

    private static void registrarCuadrilla() {
        int codigo = vista.leerEnteroPositivo("Codigo de la cuadrilla: ");
        if (registro.buscarCuadrilla(codigo) != null) {
            vista.mostrar("Ese codigo ya pertenece a otra cuadrilla.");
            return;
        }
        float capacidad = vista.leerFloatPositivo("Capacidad de horas por jornada: ");
        boolean certificada = vista.leerSiNo("Certificada en refrigeracion? (s/n): ");
        if (registro.registrarCuadrilla(new Cuadrilla(codigo, capacidad, certificada))) {
            vista.mostrar("Cuadrilla registrada.");
        } else {
            vista.mostrar("No se pudo registrar la cuadrilla.");
        }
    }

    private static void crearJornada() {
        consultarCuadrillas();
        Cuadrilla cuadrilla = registro.buscarCuadrilla(vista.leerEnteroPositivo("Codigo de la cuadrilla: "));
        Jornada jornada = registro.crearJornada(cuadrilla);
        if (jornada == null) vista.mostrar("Cuadrilla inexistente u ocupada. No se creo la jornada.");
        else vista.mostrar("Jornada " + registro.getJornadas().size() + " creada en preparacion.");
    }

    private static Jornada seleccionarJornada() {
        consultarJornadas();
        int numero = vista.leerEnteroPositivo("Numero de jornada: ");
        if (numero > registro.getJornadas().size()) {
            vista.mostrar("La jornada no existe.");
            return null;
        }
        return registro.getJornadas().get(numero - 1);
    }

    private static void asignarOrden() {
        Jornada jornada = seleccionarJornada();
        if (jornada == null) return;
        consultarOrdenes();
        Orden orden = registro.buscarOrden(vista.leerEnteroPositivo("Codigo de la orden: "));
        if (jornada.asignarPendiente(orden)) vista.mostrar("Orden asignada.");
        else vista.mostrar("Asignacion rechazada: revise estado, capacidad y certificacion.");
    }

    private static void retirarOrden() {
        Jornada jornada = seleccionarJornada();
        if (jornada == null) return;
        Orden orden = registro.buscarOrden(vista.leerEnteroPositivo("Codigo de la orden: "));
        if (jornada.retirarOrden(orden)) vista.mostrar("Orden retirada y pendiente.");
        else vista.mostrar("No se puede retirar esa orden de esta jornada.");
    }

    private static void iniciarJornada() {
        Jornada jornada = seleccionarJornada();
        if (jornada == null) return;
        float antes = registro.ingresoTotal();
        if (jornada.iniciar(registro)) {
            vista.mostrar("Jornada iniciada. Cobro por visitas adicionales: "
                    + VistaConsola.dinero(registro.ingresoTotal() - antes));
        } else {
            vista.mostrar("No se puede iniciar esta jornada. Debe estar en preparacion y tener ordenes.");
        }
    }

    private static void registrarResultado() {
        Jornada jornada = seleccionarJornada();
        if (jornada == null) return;
        if (jornada.getEstado() != EstadoJornada.INICIADA) {
            vista.mostrar("La jornada no esta iniciada.");
            return;
        }
        vista.mostrar("Ordenes sin resultado:");
        for (Orden orden : jornada.getOrdenesSinResultado()) vista.mostrar(orden.toString());
        Orden orden = registro.buscarOrden(vista.leerEnteroPositivo("Codigo de la orden: "));
        int opcion = vista.leerOpcion("Resultado (1 completada, 2 visita fallida): ", 1, 2);
        if (jornada.registrarResultado(orden, opcion == 1)) {
            vista.mostrar("Resultado guardado. Estado de la orden: " + orden.getEstado());
            if (jornada.getEstado() == EstadoJornada.CERRADA) vista.mostrar("Jornada cerrada. Cuadrilla libre.");
        } else {
            vista.mostrar("No se puede registrar ese resultado para esta jornada.");
        }
    }

    private static void consultarOrdenes() {
        if (registro.getOrdenes().isEmpty()) vista.mostrar("No hay ordenes.");
        for (Orden orden : registro.getOrdenes()) vista.mostrar(orden.toString());
    }

    private static void consultarCuadrillas() {
        if (registro.getCuadrillas().isEmpty()) vista.mostrar("No hay cuadrillas.");
        for (Cuadrilla cuadrilla : registro.getCuadrillas()) vista.mostrar(cuadrilla.toString());
    }

    private static void consultarJornadas() {
        if (registro.getJornadas().isEmpty()) vista.mostrar("No hay jornadas.");
        for (int i = 0; i < registro.getJornadas().size(); i++) {
            vista.mostrar((i + 1) + ". " + registro.getJornadas().get(i));
        }
    }
}