import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        new Cliente().iniciar();
    }

    private void iniciar() {
        try (Socket socket = new Socket("localhost", 7287);) {
            System.out.println("Conexión establecida con el servidor");
            System.out.println("Cerrando conexión...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
