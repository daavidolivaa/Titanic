package es.etg.dam.Informes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class GeneradorHTML implements GeneradorInformes {

    public static final String COMENTARIO_HTML = "Futura implementacion html";
    public static final String HTML = ".html";
    public static final String MENSAJE_HTML = "Archivo HTML creado correctamente: ";

    @Override
    public String generarEstructura(HashMap<String, Integer[]> Botes) {
        StringBuilder informe = new StringBuilder();
        informe.append(COMENTARIO_HTML);

        return informe.toString();
    }

    @Override
    public boolean crearFichero(String contenido) {
        Path filePath = Paths.get(GeneradorInformes.ruta + HTML);
        boolean creadoCorrectamente = true;
        try {
            Files.writeString(filePath, contenido);
            System.out.println(MENSAJE_HTML + filePath.toAbsolutePath());
        } catch (IOException e) {
            creadoCorrectamente = false;
        }

        return creadoCorrectamente;
    }

}
