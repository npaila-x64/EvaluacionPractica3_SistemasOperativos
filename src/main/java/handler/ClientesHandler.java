package handler;

import controlador.ControladorServidor;
import excepcion.ComandoMalformadoException;
import modelo.Comando;

import java.io.*;
import java.net.Socket;

public class ClientesHandler implements Runnable {

    private final ControladorServidor controlador;
    private PrintWriter out;
    private BufferedReader in;
    private Socket cliente;

    public ClientesHandler(ControladorServidor controlador, Socket cliente) {
        this.controlador = controlador;
        this.cliente = cliente;
        out = null;
        in = null;
    }

    @Override
    public void run() {
        try {
            procesarSolicitud();
        } catch (IOException | ComandoMalformadoException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
                cliente.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void procesarSolicitud() throws IOException, ComandoMalformadoException {
        while (true) {
            String solicitudRecibida = recibirSolicitud(cliente);
            if (solicitudRecibida == null) break;
            Comando comando = Comando.parsearSolicitud(solicitudRecibida);
            controlador.seSolicitoComando(comando, cliente.getInetAddress().getHostName());
            controlador.ejecutarComando(comando);
            enviarRespuesta(cliente, controlador.getRespuesta());
        }
    }

    private void enviarRespuesta(Socket cliente, String mensajeAEnviar) throws IOException {
        out = new PrintWriter(cliente.getOutputStream(), true);
        out.println(mensajeAEnviar);
    }

    private String recibirSolicitud(Socket cliente) throws IOException {
        in = new BufferedReader(
                new InputStreamReader(cliente.getInputStream()));
        return in.readLine();
    }
}
