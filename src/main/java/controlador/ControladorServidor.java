package controlador;

import excepcion.ComandoMalformadoException;
import handler.ClientesHandler;
import modelo.Comando;
import modelo.ComandoEnum;
import util.AccesoADatos;
import vista.Consola;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControladorServidor {

    private Consola consola;
    private List<File> listaDeArchivos;
    private Comando respuesta;
    private static final String separadorArchivos = ";";

    public ControladorServidor() {
        cargarArchivos();
    }

    public void iniciar() {
        consola = new Consola();
        try {
            new ClientesHandler(this).esperarSolicitudes();
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
            respuesta.setAtributo("mensaje", e.getMessage());
            return false;
        }
    }

    public boolean duplicarArchivoFueSolicitado(Map<String, String> atributos) {
        String nombre = atributos.get("nombre");
        String nombreIngresado = atributos.get("nombre_ingresado");
        try {
            File archivoADuplicar = obtenerArchivoPorNombre(nombre);
            duplicarArchivo(archivoADuplicar, nombreIngresado);
            consola.mostrarArchivoSeCreo(nombreIngresado);
            return true;
        } catch (IOException | IllegalStateException e) {
            consola.mostrarError(e.getMessage());
            respuesta.setAtributo("mensaje", e.getMessage());
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

    public void clienteSeConecto(String hostname, String hostaddress) {
        consola.mostrarClienteSeConecto(hostname, hostaddress);
    }

    public void ejecutarComando(Comando comando) {
        respuesta = new Comando();
        respuesta.setComandoEnum(ComandoEnum.RESPUESTA);
        switch (comando.getComandoEnum()) {
            case VER_ARCHIVOS:
                respuesta.setAtributo("respuesta", "OK");
                respuesta.setAtributo("lista",
                        String.join(separadorArchivos, getNombreDeArchivos()));
                break;
            case DUPLICAR:
                if (duplicarArchivoFueSolicitado(comando.getAtributos())) {
                    respuesta.setAtributo("respuesta", "OK");
                } else {
                    respuesta.setAtributo("respuesta", "ERROR");
                }
                break;
            case ELIMINAR:
                if (eliminarArchivoFueSolicitado(comando.getAtributos())) {
                    respuesta.setAtributo("respuesta", "OK");
                } else {
                    respuesta.setAtributo("respuesta", "ERROR");
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }

    public String getRespuesta() {
        return respuesta.toString();
    }

    public void seSolicitoComando(Comando comando, String hostName) {

    }
}
