package es.etg.dam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import es.etg.dam.Informes.GeneradorMarkdown;

public class ServicioEmergencia {

    public static final String COMANDO = "java -cp target/classes es.etg.dam.Botes ";
    public static final String FORMATO = "B%02d";
    public static final String PREFIJO = "B";
    public static final char HOMBRE = 'H';
    public static final char MUJER = 'M';
    public static final char NINO = 'N';
    public static final String MENSAJE_ARCHIVO = "Informe creado correctamente";
    public static final String MENSAJE_ERROR = "Error al generar el informe";

    public static void main(String[] args) {
        HashMap<String, Integer[]> botes = new HashMap<>();
        List<Process> procesos = new ArrayList<>();

        procesos = generarBotes(procesos);

        botes = leerDatos(procesos, botes);

        HashMap<String, Integer[]> botesOrdenados = ordenar(botes);

        GeneradorMarkdown informe = new GeneradorMarkdown();
        String contendio = informe.generarEstructura(botesOrdenados);
        boolean creado = informe.crearFichero(contendio);

        if (creado) {
            System.out.println(MENSAJE_ARCHIVO);
        } else {
            System.out.println(MENSAJE_ERROR);
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

    private static List<Process> generarBotes(List<Process> procesos) {
        for (int i = 0; i < 20; i++) {
            String nombre = String.format(FORMATO, i);
            try {
                Process p = Runtime.getRuntime().exec(COMANDO + nombre);
                procesos.add(p);

            } catch (Exception e) {
            }
        }

        return procesos;
    }

    private static HashMap<String, Integer[]> leerDatos(List<Process> procesos, HashMap<String, Integer[]> botes) {
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

        return botes;
    }

}
