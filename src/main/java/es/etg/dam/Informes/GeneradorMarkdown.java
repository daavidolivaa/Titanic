package es.etg.dam.Informes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GeneradorMarkdown implements GeneradorInformes {

    public static final String SALTO_LINEA = "\n";
    public static final String CABECERA_INFORME = "# SERVICIO DE EMERGENCIAS";
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String FORMATO_HORA = "HH:mm:ss";
    public static final String TEXTO_EJECUCION = "Ejecución realizada el dia ";
    public static final String TEXTO_A_LAS = " a las ";
    public static final String TITULO_BOTE = "## Bote ";
    public static final String TOTAL_SALVADOS = "- Total de salvados: ";
    public static final String TEXTO_TOTAL = "# Total ";
    public static final String MD = ".md";
    public static final String MENSAJE_ARCHIVO = "Archivo Markdown creado correctamente: ";
    public static final String MENSAJE_ERROR = "Error al crear el archivo Markdown";
    public static final String GUION = "-- ";
    public static final String ESPACIO = " ";

    @Override
    public String generarEstructura(HashMap<String, Integer[]> botes) {
        StringBuilder informe = new StringBuilder();
        informe.append(CABECERA_INFORME + SALTO_LINEA + SALTO_LINEA);
        LocalDate fecha = LocalDate.now();
        LocalTime hora = LocalTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern(FORMATO_FECHA);
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern(FORMATO_HORA);
        String fechaFormateada = fecha.format(formatoFecha);
        String horaFormateada = hora.format(formatoHora);

        informe.append(TEXTO_EJECUCION).append(fechaFormateada).append(TEXTO_A_LAS).append(horaFormateada)
                .append(SALTO_LINEA + SALTO_LINEA);

        Set<String> clave = botes.keySet();
        HashMap<String, Integer> totales = new HashMap<>();
        if (GeneradorInformes.totales) {
            for (String parameto : GeneradorInformes.parametrosMostrar) {
                totales.put(parameto, 0);
            }
        }

        for (String c : clave) {
            informe.append(TITULO_BOTE).append(c).append(SALTO_LINEA);
            if (GeneradorInformes.totales) {
                int total = botes.get(c)[0] + botes.get(c)[1] + botes.get(c)[2];
                informe.append(TOTAL_SALVADOS).append(total).append(SALTO_LINEA);
                for (int i = 0; i < GeneradorInformes.parametrosMostrar.length; i++) {
                    String key = GeneradorInformes.parametrosMostrar[i];
                    totales.put(key, totales.get(key) + botes.get(c)[i]);
                }

            }

            for (int i = 0; i < GeneradorInformes.parametrosMostrar.length; i++) {
                informe.append(GUION).append(GeneradorInformes.parametrosMostrar[i]).append(ESPACIO)
                        .append(botes.get(c)[i])
                        .append(SALTO_LINEA);
            }
            informe.append(SALTO_LINEA);
        }

        if (GeneradorInformes.totales) {
            int total = 0;
            for (String key : totales.keySet()) {
                total += totales.get(key);
            }
            if (GeneradorInformes.totales) {
                informe.append(TEXTO_TOTAL).append(total).append(SALTO_LINEA);
            }

            for (String key : GeneradorInformes.parametrosMostrar) {
                informe.append(GUION).append(key).append(ESPACIO).append(totales.get(key)).append(ESPACIO)
                        .append(SALTO_LINEA);
            }
        }
        return informe.toString();

    }

    @Override
    public boolean crearFichero(String contenido) {
        Path filePath = Paths.get(GeneradorInformes.ruta + MD);
        boolean creadoCorrectamente = true;
        try {
            Files.writeString(filePath, contenido);
            System.out.println(MENSAJE_ARCHIVO + filePath.toAbsolutePath());
        } catch (IOException e) {
            creadoCorrectamente = false;
        }

        return creadoCorrectamente;
    }

}
