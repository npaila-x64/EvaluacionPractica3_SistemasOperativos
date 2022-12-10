package handler;

import modelo.Comando;
import modelo.ComandoEnum;

import java.io.*;
import java.net.Socket;

public class ServidorHandler {

    private String hostname = "localhost";
    private final int puerto = 7287;
    private Comando respuesta;
    private Socket socket;

    public void crearSocket(String hostname) throws IOException {
        socket = new Socket(hostname, puerto);
    }

    private void realizarSolicitud(Comando comando) throws IOException {
        enviarComando(socket, comando);
        String mensajeRecibido = recibirMensaje(socket);
        if (mensajeRecibido != null) {
            respuesta = Comando.parsearSolicitud(mensajeRecibido);
        }
    }

    private String recibirMensaje(Socket socket) throws IOException {
        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        return in.readLine();
    }

    private void enviarComando(Socket socket, Comando comando) throws IOException{
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(comando.toString());
    }

    public void solicitarListaDeArchivos() throws IOException {
        Comando comando = new Comando();
        comando.setComandoEnum(ComandoEnum.VER_ARCHIVOS);
        realizarSolicitud(comando);
    }

    public void solicitarEliminarArchivo(String nombre) throws IOException {
        Comando comando = new Comando();
        comando.setComandoEnum(ComandoEnum.ELIMINAR);
        comando.setAtributo("nombre", nombre);
        realizarSolicitud(comando);
    }

    public void solicitarDuplicarArchivo(String nombre, String nombreIngresado) throws IOException {
        Comando comando = new Comando();
        comando.setComandoEnum(ComandoEnum.DUPLICAR);
        comando.setAtributo("nombre", nombre);
        comando.setAtributo("nombre_ingresado", nombreIngresado);
        realizarSolicitud(comando);
    }

    public Comando getRespuesta() {
        return respuesta;
    }

    public void cerrarConexion() throws IOException {
        socket.close();
    }
}
