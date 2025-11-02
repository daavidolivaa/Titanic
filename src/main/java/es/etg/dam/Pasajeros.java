package es.etg.dam;

import lombok.Getter;

@Getter
public class Pasajeros {
    private char genero;

    public Pasajeros() {
        int n = (int) (Math.random() * 3);
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
