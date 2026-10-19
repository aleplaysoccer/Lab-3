public class InstalacionElectricaCertificada extends Instalacion {
    private final float valorMateriales;

    public InstalacionElectricaCertificada(int codigo, String nombreCliente, String direccion,
                                          float horasTrabajo, boolean zonaMetropolitana,
                                          int puntosInstalacion, float valorMateriales) {
        super(codigo, nombreCliente, direccion, horasTrabajo, zonaMetropolitana, puntosInstalacion);
        if (!Float.isFinite(valorMateriales) || valorMateriales <= 0) {
            throw new IllegalArgumentException("El valor de materiales debe ser positivo.");
        }
        this.valorMateriales = valorMateriales;
    }

    @Override public float calcularCosto() { return super.calcularCosto() + 120f + valorMateriales * 0.05f; }
    @Override public int calcularTiempoEstimado() { return super.calcularTiempoEstimado() + 2; }
    @Override public int maximoVisitas() { return 2; }
    @Override public String getCategoria() { return "Instalacion electrica certificada"; }
}