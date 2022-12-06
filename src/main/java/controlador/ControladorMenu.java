package controlador;

import vista.ArchivosTableModel;
import vista.PanelMenu;
import modelo.Archivo;

import javax.swing.table.TableModel;

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

    }

    public void duplicarArchivoFueSolicitado(int fila) {

    }

    public void conectarFueSolicitada() {
        modeloDeTabla.setArchivos(controlador.getArchivos());
    }
}
