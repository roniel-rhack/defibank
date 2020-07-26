package cu.rm.defibank;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.utils.GlobalPrefs;
import cu.rm.defibank.utils.VolleyQueue;


public class CheckActivity extends CustomActivityAnimated {

    Button btnCancel;
    Button btnSend, btnResend;
    EditText codeInput;
    ConstraintLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                String code = codeInput.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    codeInput.setError("Este campo no puede estar vacío.");
                } else {
                    btnSend.setVisibility(View.INVISIBLE);
                    btnCancel.setVisibility(View.INVISIBLE);
                    btnResend.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    CheckCode(code);
                }
                break;
            case R.id.btnResend: {
                goActivity(CheckActivity.this, RegisterActivity.class);
                break;
            }
            case R.id.btnCancel:
                goActivity(CheckActivity.this, RegisterActivity.class);
                break;

        }
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        btnCancel = findViewById(R.id.btnCancel);
        btnSend = findViewById(R.id.btnSend);
        btnResend = findViewById(R.id.btnResend);
        codeInput = findViewById(R.id.inputCodeAct);
        loading = findViewById(R.id.progressBarLoading);
    }

    @Override
    protected void setOnClickListeners() {
        btnCancel.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnResend.setOnClickListener(this);
    }

    private void CheckCode(final String code) {
        String url = "https://josue95.pythonanywhere.com/api/dev/check_register/";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("Response json:", json.toString());

                            if (json.getString("status").equals("1001")) {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("registrationStep", 2);
                                editor.apply();
                                if (pref.getBoolean("openedForOtherApp", false)) {
                                    goActivity(CheckActivity.this, AuthTransActivity.class);
                                } else {
                                    goActivity(CheckActivity.this, MainActivity.class);
                                }
                            } else if (json.getString("status").equals("1002")) {
                                codeInput.setError("Código incorrecto, verifíquelo.");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d("Check Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                        btnSend.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.VISIBLE);
                        btnResend.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.INVISIBLE);

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String email;
                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
                email = pref.getString("email", "");
                params.put("email", email);
                params.put("code", code);

                return params;
            }
        };


        VolleyQueue.getInstance().addToQueue(postRequest);
    }


}