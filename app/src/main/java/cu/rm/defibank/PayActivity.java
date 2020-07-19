package cu.rm.defibank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cu.rm.defibank.parents.CustomActivityFullAnimated;
import cu.rm.defibank.utils.GlobalPrefs;
import cu.rm.defibank.utils.VolleyQueue;

public class PayActivity extends CustomActivityFullAnimated {
    Button bntSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }

    @Override
    protected void setOnClickListeners() {

    }

    @Override
    public void onClick(View v) {

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
                                Toast.makeText(getApplicationContext(), "El registro falló, intente más tarde.",
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