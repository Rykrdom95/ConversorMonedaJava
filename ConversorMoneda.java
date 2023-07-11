import java.io.BufferedReader; // Librería para leer texto desde un flujo de entrada
import java.io.InputStreamReader; // Librería para leer caracteres desde un flujo de bytes
import java.net.HttpURLConnection; // Librería para realizar una conexión HTTP
import java.net.URL; // Librería para representar y manipular una URL
import java.util.Map; // Librería para trabajar con mapas (estructura de datos clave-valor)
import java.util.Scanner; // Librería para leer entrada del usuario
import java.util.HashMap; // Implementación de la interfaz Map que proporciona un almacenamiento eficiente en memoria

public class ConversorMoneda {
    public static void main(String[] args) {
        // Declaración de variables
        double cantidad;
        String monedaBase, monedaObjetivo;

        // Obtener la cantidad a convertir
        Scanner scan = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a convertir: ");
        cantidad = scan.nextDouble();

        // Obtener las monedas base y objetivo
        System.out.print("Ingrese la moneda base (ejemplo: USD): ");
        monedaBase = scan.next();
        System.out.print("Ingrese la moneda objetivo (ejemplo: EUR): ");
        monedaObjetivo = scan.next();

        // Cerrar el objeto Scanner
        scan.close();

        // Realizar la llamada a la API para obtener la tasa de cambio
        try {
            String apiUrl = "http://data.fixer.io/api/latest?access_key=01d32d63bc473e0ff039ab8bdbe1fcda";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = bufferedReader.readLine();
            bufferedReader.close();

            // Analizar la respuesta para obtener las tasas de cambio
            String ratesString = response.substring(response.indexOf("rates") + 8, response.indexOf("}"));
            Map<String, Double> ratesMap = parseRates(ratesString);

            // Realizar la conversión
            if (ratesMap.containsKey(monedaBase) && ratesMap.containsKey(monedaObjetivo)) {
                double tasaCambioBase = ratesMap.get(monedaBase);
                double tasaCambioObjetivo = ratesMap.get(monedaObjetivo);
                double resultado = (cantidad / tasaCambioBase) * tasaCambioObjetivo;

                // Mostrar el resultado
                System.out.println("El resultado de la conversión es: " + resultado);
            } else {
                System.out.println("No se encontró alguna de las monedas en las tasas de cambio.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Double> parseRates(String ratesString) {
        String[] rates = ratesString.split(",");
        Map<String, Double> ratesMap = new HashMap<>();

        for (String rate : rates) {
            String[] parts = rate.split(":");
            String currencyCode = parts[0].replaceAll("\"", "").trim();
            double rateValue = Double.parseDouble(parts[1]);
            ratesMap.put(currencyCode, rateValue);
        }

        return ratesMap;
    }
}
