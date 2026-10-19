public class Diagnostico extends Orden {
    public Diagnostico(int codigo, String nombreCliente, String direccion,
                       float horasTrabajo, boolean zonaMetropolitana) {
        super(codigo, nombreCliente, direccion, horasTrabajo, zonaMetropolitana);
        if (horasTrabajo > 1.5f) {
            throw new IllegalArgumentException("Un diagnostico no puede superar 1.5 horas.");
        }
    }

    @Override public float calcularCosto() { return esZonaMetropolitana() ? 150f : 225f; }
    @Override public int calcularTiempoEstimado() { return esZonaMetropolitana() ? 1 : 2; }
    @Override public int maximoVisitas() { return 3; }
    @Override public String getCategoria() { return "Diagnostico"; }
}