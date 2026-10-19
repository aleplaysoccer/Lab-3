import java.util.Locale;
import java.util.Scanner;

public class VistaConsola {
    private final Scanner scanner;

    public VistaConsola() {
        scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        mostrar("\nTecniHogar - Laboratorio 3");
        mostrar("1. Cotizar y registrar orden");
        mostrar("2. Registrar cuadrilla");
        mostrar("3. Crear jornada");
        mostrar("4. Asignar orden");
        mostrar("5. Retirar orden");
        mostrar("6. Iniciar jornada");
        mostrar("7. Registrar resultado de visita");
        mostrar("8. Consultar ordenes");
        mostrar("9. Consultar cuadrillas");
        mostrar("10. Consultar jornadas");
        mostrar("11. Reportes");
        mostrar("0. Salir");
    }

    public void mostrar(String mensaje) {
        System.out.println(mensaje);
    }

    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) return valor;
            mostrar("Este dato no puede estar vacio.");
        }
    }

    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException ex) {
                mostrar("Ingrese un numero entero valido.");
            }
        }
    }

    public int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor > 0) return valor;
            mostrar("El numero debe ser positivo.");
        }
    }

    public float leerFloatPositivo(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje).replace(',', '.');
            try {
                float numero = Float.parseFloat(valor);
                if (Float.isFinite(numero) && numero > 0) return numero;
            } catch (NumberFormatException ex) {
                mostrar("Ingrese un numero valido.");
                continue;
            }
            mostrar("El numero debe ser positivo y estar dentro del rango permitido.");
        }
    }

    public int leerOpcion(String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= minimo && valor <= maximo) return valor;
            mostrar("Elija una opcion entre " + minimo + " y " + maximo + ".");
        }
    }

    public boolean leerSiNo(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje).toLowerCase(Locale.ROOT);
            if (valor.equals("s") || valor.equals("si")) return true;
            if (valor.equals("n") || valor.equals("no")) return false;
            mostrar("Responda s o n.");
        }
    }

    public static String dinero(float monto) {
        return String.format(Locale.US, "Q%.2f", monto);
    }
}