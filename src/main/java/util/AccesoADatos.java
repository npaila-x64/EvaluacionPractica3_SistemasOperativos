package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccesoADatos {

    private final static File carpeta = new File("src/archivos/");

    public static String getCarpeta() {
        return carpeta.getPath();
    }

    public static List<File> obtenerArchivos() {
        File[] listaDeArchivos = carpeta.listFiles();
        List<File> nuevaListaDeArchivos = new ArrayList<>();
        for (File archivo : listaDeArchivos) {
            if (archivo.isFile()) {
                nuevaListaDeArchivos.add(archivo);
            }
        }
        return nuevaListaDeArchivos;
    }

    public static boolean borrarArchivo(File archivoABorrar) {
        return archivoABorrar.delete();
    }

    public static boolean copiarArchivo(File archivoADuplicar, File duplicado) throws IOException {
        try (
            InputStream in = new BufferedInputStream(
                    new FileInputStream(archivoADuplicar));
            OutputStream out = new BufferedOutputStream(
                    new FileOutputStream(duplicado))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        }
        return duplicado.exists();
    }

    public static boolean existeNombreDeArchivoEnCarpeta(String nombre) {
        var archivos = obtenerArchivos();
        for (var archivo : archivos) {
            if (archivo.getName().equals(nombre)) {
                return true;
            }
        }
        return false;
    }
}

