package controlador;

import modelo.ServidorHandler;
import vista.ArchivosTableModel;
import vista.Marco;
import vista.PanelCliente;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControladorCliente {

    private final PanelCliente panel;
    private Marco marco;
    private List<String> archivos;
    private static final String separadorArchivos = ";";
    private final ArchivosTableModel modeloDeTabla;
    private ServidorHandler servidor;

    public ControladorCliente() {
        marco = new Marco();
        archivos = new ArrayList<>();
        modeloDeTabla = new ArchivosTableModel();
        panel = new PanelCliente(this);
        agregarPanel(panel);
        servidor = new ServidorHandler();
    }

    public void iniciar() {
        marco.setVisible(true);
        panel.setHostname("localhost");
        mostrarPanelPrincipal();
    }

    public void mostrarPanelPrincipal() {
        marco.mostrarPanel();
    }

    public void agregarPanel(PanelCliente menu) {
        marco.agregarPanel(menu);
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
            refrescarArchivos();
        } catch (IOException e) {
            mostrarErrorAUsuario("Ocurrió un error al borrar el archivo.");
        }
    }

    public void duplicarArchivoFueSolicitado(int fila) {
        String nombre = archivos.get(fila);
        String nombreIngresado = pedirNombreDeArchivoAUsuario();
        try {
            String nombreDeDuplicado = nombreIngresado  + "." + obtenerExtensionDeArchivo(nombre);
            servidor.solicitarDuplicarArchivo(nombre, nombreDeDuplicado);
            refrescarArchivos();
        } catch (IOException e) {
            mostrarErrorAUsuario("Ocurrió un error al intentar crear el archivo.");
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

    private void parsearListaDeArchivos(String texto) {
        archivos = new ArrayList<>();
        archivos.addAll(Arrays.asList(texto.split(separadorArchivos)));
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
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            cliente.mostrarErrorAUsuario("Ocurrió un error al intentar conectarse al servidor.");
            cliente.habilitarConectar(true);
        }
    }
}
