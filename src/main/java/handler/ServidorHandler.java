package handler;

import modelo.Comando;
import modelo.ComandoEnum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedReader;
import java.net.Socket;

public class ServidorHandler {

    private String hostname = "localhost";
    private final int puerto = 7287;
    private Comando respuesta;
    private Socket socket;

    public ServidorHandler() {
        try {
            socket = new Socket(hostname, puerto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void realizarSolicitud(Comando comando) throws IOException {
        enviarComando(socket, comando);
        respuesta = Comando.parsearComando(recibirMensaje(socket));
    }

    private String recibirMensaje(Socket socket) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        return dis.readUTF();
    }

    private void enviarComando(Socket socket, Comando comando) throws IOException{
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(comando.toString());
    }

    public void solicitarNombresDeArchivos() throws IOException {
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

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void cerrarConexion() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
