package es.etg.dam;

import java.util.Random;

import lombok.Getter;

@Getter
public class Pasajeros {
    private static final Random random = new Random();
    private char genero;

    public Pasajeros() {
        int n = random.nextInt(3);
        switch (n) {
            case 0:
                genero = ServicioEmergencia.HOMBRE;
                break;
            case 1:
                genero = ServicioEmergencia.MUJER;
                break;
            case 2:
                genero = ServicioEmergencia.NINO;
                break;
        }
    }
}