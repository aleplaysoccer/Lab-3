public abstract class Orden {
    private final int codigo;
    private final String nombreCliente;
    private final String direccion;
    private final float horasTrabajo;
    private final boolean zonaMetropolitana;
    private EstadoOrden estado;
    private int visitas;

    protected Orden(int codigo, String nombreCliente, String direccion,
                    float horasTrabajo, boolean zonaMetropolitana) {
        if (codigo <= 0 || nombreCliente == null || nombreCliente.trim().isEmpty()
                || direccion == null || direccion.trim().isEmpty()
                || !Float.isFinite(horasTrabajo) || horasTrabajo <= 0) {
            throw new IllegalArgumentException("Los datos de la orden no son validos.");
        }
        this.codigo = codigo;
        this.nombreCliente = nombreCliente.trim();
        this.direccion = direccion.trim();
        this.horasTrabajo = horasTrabajo;
        this.zonaMetropolitana = zonaMetropolitana;
        this.estado = EstadoOrden.PENDIENTE;
        this.visitas = 0;
    }

    public abstract int calcularTiempoEstimado();
    public abstract float calcularCosto();
    public abstract int maximoVisitas();
    public abstract String getCategoria();

    public boolean compatibleCon(Cuadrilla cuadrilla) {
        return cuadrilla != null;
    }

    public boolean asignar() {
        if (estado != EstadoOrden.PENDIENTE || visitas >= maximoVisitas()) return false;
        estado = EstadoOrden.ASIGNADA;
        return true;
    }

    public boolean retirar() {
        if (estado != EstadoOrden.ASIGNADA) return false;
        estado = EstadoOrden.PENDIENTE;
        return true;
    }

    public boolean iniciarAtencion() {
        if (estado != EstadoOrden.ASIGNADA || visitas >= maximoVisitas()) return false;
        visitas++;
        estado = EstadoOrden.EN_ATENCION;
        return true;
    }

    public boolean registrarResultado(boolean completada) {
        if (estado != EstadoOrden.EN_ATENCION) return false;
        if (completada) estado = EstadoOrden.COMPLETADA;
        else if (visitas >= maximoVisitas()) estado = EstadoOrden.CANCELADA;
        else estado = EstadoOrden.PENDIENTE;
        return true;
    }

    public int getCodigo() { return codigo; }
    public int getVisitas() { return visitas; }
    public EstadoOrden getEstado() { return estado; }
    protected float getHorasTrabajo() { return horasTrabajo; }
    protected boolean esZonaMetropolitana() { return zonaMetropolitana; }

    @Override
    public String toString() {
        return codigo + " | " + getCategoria() + " | " + nombreCliente + " | " + direccion
                + " | " + (zonaMetropolitana ? "Metropolitana" : "Periferica")
                + " | " + horasTrabajo + " h | " + estado + " | visitas: " + visitas;
    }
}