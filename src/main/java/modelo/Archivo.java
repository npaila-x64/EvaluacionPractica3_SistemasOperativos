package modelo;

import java.io.File;

public class Archivo {

    private String nombre;
    private File archivo;

    private Archivo() {}

    public Archivo(File archivo) {
        this.archivo = archivo;
        nombre = archivo.getName();
    }

    public String getNombre() {
        return nombre;
    }
}
