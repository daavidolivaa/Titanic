package es.etg.dam;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BotesTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testBoteImprimePasajeros() {
        Botes bote = new Botes();
        bote.main(new String[] {});

        String output = outContent.toString().trim();
        assertTrue(output.startsWith("BXX"), "La salida debe comenzar con el nombre del bote");
        assertTrue(output.length() > 3, "Debe imprimir al menos un pasajero después del nombre");
    }

    @Test
    void testConteoDePasajeros() {
        Botes bote = new Botes();
        bote.main(new String[] {});

        String output = outContent.toString().trim();
        String pasajerosStr = output.substring(3); // asumimos BXX
        int pasajeros = pasajerosStr.length();
        int totalH = 0;
        int totalM = 0;
        int totalP = 0;
        for (char c : pasajerosStr.toCharArray()) {
            switch (c) {
                case 'h' -> totalH++;
                case 'm' -> totalM++;
                case 'n' -> totalP++;
            }
        }

        assertEquals(totalH + totalM + totalP, pasajeros);
    }
}
