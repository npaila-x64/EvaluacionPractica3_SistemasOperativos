package controlador;

import utils.AccesoADatos;
import vista.ArchivosTableModel;
import vista.ConsolaLogger;
import vista.PanelMenu;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;

public class ControladorMenu {

    private final PanelMenu vista;
    private final ConsolaLogger consola;
    private final ArchivosTableModel modeloDeTabla;
    private final ControladorAplicacion controlador;

    public ControladorMenu(ControladorAplicacion controlador) {
        this.controlador = controlador;
        modeloDeTabla = new ArchivosTableModel();
        vista = new PanelMenu(this);
        consola = new ConsolaLogger();
        this.controlador.agregarMenu(vista);
    }

    public void iniciar() {
        controlador.mostrarMenuPrincipal();
    }

    public void salidaFueSolicitada() {
        controlador.cerrar();
    }

    public TableModel getModeloDeTabla() {
        return modeloDeTabla;
    }

    public void eliminarArchivoFueSolicitado(int fila) {
        var archivoABorrar = controlador.getArchivos().get(fila);
        if (AccesoADatos.borrarArchivo(archivoABorrar)) {
            borrarArchivo(archivoABorrar);
        } else {
            mostrarErrorAUsuario("Ocurrió un error al borrar el archivo.");
        }
    }

    private void borrarArchivo(File archivoABorrar) {
        controlador.borrarArchivo(archivoABorrar);
        modeloDeTabla.setArchivos(controlador.getArchivos());
        consola.mostrarArchivoSeElimino(archivoABorrar.getName());
    }

    public void duplicarArchivoFueSolicitado(int fila) {
        File archivoADuplicar = controlador.getArchivos().get(fila);
        String nombreIngresado = pedirNombreDeArchivoAUsuario();
        if (!AccesoADatos.existeNombreDeArchivoEnCarpeta(nombreIngresado)) {
            try {
                duplicarArchivo(archivoADuplicar, nombreIngresado);
            } catch (IOException | IllegalStateException e) {
                mostrarErrorAUsuario("Ocurrió un error al crear el archivo.");
            }
        } else {
            mostrarErrorAUsuario("El nombre ingresado ya existe.");
        }
    }

    private void duplicarArchivo(File archivoADuplicar, String nombreIngresado) throws IOException, IllegalStateException {
        File duplicado = new File(AccesoADatos.getCarpeta() + "/" + nombreIngresado);
        if (AccesoADatos.copiarArchivo(archivoADuplicar, duplicado)) {
            controlador.agregarArchivo(duplicado);
            modeloDeTabla.setArchivos(controlador.getArchivos());
            consola.mostrarArchivoSeCreo(duplicado.getName());
        } else {
            throw new IllegalStateException("El archivo no se pudo crear.");
        }
    }

    private void mostrarErrorAUsuario(String mensaje) {
        JOptionPane.showMessageDialog(controlador.getMarco(),
                mensaje,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
    }

    private String pedirNombreDeArchivoAUsuario() {
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        return JOptionPane.showInputDialog(
                controlador.getMarco(),
                "Ingrese el nombre del nuevo archivo.",
                "Duplicar archivo",
                JOptionPane.PLAIN_MESSAGE);
    }

    public void conectarFueSolicitada() {
        modeloDeTabla.setArchivos(controlador.getArchivos());
    }
}
