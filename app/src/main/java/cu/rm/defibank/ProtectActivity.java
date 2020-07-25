package cu.rm.defibank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.utils.GlobalPrefs;

public class ProtectActivity extends CustomActivityAnimated {
    EditText code;
    Button btnSend, btnCancel;
    int activityToGoAfter;
    boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect);
        Intent i = getIntent();

       activityToGoAfter = i.getIntExtra("activityToGoAfter", 2);
       login = i.getBooleanExtra("login", false);
       if (!login){
           btnSend.setText("Guardar");
       }
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        code = findViewById(R.id.inputCodeAct);
        btnSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.btnCancel);

    }

    @Override
    protected void setOnClickListeners() {
        btnSend.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:
            {
                if (TextUtils.isEmpty(code.getText().toString())){
                    code.setError("Este campo no puede estar vacío.");

                }else if (TextUtils.getTrimmedLength(code.getText().toString()) != 4){
                    code.setError("Este campo debe tener 4 dígitos.");
                }else{
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
                    if (login){
                        if (!pref.getString("password","").equals(code.getText().toString())){
                           code.setError("Clave incorrecta, verifique.");
                           break;
                        }
                    }else{
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("password", code.getText().toString());
                        editor.commit();
                    }

                    Intent intent;
                    switch (activityToGoAfter){
                        //     * 1 - Datos del pago
                        //     * 2 - Listado de pagos
                        //     * 3 - Autenticar transfermovil
                        //     * 4 - Check Activity
                        //     * 5 - Register
                        case 1:
                        {
                            intent = new Intent(ProtectActivity.this, PayActivity.class);
                            break;
                        }
                        case 2:
                        {
                            intent = new Intent(ProtectActivity.this, MainActivity.class);
                            break;
                        }
                        case 3:
                        {
                            intent = new Intent(ProtectActivity.this, AuthTransActivity.class);
                            break;
                        }
                        case 4:
                        {
                            intent = new Intent(ProtectActivity.this, CheckActivity.class);
                            break;
                        }
                        case 5:
                        {
                            intent = new Intent(ProtectActivity.this, RegisterActivity.class);
                            break;
                        }
                        default:
                            intent = new Intent(ProtectActivity.this, MainActivity.class);

                    }
                    startActivity(intent);
                    finish();
                }
                break;
            }
            case R.id.btnCancel:
            {
                finish();
                break;
            }
        }
    }
}