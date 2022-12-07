package controlador;

import utils.AccesoADatos;
import vista.Marco;
import vista.PanelMenu;

import java.awt.*;
import java.io.File;

public class ControladorAplicacion {

    private Marco vista;
    private ControladorMenu menu;
    private java.util.List<File> listaDeArchivos;

    public ControladorAplicacion() {
        cargarArchivos();
    }

    private void cargarArchivos() {
        listaDeArchivos = AccesoADatos.obtenerArchivos();
    }

    public void agregarArchivo(File archivo) {
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

    public java.util.List<File> getArchivos() {
        return listaDeArchivos;
    }

    public Component getMarco() {
        return vista;
    }

    public void borrarArchivo(File archivo) {
        listaDeArchivos.remove(archivo);
    }
}