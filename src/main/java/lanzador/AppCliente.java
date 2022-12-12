package lanzador;

import controlador.ControladorCliente;

public class AppCliente {

    public static boolean DEBUG = true;

    public static void main(String[] args) {
        new ControladorCliente().iniciar();
    }
}
