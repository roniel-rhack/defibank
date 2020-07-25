package cu.rm.defibank.utils;

public class CheckMessages {

    /**
     * Devuelve un booleano que indica, según el mensaje de autenticación de etecsa, si la autenticación es satisfactoria o no
     *
     * @param message El mensaje de autenticación que ofrece etecsa a procesar
     * @return boolean Verdadero si el mensaje es satisfactorio
     */
    public static boolean checkAuthenticationMessage(String message) {
        return message.toLowerCase().contains("Usted se ha autenticado".toLowerCase());
    }

    public static boolean checkTransferMethodMessage(String message) {
        return message.toLowerCase().contains("realizado una transferencia".toLowerCase())
                || message.toLowerCase().contains("Transferencia fue completada".toLowerCase());
    }

    public static boolean checkAddress(String address){
        return address.toLowerCase().equals("PAGOxMOVIL".toLowerCase());
    }

}
