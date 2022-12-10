package vista;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consola {

    public Consola() {
        System.out.println(getTimeStamp()
                .concat("Inicializando la consola del servidor. Esperando conexiones."));
    }

    public void mostrarSolicitud(String mensaje) {
        mostrar("Se solicitó "
                .concat("'")
                .concat("'"));
    }

    public void mostrarArchivoSeElimino(String name) {
        mostrar("Se eliminó el archivo "
                .concat("'")
                .concat(name)
                .concat("'"));
    }

    public void mostrarArchivoSeCreo(String name) {
        mostrar("Se creo el archivo "
                .concat("'")
                .concat(name)
                .concat("'"));
    }

    public void mostrarError(String mensaje) {
        mostrar("Hubo un error en la creación del archivo: "
                .concat(mensaje));
    }

    public void mostrarClienteSeConecto(String hostname, String hostaddress) {
        mostrar("Se estableció una conexión con un cliente. Con nombre de host "
                .concat(hostname)
                .concat(" y dirección ")
                .concat(hostaddress));
    }

    private String getTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss");
        return String.format("[%s] ", LocalDateTime.now().format(formatter));
    }

    private void mostrar(String mensaje) {
        System.out.println(
                getTimeStamp()
                        .concat(mensaje));
    }
}
