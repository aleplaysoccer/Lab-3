import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jornada {
    private final Cuadrilla cuadrilla;
    private final List<Orden> ordenesAsignadas = new ArrayList<>();
    private final List<Orden> resultadosRegistrados = new ArrayList<>();
    private EstadoJornada estado = EstadoJornada.EN_PREPARACION;
    private float horasAsignadas;

    public Jornada(Cuadrilla cuadrilla) {
        if (cuadrilla == null) throw new IllegalArgumentException("La cuadrilla es obligatoria.");
        this.cuadrilla = cuadrilla;
    }

    public boolean asignarPendiente(Orden orden) {
        if (estado != EstadoJornada.EN_PREPARACION || orden == null
                || orden.getEstado() != EstadoOrden.PENDIENTE
                || ordenesAsignadas.contains(orden) || !orden.compatibleCon(cuadrilla)
                || horasAsignadas + orden.getHorasTrabajo() > cuadrilla.getMaximoHorasJornada() + 0.00001f
                || !orden.asignar()) return false;
        ordenesAsignadas.add(orden);
        horasAsignadas += orden.getHorasTrabajo();
        return true;
    }

    public boolean retirarOrden(Orden orden) {
        if (estado != EstadoJornada.EN_PREPARACION || orden == null
                || !ordenesAsignadas.contains(orden) || !orden.retirar()) return false;
        ordenesAsignadas.remove(orden);
        horasAsignadas -= orden.getHorasTrabajo();
        return true;
    }

    public boolean iniciar(Registro registro) {
        if (estado != EstadoJornada.EN_PREPARACION || ordenesAsignadas.isEmpty() || registro == null) {
            return false;
        }
        for (Orden orden : ordenesAsignadas) {
            if (orden.getEstado() != EstadoOrden.ASIGNADA
                    || orden.getVisitas() >= orden.maximoVisitas()) return false;
        }
        for (Orden orden : ordenesAsignadas) {
            orden.iniciarAtencion();
            if (orden.getVisitas() > 1) registro.registrarCargoVisita(orden);
        }
        estado = EstadoJornada.INICIADA;
        return true;
    }

    public boolean registrarResultado(Orden orden, boolean completada) {
        if (estado != EstadoJornada.INICIADA || orden == null
                || !ordenesAsignadas.contains(orden) || resultadosRegistrados.contains(orden)
                || !orden.registrarResultado(completada)) return false;
        resultadosRegistrados.add(orden);
        if (resultadosRegistrados.size() == ordenesAsignadas.size()) {
            estado = EstadoJornada.CERRADA;
            cuadrilla.liberar();
        }
        return true;
    }

    public float capacidadDisponible() { return cuadrilla.getMaximoHorasJornada() - horasAsignadas; }
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public float getHorasAsignadas() { return horasAsignadas; }
    public EstadoJornada getEstado() { return estado; }

    public List<Orden> getOrdenesSinResultado() {
        List<Orden> pendientes = new ArrayList<>();
        for (Orden orden : ordenesAsignadas) {
            if (!resultadosRegistrados.contains(orden)) pendientes.add(orden);
        }
        return Collections.unmodifiableList(pendientes);
    }

    @Override
    public String toString() {
        return "Cuadrilla " + cuadrilla.getCodigo() + " | " + estado
                + " | ordenes: " + ordenesAsignadas.size() + " | horas: " + horasAsignadas
                + " | disponibles: " + capacidadDisponible();
    }
}