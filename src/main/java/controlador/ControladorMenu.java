package controlador;

import utils.AccesoADatos;
import vista.ArchivosTableModel;
import vista.PanelMenu;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;

public class ControladorMenu {

    private final PanelMenu vista;
    private final ArchivosTableModel modeloDeTabla;
    private final ControladorAplicacion controlador;

    public ControladorMenu(ControladorAplicacion controlador) {
        this.controlador = controlador;
        modeloDeTabla = new ArchivosTableModel();
        vista = new PanelMenu(this);
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
            controlador.borrarArchivo(archivoABorrar);
            modeloDeTabla.setArchivos(controlador.getArchivos());
        }

    }

    public void duplicarArchivoFueSolicitado(int fila) {
        File archivoADuplicar = controlador.getArchivos().get(fila);
        String nombreIngresado = pedirNombreDeArchivo();
        if (!AccesoADatos.estaNombreDeArchivoEnUso(nombreIngresado)) {
            try {
                duplicarArchivo(archivoADuplicar, nombreIngresado);
            } catch (IOException e) {
                mostrarOcurrioUnError();
            }
        } else {
            mostrarNombreDeArchivoEnUso();
        }
    }

    private void duplicarArchivo(File archivoADuplicar, String nombreIngresado) throws IOException {
        File nuevoArchivo = new File(AccesoADatos.getCarpeta() + "/" + nombreIngresado);
        if (AccesoADatos.duplicarArchivo(archivoADuplicar, nuevoArchivo)) {

        }
        controlador.agregarArchivo(nuevoArchivo);
        modeloDeTabla.setArchivos(controlador.getArchivos());
    }

    private void mostrarOcurrioUnError() {
        JOptionPane.showMessageDialog(controlador.getMarco(),
                "Ocurrió un error al crear el archivo.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarNombreDeArchivoEnUso() {
        JOptionPane.showMessageDialog(controlador.getMarco(),
                "El nombre ingresado ya existe.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
    }

    private String pedirNombreDeArchivo() {
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
