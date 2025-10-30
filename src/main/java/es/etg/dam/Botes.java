package es.etg.dam;

public class Botes {
    private static String nombre;
    private static int cantidad;
    private static Pasajeros[] pasajeros;
    private static ServicioEmergencia servicioEmergencia;

    public static final String NUM_BOTE = "BXX";

    public static void main(String[] args) {

        if (args.length > 0)
            nombre = args[0];
        else
            nombre = NUM_BOTE;

        cantidad = (int) (Math.random() * 100) + 1;
        pasajeros = new Pasajeros[cantidad];

        for (int i = 0; i < cantidad; i++) {
            pasajeros[i] = new Pasajeros();
        }

        int tiempo = (int) (Math.random() * 5) + 2;
        try {
            Thread.sleep(tiempo * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        StringBuilder output = new StringBuilder();

        output.append(nombre);

        for (Pasajeros p : pasajeros)
            output.append(p.getGenero());

        System.out.println(output.toString());
    }

}
