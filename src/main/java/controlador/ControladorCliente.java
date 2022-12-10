package controlador;

import handler.ServidorHandler;
import modelo.Comando;
import vista.ArchivosTableModel;
import vista.PanelCliente;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ControladorCliente {

    private JFrame marco;
    private PanelCliente panel;
    private List<String> archivos;
    private final ArchivosTableModel modeloDeTabla;
    private final ServidorHandler servidor;
    private static final String separadorArchivos = ";";

    public ControladorCliente() {
        archivos = new ArrayList<>();
        modeloDeTabla = new ArchivosTableModel();
        configurarVista();
        servidor = new ServidorHandler();
    }

    private void configurarVista() {
        panel = new PanelCliente(this);
        marco = new JFrame();
        marco.setContentPane(panel);
        marco.setTitle("Interfaz De Cliente");
        marco.setBounds(0, 0, 565, 480);
        marco.setDefaultCloseOperation(EXIT_ON_CLOSE);
        marco.setLocationRelativeTo(null);
        marco.setResizable(false);
    }

    public void iniciar() {
        marco.setVisible(true);
        panel.setHostname("localhost");
    }

    public void salidaFueSolicitada() {
        marco.dispose();
    }

    public TableModel getModeloDeTabla() {
        return modeloDeTabla;
    }

    public void eliminarArchivoFueSolicitado(int fila) {
        String nombre = archivos.get(fila);
        try {
            servidor.solicitarEliminarArchivo(nombre);
            analizarRespuesta();
            refrescarArchivos();
        } catch (IOException e) {
            mostrarErrorAUsuario("Ocurrió un error al borrar el archivo. " + e.getMessage());
        }
    }

    public void duplicarArchivoFueSolicitado(int fila) {
        String nombre = archivos.get(fila);
        String nombreIngresado = pedirNombreDeArchivoAUsuario();
        try {
            String nombreDeDuplicado = nombreIngresado  + "." + obtenerExtensionDeArchivo(nombre);
            servidor.solicitarDuplicarArchivo(nombre, nombreDeDuplicado);
            analizarRespuesta();
            refrescarArchivos();
        } catch (IOException e) {
            mostrarErrorAUsuario("Ocurrió un error al intentar crear el archivo. " + e.getMessage());
        }
    }

    private void analizarRespuesta() throws IOException {
        String respuesta = servidor.getRespuesta().getAtributos().get("respuesta");
        if (!respuesta.equals("OK")) {
            String mensaje = servidor.getRespuesta().getAtributos().get("mensaje");
            throw new IOException(mensaje);
        }
    }

    private String obtenerExtensionDeArchivo(String nombre) {
        String[] nombreArray = nombre.split("\\.");
        if (nombreArray.length > 1) {
            return nombreArray[nombreArray.length - 1];
        }
        return "";
    }

    public void mostrarErrorAUsuario(String mensaje) {
        JOptionPane.showMessageDialog(marco,
                mensaje,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
    }

    private String pedirNombreDeArchivoAUsuario() {
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        return JOptionPane.showInputDialog(marco,
                "Ingrese el nombre del nuevo archivo.",
                "Duplicar archivo",
                JOptionPane.PLAIN_MESSAGE);
    }

    public void conectarFueSolicitada() {
        servidor.setHostname(panel.getHostname());
        RefrescadorDeLista refrescador = new RefrescadorDeLista(this);
        refrescador.start();
        habilitarConectar(false);
    }

    public void refrescarArchivos() throws IOException {
        servidor.solicitarNombresDeArchivos();
        parsearListaDeArchivos(servidor.getRespuesta());
        modeloDeTabla.setArchivos(archivos);
    }

    private void parsearListaDeArchivos(Comando comando) {
        archivos = new ArrayList<>();
        String lista = comando.getAtributos().get("lista");
        archivos.addAll(Arrays.asList(lista.split(separadorArchivos)));
    }

    public void habilitarConectar(boolean b) {
        panel.habilitarConectar(b);
    }
}

class RefrescadorDeLista extends Thread {

    private ControladorCliente cliente;

    public RefrescadorDeLista(ControladorCliente cliente) {
        this.cliente = cliente;
    }

    public void run() {
        try {
            while (true) {
                cliente.refrescarArchivos();
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            cliente.mostrarErrorAUsuario("Ocurrió un error al intentar conectarse al servidor.");
            cliente.habilitarConectar(true);
        }
    }
}
