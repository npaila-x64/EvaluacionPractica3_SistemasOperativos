package utils;

import modelo.Archivo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AccesoADatos {

    public static List<Archivo> obtenerArchivos() {
        File folder = new File("src/archivos");
        File[] listaDeArchivos = folder.listFiles();
        List<Archivo> nuevaListaDeArchivos = new ArrayList<>();
        for (File archivo : listaDeArchivos) {
            if (archivo.isFile()) {
                nuevaListaDeArchivos.add(new Archivo(archivo));
            }
        }
        return nuevaListaDeArchivos;
    }
}
