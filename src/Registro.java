import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Registro {
    private final List<Orden> ordenes = new ArrayList<>();
    private final List<Cuadrilla> cuadrillas = new ArrayList<>();
    private final List<Jornada> jornadas = new ArrayList<>();
    private final Map<Integer, Float> ingresosRegistro = new HashMap<>();
    private final Map<Integer, Float> ingresosVisitas = new HashMap<>();

    public boolean registrarOrden(Orden orden) {
        if (orden == null || buscarOrden(orden.getCodigo()) != null
                || orden.getEstado() != EstadoOrden.PENDIENTE) return false;
        float costo = orden.calcularCosto();
        if (!Float.isFinite(costo) || costo <= 0) return false;
        ordenes.add(orden);
        ingresosRegistro.put(orden.getCodigo(), costo);
        return true;
    }

    public boolean registrarCuadrilla(Cuadrilla cuadrilla) {
        if (cuadrilla == null || buscarCuadrilla(cuadrilla.getCodigo()) != null) return false;
        cuadrillas.add(cuadrilla);
        return true;
    }

    public void agregarInicial(Orden orden) {
        if (orden == null || buscarOrden(orden.getCodigo()) != null) {
            throw new IllegalArgumentException("Codigo de orden inicial repetido.");
        }
        ordenes.add(orden);
    }

    public Jornada crearJornada(Cuadrilla cuadrilla) {
        if (cuadrilla == null || !cuadrillas.contains(cuadrilla) || !cuadrilla.estaLibre()) return null;
        Jornada jornada = new Jornada(cuadrilla);
        if (!cuadrilla.asignarJornada(jornada)) return null;
        jornadas.add(jornada);
        return jornada;
    }

    public void registrarCargoVisita(Orden orden) {
        if (orden != null && ordenes.contains(orden) && orden.getVisitas() > 1) {
            ingresosVisitas.merge(orden.getCodigo(), 40f, Float::sum);
        }
    }

    public float ingresoTotal() {
        float total = 0;
        for (float monto : ingresosRegistro.values()) total += monto;
        for (float monto : ingresosVisitas.values()) total += monto;
        return total;
    }

    public Orden buscarOrden(int codigo) {
        for (Orden orden : ordenes) if (orden.getCodigo() == codigo) return orden;
        return null;
    }

    public Cuadrilla buscarCuadrilla(int codigo) {
        for (Cuadrilla cuadrilla : cuadrillas) if (cuadrilla.getCodigo() == codigo) return cuadrilla;
        return null;
    }

    public List<Orden> getOrdenes() { return Collections.unmodifiableList(ordenes); }
    public List<Cuadrilla> getCuadrillas() { return Collections.unmodifiableList(cuadrillas); }
    public List<Jornada> getJornadas() { return Collections.unmodifiableList(jornadas); }

    public void mostrarReportes() {
        Map<String, EnumMap<EstadoOrden, Integer>> conteos = new LinkedHashMap<>();
        Map<String, Float> porCategoria = new LinkedHashMap<>();
        for (Orden orden : ordenes) {
            String categoria = orden.getCategoria();
            conteos.computeIfAbsent(categoria, k -> new EnumMap<>(EstadoOrden.class))
                    .merge(orden.getEstado(), 1, Integer::sum);
            float cobrado = ingresosRegistro.getOrDefault(orden.getCodigo(), 0f)
                    + ingresosVisitas.getOrDefault(orden.getCodigo(), 0f);
            porCategoria.merge(categoria, cobrado, Float::sum);
        }
        System.out.println("Ordenes por categoria y estado:");
        for (Map.Entry<String, EnumMap<EstadoOrden, Integer>> fila : conteos.entrySet()) {
            System.out.println(fila.getKey() + ":");
            for (EstadoOrden estado : EstadoOrden.values()) {
                System.out.println("  " + estado + ": " + fila.getValue().getOrDefault(estado, 0));
            }
        }
        float porRegistro = 0;
        float porVisitas = 0;
        for (float monto : ingresosRegistro.values()) porRegistro += monto;
        for (float monto : ingresosVisitas.values()) porVisitas += monto;
        System.out.println("Ingreso por registros: " + VistaConsola.dinero(porRegistro));
        System.out.println("Ingreso por visitas adicionales: " + VistaConsola.dinero(porVisitas));
        System.out.println("Ingreso total: " + VistaConsola.dinero(ingresoTotal()));
        for (Map.Entry<String, Float> fila : porCategoria.entrySet()) {
            System.out.println(fila.getKey() + ": " + VistaConsola.dinero(fila.getValue()));
        }
        System.out.println("Jornadas sin cerrar:");
        boolean hayAbiertas = false;
        for (int i = 0; i < jornadas.size(); i++) {
            if (jornadas.get(i).getEstado() != EstadoJornada.CERRADA) {
                System.out.println((i + 1) + ". " + jornadas.get(i));
                hayAbiertas = true;
            }
        }
        if (!hayAbiertas) System.out.println("Ninguna.");
    }
}