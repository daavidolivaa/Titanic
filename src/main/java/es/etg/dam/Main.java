package es.etg.dam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static final String JAVA = "java";
    private static final String CP = "-cp";
    private static final String VALUE = "target/classes";
    private static final String CLASS = "es.etg.dam.ServicioEmergencia";

    private static final String OUTPUT = "[ServicioEmergencias] ";
    private static final String ERROR = "[ServicioEmergencias-ERROR] ";
    private static final String EXIT = "Proceso ServicioEmergencias finalizado con código: ";

    public static void main(String[] args) {
        try {
            Process servicio = Runtime.getRuntime().exec(
                    new String[] { JAVA, CP, VALUE, CLASS });

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(servicio.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(OUTPUT + line);
                }
            }

            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(servicio.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(ERROR + line);
                }
            }

            int exitCode = servicio.waitFor();
            System.out.println(EXIT + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}