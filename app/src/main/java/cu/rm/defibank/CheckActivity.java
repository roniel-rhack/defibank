package cu.rm.defibank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cu.rm.defibank.parents.CustomActivityAnimated;
import cu.rm.defibank.utils.GlobalPrefs;
import cu.rm.defibank.utils.VolleyQueue;


public class CheckActivity extends CustomActivityAnimated {

    Button btnCancel;
    Button btnSend, btnResend;
    EditText codeInput;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
            case R.id.btnResend: {
                String code = codeInput.getText().toString();
                // TODO: hacer las validaciones pertinentes con el code
                btnSend.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                CheckCode(code);
                break;
            }
            case R.id.btnCancel:
                goActivity(v, CheckActivity.this, RegisterActivity.class);
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
        loading = findViewById(R.id.progressBar3);
    }

    @Override
    protected void setOnClickListeners() {
        btnCancel.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnResend.setOnClickListener(this);
    }

    private void CheckCode(final String code){
        String url = "https://josue95.pythonanywhere.com/api/dev/check_register/";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("Response json:", json.toString());

                            if (json.getString("status").equals("1001")){
                                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("registrationStep", 2);
                                editor.commit();
                                Intent intent = new Intent(CheckActivity.this, AuthTransActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (json.getString("status").equals("1002")){
                                Toast.makeText(getApplicationContext(), "El código no es correcto, verifíquelo.",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                        btnSend.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.INVISIBLE);

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                String email;
                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
                email = pref.getString("email","");
                params.put("email", email);
                params.put("code", code);

                return params;
            }
        };


        VolleyQueue.getInstance().addToQueue(postRequest);
    }
}