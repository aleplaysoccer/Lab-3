public class Instalacion extends Orden {
    private final int puntosInstalacion;

    public Instalacion(int codigo, String nombreCliente, String direccion,
                       float horasTrabajo, boolean zonaMetropolitana, int puntosInstalacion) {
        super(codigo, nombreCliente, direccion, horasTrabajo, zonaMetropolitana);
        if (puntosInstalacion <= 0) {
            throw new IllegalArgumentException("Los puntos deben ser positivos.");
        }
        this.puntosInstalacion = puntosInstalacion;
    }

    @Override
    public float calcularCosto() {
        double horasFacturables = Math.ceil(Math.max(getHorasTrabajo(), puntosInstalacion * 0.75));
        return (esZonaMetropolitana() ? 100f : 175f) + (float) horasFacturables * 90f;
    }

    @Override public int calcularTiempoEstimado() { return esZonaMetropolitana() ? 3 : 5; }
    @Override public int maximoVisitas() { return 3; }
    @Override public String getCategoria() { return "Instalacion"; }
}