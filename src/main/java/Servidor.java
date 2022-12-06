import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        new Servidor().iniciar();
    }

    private void iniciar() {
        try {
            ServerSocket ss = new ServerSocket(7287);
            System.out.println("Esperando cliente...");
            Socket cliente = ss.accept();
            System.out.println("Conexión establecida con el cliente");
            System.out.println("Cerrando conexión...");
            cliente.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
