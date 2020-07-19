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

import androidx.appcompat.app.AlertDialog;

import com.romellfudi.ussdlibrary.USSDService;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.util.List;

import cu.rm.defibank.parents.CustomActivityAnimated;
import cu.rm.defibank.utils.CustomDialogFragment;
import cu.rm.defibank.utils.GlobalPrefs;
import cu.rm.defibank.utils.USSDUtils;


public class AuthTransActivity extends CustomActivityAnimated {

    Button btnCancel;
    Button btnSend;
    ImageView logoTransfer;
    EditText codeInput;
    String codeTransf;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_trans);
    }

    @Override
    protected void setOnClickListeners() {
        btnCancel.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                codeTransf = codeInput.getText().toString();
                //TODO: Hacer las validaciones oportunas
                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("registrationStep", 3);
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
                        Log.d("SMS received: ", sms.getAddress()+": "+sms.getMsg());
                        AlertDialog.Builder builder = new AlertDialog.Builder(AuthTransActivity.this);

                        builder.setMessage(sms.getMsg())
                                .setTitle(sms.getAddress());
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                SmsRadar.stopSmsRadarService(getApplicationContext());
                                goActivity(v, AuthTransActivity.this, MainActivity.class);
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });
//                goActivity(v, AuthTransActivity.this, MainActivity.class);
                break;
            case R.id.btnCancel:
                goActivity(v, AuthTransActivity.this, CheckActivity.class);
                break;
        }
    }

    // autenticacion de pruba para saber si el codigo introducido por el cliente es correcto

    private void autenticarTransfermovil(){
        // TODO: esta es la forma de comprobar que el permiso de accesibilidad (PARA USSD) esta otorgado, hay que decirle al usuario que lo active.
        boolean permisoAccesibilidad = isAccessibilityServiceEnabled(this, USSDService.class);
        System.out.println("Accesibilidad: " + permisoAccesibilidad);
        if (permisoAccesibilidad){
            btnSend.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
            USSDUtils.autenticarTransfermovil(getApplicationContext(), codeTransf);

        }else{
            AlertDialog.Builder builder2 = new AlertDialog.Builder(AuthTransActivity.this);

            builder2.setMessage("Usted debe autorizar a la APP para que pueda utilizar los códigos USSD.")
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
    }

    @Override
    protected void animationsIn() {
        super.animationsIn();
        logoTransfer = findViewById(R.id.logoTransfermovil);
        logoTransfer.setAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_right_in));
    }

    @Override
    protected void animationsOut(View v) {
        super.animationsOut(v);
        logoTransfer.setAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_right_out));
    }

    @Override
    protected void animationsClearOut() {
        super.animationsClearOut();
        logoTransfer.clearAnimation();
    }
}