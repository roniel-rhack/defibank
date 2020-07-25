package cu.rm.defibank.utils;

public class GlobalPrefs {
    public static String PREFS_FILE_NAME = "cu.rm.defibank.Prefs";

    // variables globales que definen los códigos USSD
    public static String SALDO_USSD = "*222#";
    public static String AUTENTICAR_TRANSFERMOVIL_USSD = "*444*40*BANCO#";
    public static String TRANSFERIR_TRANSFERMOVIL_USSD = "*444*45#";
    public static final String SALIR_TRANSFERMOVIL_USSD = "*444*70#";

    public enum BANCOS {
        BPA,
        BANDEC,
        BANMET
    }
    public static final String BANCO_BPA_USSD = "01";
    public static final String BANCO_BANDEC_USSD = "02";
    public static final String BANCO_BANMET_USSD = "03";

}
