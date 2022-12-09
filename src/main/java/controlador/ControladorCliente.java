package controlador;

import modelo.ServidorConector;
import vista.ArchivosTableModel;
import vista.Marco;
import vista.PanelMenu;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControladorCliente {

    private final PanelMenu menu;
    private Marco marco;
    private List<String> archivos;
    private static final String separadorArchivos = ";";
    private final ArchivosTableModel modeloDeTabla;
    private ServidorConector servidor;

    public ControladorCliente() {
        marco = new Marco();
        archivos = new ArrayList<>();
        modeloDeTabla = new ArchivosTableModel();
        menu = new PanelMenu(this);
        agregarMenu(menu);
        servidor = new ServidorConector();
    }

    public void iniciar() {
        marco.setVisible(true);
        menu.setHostname("localhost");
        mostrarMenuPrincipal();
    }

    public void mostrarMenuPrincipal() {
        marco.mostrarMenu();
    }

    public void agregarMenu(PanelMenu menu) {
        marco.agregarMenu(menu);
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
        servidor.setHostname(menu.getHostname());
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
        menu.habilitarConectar(b);
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
