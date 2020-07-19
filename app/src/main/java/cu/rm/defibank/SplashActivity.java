package cu.rm.defibank;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.romellfudi.ussdlibrary.USSDService;

import java.util.List;
import java.util.concurrent.Executor;

import cu.rm.defibank.parents.CustomSplashActivityAnimated;
import cu.rm.defibank.utils.GlobalPrefs;


public class SplashActivity extends CustomSplashActivityAnimated {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private boolean openedForOtherApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        openedForOtherApp = false;


//        Intent originalIntent = getIntent();
        String sharedId = "defi-BeCvMUiNSB9ZflqTqAeM";//originalIntent.getStringExtra("transaction_id");
        String token = "wUD1IskOFVr4ScIEBvUw";


        if (sharedId != null) {
            openedForOtherApp = true;
            SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("openedForOtherApp", true);
            editor.putString("transaction_id", sharedId);
            editor.putString("token", token);
            editor.putString("email", "roniel.rack@gmail.com");
            editor.commit();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (ContextCompat.checkSelfPermission(SplashActivity.this,
                        Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(SplashActivity.this,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {

                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.CALL_PHONE},
                            1);
                } else {
                    // Permission has already been granted
//                    Continue();
                    // para pruebas de transfermovil
                    Intent i = new Intent(SplashActivity.this, PayActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, 3500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Continue();
                    break;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



    private void Continue() {
        // check if the phone support biometric authentication
        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                executor = ContextCompat.getMainExecutor(getApplicationContext());
                biometricPrompt = new BiometricPrompt(SplashActivity.this,
                        executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode,
                                                      @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);

                        // comprobar que existen las prefs
                        if (loadedPreferences() && getRegistrationStep() == 3) {
                            if (openedForOtherApp) {
                                // TODO: Si la App fue abierta por otra, mostrar los datos del pago
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // TODO: Si la App fue abierta por el usuario,  mostrar el listado de pagos
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else if (loadedPreferences() && getRegistrationStep() == 2) {
                            Intent intent = new Intent(SplashActivity.this, AuthTransActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (loadedPreferences() && getRegistrationStep() == 1) {
                            // TODO: Preguntar al usuario si desea comenzar desde el inicio?
                            Intent intent = new Intent(SplashActivity.this, CheckActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Autenticación biométrica")
                        .setSubtitle("Acceda a la App utilizando sus credenciales biométricas.")
                        .setConfirmationRequired(false)
                        .setDeviceCredentialAllowed(true)
                        .build();


                // TODO: Consider integrating with the keystore to unlock cryptographic operations, if needed by the app.

                biometricPrompt.authenticate(promptInfo);

                break;
            //TODO: Que hacer si el telefono no soporta ningun tipo de autenticacion biometrica?
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("MY_APP_TAG", "The user hasn't associated " +
                        "any biometric credentials with their account.");
                break;
        }
    }

    @Override
    protected void animationsClearOut() {
    }


    @Override
    protected void setOnClickListeners() {

    }

    @Override
    protected void animationsIn() {
        super.animationsIn();
    }

    @Override
    public void onClick(View v) {
    }

    private boolean loadedPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
        return pref.getBoolean("isIntroOpened", false);
    }

    private int getRegistrationStep() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
        return pref.getInt("registrationStep", 0);
    }
}