package modelo;

import excepciones.ComandoMalformadoException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Comando {

    private ComandoEnum comando;
    private static final String separador = "&";
    private static final String subseparador = "=";
    private Map<String, String> atributos;

    public Comando() {
        this.atributos = new HashMap<>();
    }

    public static Comando parsearComando(String respuesta) throws ComandoMalformadoException {
        String[] comandoArray = respuesta.split(separador);
        esComandoValido(comandoArray);

        Comando comando = new Comando();
        comando.setComandoEnum(Arrays.stream(ComandoEnum.values())
                .filter(comandoEnum -> comandoEnum.toString().equals(comandoArray[0]))
                .findFirst().get());
        comando.setAtributos(parsearAtributos(comandoArray));

        return comando;
    }

    private static Map<String, String> parsearAtributos(String[] comandoArray) {
        Map<String, String> atributos = new HashMap<>();
        if (comandoArray.length < 2) return atributos;
        for (int indice = 1; indice < comandoArray.length; indice++) {
            String llave = comandoArray[indice].split(subseparador)[0];
            String valor = comandoArray[indice].split(subseparador)[1];
            atributos.put(llave, valor);
        }
        return atributos;
    }

    public void setComandoEnum(ComandoEnum comando) {
        this.comando = comando;
    }

    public void setAtributo(String llave, String valor) {
        this.atributos.put(llave, valor);
    }

    private void setAtributos(Map<String, String> atributos) {
        this.atributos = atributos;
    }

    public ComandoEnum getComandoEnum() {
        return comando;
    }

    public Map<String, String> getAtributos() {
        return atributos;
    }

    private static void esComandoValido(String[] comandoArray) {
        validaExistenciaDeComando(comandoArray);
        validaCantidadMaximaDeParametros(comandoArray);
        validaAtributosTienenFormatoCorrecto(comandoArray);
    }

    private static void validaCantidadMaximaDeParametros(String[] comandoArray) {
        if (comandoArray.length > 3) {
            throw ComandoMalformadoException.noSePudoParsearComando(
                    "El comando supera la cantidad máxima de parámetros");
        }
    }

    private static void validaExistenciaDeComando(String[] comandoArray) {
        Arrays.stream(ComandoEnum.values())
                .filter(comandoEnum -> comandoEnum.toString().equals(comandoArray[0]))
                .findFirst()
                .orElseThrow(() -> ComandoMalformadoException.noSePudoParsearComando(
                        "El comando no existe"));
    }

    private static void validaAtributosTienenFormatoCorrecto(String[] comandoArray) {
        if (comandoArray.length == 1) return;
        for (int indice = 1; indice < comandoArray.length; indice++) {
            if (comandoArray[indice].split(subseparador).length != 2) {
                throw ComandoMalformadoException.noSePudoParsearComando(
                        "El comando contiene atributos inválidos");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append(this.comando.toString());
        for (String llave : this.atributos.keySet()) {
            st.append(separador);
            st.append(llave);
            st.append(subseparador);
            st.append(this.atributos.get(llave));
        }
        return st.toString();
    }
}
