public class MantenimientoAireAcondicionado extends Orden {
    private static float tarifaBase = Float.NaN;
    private final int capacidadEquipo;

    public MantenimientoAireAcondicionado(int codigo, String nombreCliente, String direccion,
                                         float horasTrabajo, boolean zonaMetropolitana,
                                         int capacidadEquipo) {
        super(codigo, nombreCliente, direccion, horasTrabajo, zonaMetropolitana);
        if (!zonaMetropolitana) {
            throw new IllegalArgumentException("El mantenimiento de aire solo se acepta en zona metropolitana.");
        }
        if (capacidadEquipo < 9000 || capacidadEquipo > 60000) {
            throw new IllegalArgumentException("La capacidad debe estar entre 9000 y 60000 BTU.");
        }
        this.capacidadEquipo = capacidadEquipo;
    }

    public static void setTarifaBase(float nuevaTarifa) {
        if (!Float.isFinite(nuevaTarifa) || nuevaTarifa <= 0) {
            throw new IllegalArgumentException("La tarifa base debe ser positiva.");
        }
        tarifaBase = nuevaTarifa;
    }

    @Override
    public float calcularCosto() {
        if (!Float.isFinite(tarifaBase)) {
            throw new IllegalStateException("Primero debe ingresar la tarifa base del mantenimiento.");
        }
        return tarifaBase + 110f * getHorasTrabajo() + 80f;
    }

    @Override public int calcularTiempoEstimado() { return 2; }
    @Override public int maximoVisitas() { return 1; }
    @Override public String getCategoria() { return "Mantenimiento de aire acondicionado"; }

    @Override
    public boolean compatibleCon(Cuadrilla cuadrilla) {
        return cuadrilla != null && cuadrilla.esCertificada();
    }
}