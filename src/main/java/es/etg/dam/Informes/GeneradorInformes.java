package es.etg.dam.Informes;

import java.util.HashMap;

public interface GeneradorInformes {

    public static final String HOMBRE = "Hombre";
    public static final String MUJER = "Mujer";
    public static final String NINO = "Niño";
    public static final String RUTA = "src/main/resources/Informe";

    String[] parametrosMostrar = { HOMBRE, MUJER, NINO };
    boolean totales = true;
    String ruta = RUTA;

    public String generarEstructura(HashMap<String, Integer[]> Botes);

    public boolean crearFichero(String contenido);
}
