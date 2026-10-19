public class Cuadrilla {
    private final int codigo;
    private final float maximoHorasJornada;
    private final boolean certificada;
    private Jornada jornadaSinCerrar;

    public Cuadrilla(int codigo, float maximoHorasJornada, boolean certificada) {
        if (codigo <= 0 || !Float.isFinite(maximoHorasJornada) || maximoHorasJornada <= 0) {
            throw new IllegalArgumentException("El codigo y la capacidad deben ser positivos.");
        }
        this.codigo = codigo;
        this.maximoHorasJornada = maximoHorasJornada;
        this.certificada = certificada;
    }

    public boolean estaLibre() { return jornadaSinCerrar == null; }

    public boolean asignarJornada(Jornada jornada) {
        if (!estaLibre() || jornada == null) return false;
        jornadaSinCerrar = jornada;
        return true;
    }

    public void liberar() { jornadaSinCerrar = null; }
    public int getCodigo() { return codigo; }
    public float getMaximoHorasJornada() { return maximoHorasJornada; }
    public boolean esCertificada() { return certificada; }

    @Override
    public String toString() {
        return "Cuadrilla " + codigo + " | capacidad: " + maximoHorasJornada
                + " h | refrigeracion: " + (certificada ? "si" : "no")
                + " | " + (estaLibre() ? "libre" : "jornada sin cerrar");
    }
}