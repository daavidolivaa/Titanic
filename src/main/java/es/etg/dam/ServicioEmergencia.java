package es.etg.dam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ServicioEmergencia {

    public static final String COMANDO = "java -cp target/classes es.etg.dam.Botes ";
    public static final String FORMATO = "B%02d";
    public static final String PREFIJO = "B";
    public static final char HOMBRE = 'H';
    public static final char MUJER = 'M';
    public static final char NINO = 'N';
    public static final String SALTO_LINEA = "\n";
    public static final String CABECERA_INFORME = "# SERVICIO DE EMERGENCIAS";
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String FORMATO_HORA = "HH:mm:ss";
    public static final String TEXTO_EJECUCION = "Ejecución realizada el dia ";
    public static final String TEXTO_A_LAS = " a las ";
    public static final String TITULO_BOTE = "## Bote ";
    public static final String TOTAL_SALVADOS = "- Total de salvados: ";
    public static final String TEXTO_HOMBRES = "-- Hombres ";
    public static final String TEXTO_MUJERES = "-- Mujeres ";
    public static final String TEXTO_NINOS = "-- Niños ";
    public static final String TEXTO_TOTAL = "# Total ";
    public static final String ARCHIVO_INFORME = "src/main/resources/InformeTitanic.md";
    public static final String MENSAJE_ARCHIVO = "Archivo Markdown creado correctamente: ";
    public static final String MENSAJE_ERROR = "Error al crear el archivo Markdown";

    public static void main(String[] args) {
        HashMap<String, Integer[]> botes = new HashMap<>();
        List<Process> procesos = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            String nombre = String.format(FORMATO, i + 1);
            try {
                Process p = Runtime.getRuntime().exec(COMANDO + nombre);
                procesos.add(p);

            } catch (Exception e) {
            }

        }
        for (Process pp : procesos) {
            int[] personas = { 0, 0, 0 };
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(pp.getInputStream()))) {
                String linea;
                String nombre = "";
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith(PREFIJO)) {
                        nombre = linea.substring(0, 3);
                        linea = linea.substring(3);
                    }
                    for (char c : linea.toCharArray()) {
                        switch (c) {
                            case HOMBRE:
                                personas[0] = personas[0] + 1;
                                break;
                            case MUJER:
                                personas[1] = personas[1] + 1;
                                break;
                            case NINO:
                                personas[2] = personas[2] + 1;
                                break;
                        }
                    }

                }

                pp.waitFor();
                botes.put(nombre, new Integer[] { personas[0], personas[1], personas[2] });
            } catch (Exception e) {
            }
        }
        generarInforme(botes);
    }

    private static void generarInforme(HashMap<String, Integer[]> botes) {
        HashMap<String, Integer[]> ordenado = ordenar(botes);

        StringBuilder informe = new StringBuilder();

        informe.append(CABECERA_INFORME + SALTO_LINEA + SALTO_LINEA);

        LocalDate fecha = LocalDate.now();
        LocalTime hora = LocalTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern(FORMATO_FECHA);
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern(FORMATO_HORA);
        String fechaFormateada = fecha.format(formatoFecha);
        String horaFormateada = hora.format(formatoHora);

        informe.append(TEXTO_EJECUCION).append(fechaFormateada).append(TEXTO_A_LAS)
                .append(horaFormateada).append(SALTO_LINEA + SALTO_LINEA);

        Set<String> clave = ordenado.keySet();
        int hombres = 0;
        int mujeres = 0;
        int niños = 0;

        for (String c : clave) {
            informe.append(TITULO_BOTE).append(c).append(SALTO_LINEA + SALTO_LINEA);
            int total = ordenado.get(c)[0] + ordenado.get(c)[1] + ordenado.get(c)[2];
            informe.append(TOTAL_SALVADOS).append(total).append(SALTO_LINEA);
            hombres += ordenado.get(c)[0];
            mujeres += ordenado.get(c)[1];
            niños += ordenado.get(c)[2];
            informe.append(TEXTO_HOMBRES).append(ordenado.get(c)[0]).append(SALTO_LINEA);
            informe.append(TEXTO_MUJERES).append(ordenado.get(c)[1]).append(SALTO_LINEA);
            informe.append(TEXTO_NINOS).append(ordenado.get(c)[2]).append(SALTO_LINEA + SALTO_LINEA);
        }

        informe.append(TEXTO_TOTAL).append(niños + hombres + mujeres).append(SALTO_LINEA);
        informe.append(TEXTO_HOMBRES).append(hombres).append(SALTO_LINEA);
        informe.append(TEXTO_MUJERES).append(mujeres).append(SALTO_LINEA);
        informe.append(TEXTO_NINOS).append(niños).append(SALTO_LINEA + SALTO_LINEA);

        Path filePath = Paths.get(ARCHIVO_INFORME);

        try {
            Files.writeString(filePath, informe);
            System.out.println(MENSAJE_ARCHIVO + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println(MENSAJE_ERROR);
            e.printStackTrace();
        }
    }

    private static HashMap<String, Integer[]> ordenar(HashMap<String, Integer[]> salvavidas) {
        List<String> sortedKeys = new ArrayList<>(salvavidas.keySet());
        sortedKeys.sort((key1, key2) -> {
            int num1 = Integer.parseInt(key1.substring(1));
            int num2 = Integer.parseInt(key2.substring(1));
            return Integer.compare(num1, num2);
        });

        LinkedHashMap<String, Integer[]> sortedMap = new LinkedHashMap<>();
        for (String key : sortedKeys) {
            sortedMap.put(key, salvavidas.get(key));
        }
        return sortedMap;
    }
}