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
    Button bntSend, btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }

    @Override
    protected void setOnClickListeners() {
        bntSend.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        bntSend = findViewById(R.id.inputName);
        btnCancel = findViewById(R.id.btnCancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.btnSend):
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
                String transaction_id = pref.getString("transaction_id", "");
                String token = pref.getString("token", "");
                getPayment(transaction_id, token);
                break;
            }
        }
    }

    public  void getPayment(final String transaction_id, final String token){
        String url = "https://josue95.pythonanywhere.com/api/dev/get_payment/";
        Log.d("init", "iniciando el metodo get_payment");
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

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("transaction_id", transaction_id);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "x-www-form-urlencoded");
                params.put("Authorization", token);

                return params;
            }
        };

        VolleyQueue.getInstance().addToQueue(postRequest);
    }
}