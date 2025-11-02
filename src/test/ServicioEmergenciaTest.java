package es.etg.dam;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

class ServicioEmergenciaTest {

    @Test
    void testServicioEmergenciasRecibeDatos() {
        HashMap<String, Integer[]> salvavidas = new HashMap<>();

        String salidaBote1 = "B01hhmmpp";
        String salidaBote2 = "B02hmph";

        for (String line : new String[] { salidaBote1, salidaBote2 }) {
            String nombre = line.substring(0, 3);
            String pasajerosStr = line.substring(3);
            int[] cuenta = { 0, 0, 0 };
            for (char c : pasajerosStr.toCharArray()) {
                switch (c) {
                    case 'm' -> cuenta[0]++;
                    case 'h' -> cuenta[1]++;
                    case 'p' -> cuenta[2]++;
                }
            }
            salvavidas.put(nombre, new Integer[] { cuenta[0], cuenta[1], cuenta[2] });
        }

        assertArrayEquals(new Integer[] { 2, 2, 2 }, salvavidas.get("B01"));
        assertArrayEquals(new Integer[] { 1, 2, 1 }, salvavidas.get("B02"));
    }
}
