package cu.rm.defibank.utils;

public class CheckMessages {

    private static String Normilize(String text) {
        return text.toLowerCase();
    }

    /**
     * Devuelve un booleano que indica, según el mensaje de autenticación de etecsa, si la autenticación es satisfactoria o no
     *
     * @param message El mensaje de autenticación que ofrece etecsa a procesar
     * @return boolean Verdadero si el mensaje es satisfactorio
     */
    public static boolean checkAuthenticationMessage(String message) {
        return Normilize(message).contains(Normilize("Usted se ha autenticado"))
                || Normilize(message).contains(Normilize("ya se encuentra autenticado"));
    }

    public static boolean checkTransferMethodMessage(String message) {
        return Normilize(message).contains(Normilize("realizado una transferencia"))
                || Normilize(message).contains(Normilize("Transferencia fue completada"));
    }

    public static boolean checkAddress(String address) {
        return Normilize(address).equals(Normilize("PAGOxMOVIL"));
    }
}
