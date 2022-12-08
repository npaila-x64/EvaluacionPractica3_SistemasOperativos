package controlador;

import excepcion.ComandoMalformadoException;
import modelo.ClientesConector;
import modelo.Comando;
import util.AccesoADatos;
import vista.ConsolaLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControladorServidor {

    private ConsolaLogger consola;
    private List<File> listaDeArchivos;
    private String respuesta;
    private static final String separadorArchivos = ";";

    public ControladorServidor() {
        cargarArchivos();
    }

    public void iniciar() {
        consola = new ConsolaLogger();
        try {
            new ClientesConector(this).esperarSolicitudes();
        } catch (IOException | ComandoMalformadoException e) {
            throw new RuntimeException(e);
        }
    }

    private void cargarArchivos() {
        listaDeArchivos = AccesoADatos.obtenerArchivos();
    }

    private void agregarArchivo(File archivo) {
        listaDeArchivos.add(archivo);
    }

    public List<String> getNombreDeArchivos() {
        List<String> listaDeNombres = new ArrayList<>();
        for (var archivo : listaDeArchivos) {
            listaDeNombres.add(archivo.getName());
        }
        return listaDeNombres;
    }

    public void borrarArchivo(File archivo) {
        listaDeArchivos.remove(archivo);
    }

    public boolean eliminarArchivoFueSolicitado(Map<String, String> atributos) {
        String nombre = atributos.get("nombre");
        try {
            File archivoABorrar = obtenerArchivoPorNombre(nombre);
            AccesoADatos.borrarArchivo(archivoABorrar);
            borrarArchivo(archivoABorrar);
            consola.mostrarArchivoSeElimino(archivoABorrar.getName());
            return true;
        } catch (IOException | IllegalStateException e) {
            consola.mostrarError(e.getMessage());
            return false;
        }
    }

    public boolean duplicarArchivoFueSolicitado(Map<String, String> atributos) {
        String nombre = atributos.get("nombre");
        String nombreIngresado = atributos.get("nombre_ingresado");
        File archivoADuplicar = obtenerArchivoPorNombre(nombre);
        try {
            duplicarArchivo(archivoADuplicar, nombreIngresado);
            consola.mostrarArchivoSeCreo(nombreIngresado);
            return true;
        } catch (IOException | IllegalStateException e) {
            consola.mostrarError(e.getMessage());
            return false;
        }
    }

    private File obtenerArchivoPorNombre(String nombre) {
        for (File archivo : AccesoADatos.obtenerArchivos()) {
            if (nombre.equals(archivo.getName())) {
                return archivo;
            }
        }
        throw new IllegalStateException("El archivo no existe.");
    }

    private void duplicarArchivo(File archivoADuplicar, String nombreIngresado) throws IOException, IllegalStateException {
        File duplicado = new File(AccesoADatos.getCarpeta() + "/" + nombreIngresado);
        if (AccesoADatos.existeNombreDeArchivoEnCarpeta(nombreIngresado)) {
            throw new IllegalStateException("El archivo ya existe en la carpeta.");
        }
        if (AccesoADatos.copiarArchivo(archivoADuplicar, duplicado)) {
            agregarArchivo(duplicado);
            consola.mostrarArchivoSeCreo(duplicado.getName());
        } else {
            throw new IllegalStateException("El archivo no se pudo crear.");
        }
    }

    public void clienteSeConecto() {
        consola.mostrarClienteSeConecto();
    }

    public boolean ejecutarComando(Comando comando) {
        switch (comando.getComandoEnum()) {
            case VER_ARCHIVOS:
                respuesta = String.join(separadorArchivos, getNombreDeArchivos());
                return true;
            case DUPLICAR:
                if (duplicarArchivoFueSolicitado(comando.getAtributos())) {
                    respuesta = "OK";
                    return true;
                } else {
                    respuesta = "Ocurrió un error al intentar duplicar el archivo";
                    return false;
                }
            case ELIMINAR:
                if (eliminarArchivoFueSolicitado(comando.getAtributos())) {
                    respuesta = "OK";
                    return true;
                } else {
                    respuesta = "Ocurrió un error al intentar eliminar el archivo";
                    return false;
                }
            default:
                throw new IllegalStateException();
        }
    }

    public String getRespuesta() {
        return respuesta;
    }
}
