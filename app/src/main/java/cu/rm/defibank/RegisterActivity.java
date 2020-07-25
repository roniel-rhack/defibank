package cu.rm.defibank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cu.rm.defibank.customsCompatActivity.CustomActivityFullAnimated;
import cu.rm.defibank.utils.GlobalPrefs;
import cu.rm.defibank.utils.VolleyQueue;


public class RegisterActivity extends CustomActivityFullAnimated {

    Button btnSend;
    EditText nameEdit, emailEdit;
    ConstraintLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        btnSend = findViewById(R.id.btnSend);
        nameEdit = findViewById(R.id.inputName);
        emailEdit = findViewById(R.id.inputEmail);
        loading = findViewById(R.id.progressBarLoading);
    }

    @Override
    protected void setOnClickListeners() {
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
            {
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                if (TextUtils.isEmpty(name)){
                    nameEdit.setError("Este campo no puede estar vacío.");
                } else if (TextUtils.isEmpty(email)){
                    emailEdit.setError("Este campo no puede estar vacío.");
                }else{
                    Log.d("Registration", name+" "+email);
                    btnSend.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    registerUser(email, name);
                }

                break;
            }
        }
    }

    public  void registerUser(final String email, final String name){
        String url = "https://josue95.pythonanywhere.com/api/dev/register/";
        Log.d("init", "iniciando el metodo");
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
                                editor.putBoolean("isIntroOpened",true);
                                editor.putInt("registrationStep", 1);
                                editor.putString("email", email);
                                editor.putString("name", name);
                                editor.commit();
                                Intent intent = new Intent(RegisterActivity.this, CheckActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (json.getString("status").equals("1002")){
                                Toast.makeText(getApplicationContext(), json.getString("message"),
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
                params.put("email", email);
                return params;
            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("User-Agent", "Nintendo Gameboy");
//                params.put("Accept-Language", "fr");
//
//                return params;
//            }
        };

        VolleyQueue.getInstance().addToQueue(postRequest);
    }
}