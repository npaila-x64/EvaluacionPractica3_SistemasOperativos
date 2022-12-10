package vista;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consola {

    public Consola() {
        System.out.println(getTimeStamp()
                .concat("Inicializando la consola del servidor. Esperando conexiones."));
    }

    public void mostrarSolicitud(String comando, String hostName) {
        mostrar("El host '"
                .concat(hostName)
                .concat("' solicitó el comando ")
                .concat(comando));
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
        mostrar("Se estableció una conexión con un cliente. Cuyo nombre de host es '"
                .concat(hostname)
                .concat("' y dirección IP ")
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
