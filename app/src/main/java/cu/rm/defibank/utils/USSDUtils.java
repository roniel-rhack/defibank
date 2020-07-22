package cu.rm.defibank.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.romellfudi.ussdlibrary.SplashLoadingService;
import com.romellfudi.ussdlibrary.USSDApi;
import com.romellfudi.ussdlibrary.USSDController;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import cu.rm.defibank.SplashActivity;

public class USSDUtils extends Application {
    private static USSDUtils ussdUtils;
    private HashMap map;

    @Override
    public void onCreate() {
        super.onCreate();
        ussdUtils = this;
        map = new HashMap<>();
        map.put("KEY_LOGIN",new HashSet<>(Arrays.asList("espere", "waiting", "loading", "esperando")));
        map.put("KEY_ERROR",new HashSet<>(Arrays.asList("problema", "problem", "error", "null")));
    }

    public static HashMap getMap(){
        HashMap map = new HashMap<>();
        map.put("KEY_LOGIN",new HashSet<>(Arrays.asList("espere", "waiting", "loading", "esperando")));
        map.put("KEY_ERROR",new HashSet<>(Arrays.asList("problema", "problem", "error", "null")));
        return map;
    }

    public static void getSaldo(final Context context) {

        final USSDApi ussdApi = USSDController.getInstance(context);
        ussdApi.callUSSDInvoke(GlobalPrefs.SALDO_USSD, getMap(), new USSDController.CallbackInvoke() {
            @Override
            public void responseInvoke(String message) {
                // message has the response string data
                Log.d("Saldo: ",message);
                Toast.makeText(context, "Saldo: "+message, Toast.LENGTH_SHORT).show();
//                String dataToSend = "data"; // <- send "data" into USSD's input text
//                ussdApi.send(dataToSend,new USSDController.CallbackMessage(){
//                    @Override
//                    public void responseMessage(String message) {
//                        // message has the response string data from USSD
//                    }
//                });
            }

            @Override
            public void over(String message) {
                // message has the response string data from USSD or error
                // response no have input text, NOT SEND ANY DATA
                Log.d("Error ussd: ", message);
            }
        });
    }

    public static void autenticarTransfermovil(final Context context, final String code) {

        final USSDApi ussdApi = USSDController.getInstance(context);
        ussdApi.callUSSDInvoke(GlobalPrefs.AUTENTICAR_TRANSFERMOVIL_USSD, getMap(), new USSDController.CallbackInvoke() {
            @Override
            public void responseInvoke(String message) {
                // message has the response string data
                String dataToSend = code; // <- send "data" into USSD's input text
                ussdApi.send(dataToSend,new USSDController.CallbackMessage(){
                    @Override
                    public void responseMessage(String message) {
                        // message has the response string data from USSD
                        Log.d("USSD Autenticar: ", message);

                    }
                });
            }

            @Override
            public void over(String message) {
                // message has the response string data from USSD or error
                // response no have input text, NOT SEND ANY DATA
                Log.d("USSD Autenticar: ", message);

            }
        });
    }

    public static void salirTransfermovil(final Context context) {

        final USSDApi ussdApi = USSDController.getInstance(context);
        ussdApi.callUSSDInvoke(GlobalPrefs.SALIR_TRANSFERMOVIL_USSD, getMap(), new USSDController.CallbackInvoke() {
            @Override
            public void responseInvoke(String message) {
                // message has the response string data

            }

            @Override
            public void over(String message) {
                // message has the response string data from USSD or error
                // response no have input text, NOT SEND ANY DATA
                Log.d("USSD Salir: ", message);

            }
        });
    }

    public static void transferirTransfermovil(final Context context, final String cuenta, final Double importe) {

        final USSDApi ussdApi = USSDController.getInstance(context);
        ussdApi.callUSSDInvoke(GlobalPrefs.TRANSFERIR_TRANSFERMOVIL_USSD, getMap(), new USSDController.CallbackInvoke() {
            @Override
            public void responseInvoke(String message) {
                // message has the response string data
                String dataToSend = cuenta; // <- send "data" into USSD's input text
                ussdApi.send(dataToSend,new USSDController.CallbackMessage(){
                    @Override
                    public void responseMessage(String message) {
                        // message has the response string data from USSD
                        Log.d("USSD Transferir - cuenta: ", message);
                        ussdApi.send(String.valueOf(importe),new USSDController.CallbackMessage(){
                            @Override
                            public void responseMessage(String message) {
                                // message has the response string data from USSD
                                Log.d("USSD Tranferir - importe: ", message);

                            }
                        });

                    }
                });
            }

            @Override
            public void over(String message) {
                // message has the response string data from USSD or error
                // response no have input text, NOT SEND ANY DATA
                Log.d("USSD Transferir - resultado: ", message);

            }
        });
    }
}
