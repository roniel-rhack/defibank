package cu.rm.defibank.utils;

public class CheckMessages {

    /**
     * Devuelve un booleano que indica, según el mensaje de autenticación de etecsa, si la autenticación es satisfactoria o no
     *
     * @param message El mensaje de autenticación que ofrece etecsa a procesar
     * @return boolean Verdadero si el mensaje es satisfactorio
     */
    public static boolean checkAuthenticationMessage(String message) {
        return message.contains("Usted se ha autenticado") && (message.contains("correctamente") || message.contains("satisfactoriamente"));
    }

    public static boolean checkAddress(String address){
        return address.equals("PAGOxMOVIL");
    }
}
