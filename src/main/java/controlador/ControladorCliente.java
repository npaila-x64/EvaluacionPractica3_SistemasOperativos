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
    private PanelCliente vista;
    private List<String> archivos;
    private final ArchivosTableModel modeloDeTabla;
    private final ServidorHandler servidor;
    private static final String separadorArchivos = ";";
    private boolean refrescandoListaDeArchivos;
    private final int puerto = 65304;

    public ControladorCliente() {
        archivos = new ArrayList<>();
        modeloDeTabla = new ArchivosTableModel();
        configurarVista();
        servidor = new ServidorHandler();
    }

    private void configurarVista() {
        vista = new PanelCliente(this);
        marco = new JFrame();
        marco.setContentPane(vista);
        marco.setTitle("Interfaz De Cliente");
        marco.setIconImage(new ImageIcon("src/img/logo.png").getImage());
        marco.setBounds(0, 0, 565, 480);
        marco.setDefaultCloseOperation(EXIT_ON_CLOSE);
        marco.setLocationRelativeTo(null);
        marco.setResizable(false);
        marco.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            cerrarAplicacionFueSolicitada();
            }
        });
    }

    public void iniciar() {
        marco.setVisible(true);
        vista.setHostname("localhost");
    }

    public void salidaFueSolicitada() {
        marco.dispose();
        cerrarAplicacionFueSolicitada();
    }

    public TableModel getModeloDeTabla() {
        return modeloDeTabla;
    }

    public void eliminarArchivoFueSolicitado(int fila) {
        String nombre = archivos.get(fila);
        try {
            servidor.solicitarEliminarArchivo(nombre);
            analizarRespuesta();
            refrescarListaDeArchivos();
        } catch (IOException e) {
            mostrarErrorAUsuario("Ocurrió un error al borrar el archivo. " + e.getMessage());
        }
    }

    public void duplicarArchivoFueSolicitado(int fila) {
        String nombre = archivos.get(fila);
        String nombreIngresado = pedirNombreDeArchivoAUsuario();
        if (nombreIngresado == null) return;
        try {
            String nombreDeDuplicado = nombreIngresado  + "." + obtenerExtensionDeArchivo(nombre);
            servidor.solicitarDuplicarArchivo(nombre, nombreDeDuplicado);
            analizarRespuesta();
            refrescarListaDeArchivos();
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

    public void conectarConServidorFueSolicitado() {
        try {
            servidor.crearSocket(vista.getServidorHostname(), puerto);
            iniciarAutoRefrescadorDeLista();
            habilitarComponentesInteractivosDeConexion(false);
        } catch (IOException e) {
            mostrarErrorAUsuario("Ocurrió un error al intentar conectar con el servidor. " + e.getMessage());
        }
     }

    private void iniciarAutoRefrescadorDeLista() {
        RefrescadorDeLista refrescador = new RefrescadorDeLista(this);
        refrescandoListaDeArchivos = true;
        refrescador.start();
    }

    public void refrescarListaDeArchivos() throws IOException {
        servidor.solicitarListaDeArchivos();
        parsearListaDeArchivos(servidor.getRespuesta());
        modeloDeTabla.setArchivos(archivos);
    }

    private void parsearListaDeArchivos(Comando comando) {
        archivos = new ArrayList<>();
        String lista = comando.getAtributos().get("lista");
        archivos.addAll(Arrays.asList(lista.split(separadorArchivos)));
    }

    public void habilitarComponentesInteractivosDeConexion(boolean b) {
        vista.habilitarConectar(b);
    }

    public boolean getRefrescandoListaDeArchivos() {
        return refrescandoListaDeArchivos;
    }

    private void cerrarAplicacionFueSolicitada() {
        refrescandoListaDeArchivos = false;
        try {
            servidor.cerrarConexion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void limpiarListaDeArchivos() {
        archivos = new ArrayList<>();
        modeloDeTabla.setArchivos(archivos);
    }
}

class RefrescadorDeLista extends Thread {

    private ControladorCliente cliente;

    public RefrescadorDeLista(ControladorCliente cliente) {
        this.cliente = cliente;
    }

    public void run() {
        try {
            while (cliente.getRefrescandoListaDeArchivos()) {
                cliente.refrescarListaDeArchivos();
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            cliente.mostrarErrorAUsuario("Ocurrió un error al intentar conectarse al servidor.");
            cliente.limpiarListaDeArchivos();
            cliente.habilitarComponentesInteractivosDeConexion(true);
        }
    }
}
