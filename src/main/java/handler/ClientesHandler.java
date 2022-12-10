package handler;

import controlador.ControladorServidor;
import excepcion.ComandoMalformadoException;
import modelo.Comando;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientesHandler {

    private final int puerto = 7287;
    private boolean escuchando = true;
    private final ControladorServidor controlador;

    public ClientesHandler(ControladorServidor controlador) {
        this.controlador = controlador;
    }

    public void esperarSolicitudes() throws IOException, ComandoMalformadoException {
        try (ServerSocket ss = new ServerSocket(puerto)) {
            while (escuchando) {
                try (Socket cliente = ss.accept()) {
                    controlador.clienteSeConecto(
                            cliente.getInetAddress().getHostName(),
                            cliente.getInetAddress().getHostAddress());
                    String mensajeRecibido = recibirMensaje(cliente);
                    Comando comando = Comando.parsearComando(mensajeRecibido);
                    controlador.seSolicitoComando(comando, cliente.getInetAddress().getHostName());
                    controlador.ejecutarComando(comando);
                    enviarRespuesta(cliente, controlador.getRespuesta());
                }
            }
        }
    }

    private void enviarRespuesta(Socket cliente, String mensajeAEnviar) throws IOException {
        DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
        dos.writeUTF(mensajeAEnviar);
        dos.flush();
    }

    private String recibirMensaje(Socket cliente) throws IOException {
        DataInputStream dis = new DataInputStream(cliente.getInputStream());
        return dis.readUTF();
    }
}
