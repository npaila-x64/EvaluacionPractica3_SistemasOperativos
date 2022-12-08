package excepciones;

public class ComandoMalformadoException extends RuntimeException {

    private ComandoMalformadoException(String mensaje) {
        super(mensaje);
    }

    public static ComandoMalformadoException noSePudoParsearComando(String mensaje) {
        return new ComandoMalformadoException("No se pudo parsear el comando: " + mensaje);
    }
}
