package cu.rm.defibank.utils;

public class CheckMessages {

    /**
     * Devuelve un booleano que indica si el mensaje de autenticacion de etecsa
     *
     * @param message El mensaje de autenticacion que ofrece etecsa a procesar
     * @return boolean Verdadero si el mensaje es satisfactorio
     */
    public static boolean checkAutenticationMessage(String message) {
        return message.contains("Usted se ha autenticado") && (message.contains("correctamente") || message.contains("satisfactoriamente"));
    }
}
