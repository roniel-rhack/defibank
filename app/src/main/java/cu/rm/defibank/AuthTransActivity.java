package cu.rm.defibank;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.romellfudi.ussdlibrary.USSDService;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.util.List;

import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.utils.CheckMessages;
import cu.rm.defibank.utils.GlobalPrefs;
import cu.rm.defibank.utils.USSDUtils;


public class AuthTransActivity extends CustomActivityAnimated {

    Button btnCancel;
    Button btnSend;
    ImageView logoTransfer;
    EditText codeInput;
    String codeTransf;
    ConstraintLayout loading;
    RadioGroup radioBancos;

    GlobalPrefs.BANCOS banco = GlobalPrefs.BANCOS.BANDEC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_trans);
    }

    @Override
    protected void setOnClickListeners() {
        btnCancel.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        radioBancos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.banco_bpa:
                        banco = GlobalPrefs.BANCOS.BPA;
                        break;
                    case R.id.banco_bandec:
                        banco = GlobalPrefs.BANCOS.BANDEC;
                        break;
                    case R.id.banco_banmet:
                        banco = GlobalPrefs.BANCOS.BANMET;
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                if (!codeInput.getText().toString().equals("")) {
                    codeTransf = codeInput.getText().toString();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putInt("registrationStep", 3);
                    editor.putString("codeTransfermovil", codeTransf);
                    editor.commit();

                    autenticarTransfermovil();
                    SmsRadar.initializeSmsRadarService(getApplicationContext(), new SmsListener() {
                        @Override
                        public void onSmsSent(Sms sms) {
                            Log.d("SMS Sent: ", sms.getMsg());
                        }

                        @Override
                        public void onSmsReceived(Sms sms) {
                            final Sms smsCopy = sms;
                            Log.d("SMS received: ", sms.getAddress() + ": " + sms.getMsg());

                            if (CheckMessages.checkAddress(smsCopy.getAddress()))
                                Log.d("check adrress", "true");
                            else Log.d("check adrress", "false");

                            if (CheckMessages.checkAuthenticationMessage(smsCopy.getMsg()))
                                Log.d("check msg", "true");
                            else Log.d("check msg", "false");
                            // TODO: FUTURE: Evitar que la notificacion del mensaje se muestre en el sistema para q no aparezca dos veces

                            if (CheckMessages.checkAddress(smsCopy.getAddress()) && CheckMessages.checkAuthenticationMessage(smsCopy.getMsg())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AuthTransActivity.this);
                                builder.setMessage(sms.getMsg())
                                        .setTitle(sms.getAddress());
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        SmsRadar.stopSmsRadarService(getApplicationContext());
                                        Log.d("stop r", "radar is stopped");
                                        goActivity(AuthTransActivity.this, PayActivity.class);
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                btnSend.setVisibility(View.VISIBLE);
                                btnCancel.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                } else {
                    codeInput.setError("Este campo es obligatorio.");
                }

//                goActivity(v, AuthTransActivity.this, MainActivity.class);
                break;
            case R.id.btnCancel:
                goActivity(AuthTransActivity.this, CheckActivity.class);
                break;
        }
    }

    // autenticacion de pruba para saber si el codigo introducido por el cliente es correcto

    private void autenticarTransfermovil() {
        // TODO: esta es la forma de comprobar que el permiso de accesibilidad (PARA USSD) esta otorgado, hay que decirle al usuario que lo active.
        boolean permisoAccesibilidad = isAccessibilityServiceEnabled(this, USSDService.class);
        System.out.println("Accesibilidad: " + permisoAccesibilidad);
        if (permisoAccesibilidad) {
            btnSend.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
            USSDUtils.autenticarTransfermovil(getApplicationContext(), codeTransf, banco);

        } else {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(AuthTransActivity.this);

            builder2.setMessage("Usted debe autorizar a la APP DefiBank para que pueda utilizar los códigos USSD.")
                    .setTitle("Permiso de accesibilidad");
            builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
                }
            });

            AlertDialog dialog2 = builder2.create();
            dialog2.show();
        }

    }

    /***
     * Este método comprueba si el permiso de accesibilidad para usar USSD esta activado.
     * @param context
     * @param service
     * @return boolean
     */
    public boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
                return true;
        }

        return false;
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        btnCancel = findViewById(R.id.btnCancel);
        btnSend = findViewById(R.id.btnSend);
        codeInput = findViewById(R.id.inputCodeAct);
        loading = findViewById(R.id.progressBar2);
        radioBancos = findViewById(R.id.banco_sel);
    }

    @Override
    protected void animationsIn() {
        super.animationsIn();
        logoTransfer = findViewById(R.id.logoTransfermovil);
        logoTransfer.setAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_right_in));
    }

    @Override
    protected void animationsOut() {
        super.animationsOut();
        logoTransfer.setAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_right_out));
    }

    @Override
    protected void animationsClearOut() {
        super.animationsClearOut();
        logoTransfer.clearAnimation();
    }
}