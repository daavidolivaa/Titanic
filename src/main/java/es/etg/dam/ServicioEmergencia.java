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

    public static void main(String[] args) {
        HashMap<String, Integer[]> botes = new HashMap<>();
        List<Process> procesos = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            String nombre = String.format("B%02d", i + 1);
            try {
                Process p = Runtime.getRuntime().exec(COMANDO + nombre);
                procesos.add(p);

            } catch (Exception e) {
            }

        }
        for (Process pp : procesos) {
            int[] personas = { 0, 0, 0 }; // 1º Hombre 2º Mujer 3º Niño
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(pp.getInputStream()))) {
                String linea;
                String nombre = "";
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith("B")) {
                        nombre = linea.substring(0, 3);
                        linea = linea.substring(3);
                    }
                    for (char c : linea.toCharArray()) {
                        switch (c) {
                            case 'H':
                                personas[0] = personas[0] + 1;
                                break;
                            case 'M':
                                personas[1] = personas[1] + 1;
                                break;
                            case 'N':
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

        informe.append("# SERVICIO DE EMERGENCIAS \n\n");

        LocalDate fecha = LocalDate.now();
        LocalTime hora = LocalTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        String fechaFormateada = fecha.format(formatoFecha);
        String horaFormateada = hora.format(formatoHora);

        informe.append("Ejecución realizada el dia ").append(fechaFormateada).append(" a las ")
                .append(horaFormateada).append("\n\n");

        Set<String> clave = ordenado.keySet();
        int hombres = 0;
        int mujeres = 0;
        int niños = 0;

        for (String c : clave) {
            informe.append("## Bote ").append(c).append("\n\n");
            int total = ordenado.get(c)[0] + ordenado.get(c)[1] + ordenado.get(c)[2];
            informe.append("- Total de salvados: ").append(total).append("\n");
            hombres += ordenado.get(c)[0];
            mujeres += ordenado.get(c)[1];
            niños += ordenado.get(c)[2];
            informe.append("-- Hombres ").append(ordenado.get(c)[0]).append("\n");
            informe.append("-- Mujeres ").append(ordenado.get(c)[1]).append("\n");
            informe.append("-- Niños ").append(ordenado.get(c)[2]).append("\n\n");
        }

        informe.append("# Total ").append(niños + hombres + mujeres).append("\n");
        informe.append("-- Hombres ").append(hombres).append("\n");
        informe.append("-- Mujeres ").append(mujeres).append("\n");
        informe.append("-- Niños ").append(niños).append("\n\n");

        Path filePath = Paths.get("InformeTitanic.md");

        try {
            Files.writeString(filePath, informe);
            System.out.println("Archivo Markdown creado correctamente: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al crear el archivo Markdown");
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