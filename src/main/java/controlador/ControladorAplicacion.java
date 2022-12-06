package controlador;

import utils.AccesoADatos;
import vista.Marco;
import vista.PanelMenu;
import modelo.Archivo;

import java.util.List;

public class ControladorAplicacion {

    private Marco vista;
    private ControladorMenu menu;
    private List<Archivo> listaDeArchivos;

    public ControladorAplicacion() {
        cargarArchivos();
    }

    private void cargarArchivos() {
        listaDeArchivos = AccesoADatos.obtenerArchivos();
    }

    public void agregarArchivo(Archivo archivo) {
        listaDeArchivos.add(archivo);
    }

    public void iniciar() {
        vista = new Marco();
        crearControladores();
        menu.iniciar();
        vista.setVisible(true);
    }

    private void crearControladores() {
        menu = new ControladorMenu(this);
    }

    public void mostrarMenuPrincipal() {
        vista.mostrarMenu();
    }

    public void cerrar() {
        vista.dispose();
    }

    public void agregarMenu(PanelMenu menu) {
        vista.agregarMenu(menu);
    }

    public List<Archivo> getArchivos() {
        return listaDeArchivos;
    }
}