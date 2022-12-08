package vista;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsolaLogger {

    public ConsolaLogger() {
        System.out.println(getTimeStamp()
                .concat("Inicializando consola..."));
    }

    private String getTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss");
        return String.format("[%s] ", LocalDateTime.now().format(formatter));
    }

    public void mostrarArchivoSeElimino(String name) {
        System.out.println(
                getTimeStamp()
                    .concat("Se eliminó el archivo ")
                    .concat("'")
                    .concat(name)
                    .concat("'"));
    }

    public void mostrarArchivoSeCreo(String name) {
        System.out.println(
                getTimeStamp()
                        .concat("Se creo el archivo ")
                        .concat("'")
                        .concat(name)
                        .concat("'"));
    }
}
