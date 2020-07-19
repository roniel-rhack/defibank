package cu.rm.defibank;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
    Button bntSend, btnCancel, btnChange;
    ProgressBar loading, loading_global;
    TextView pay, shipment, tax, card_to, card_manage, total_to_pay;
    String transaction_id, token, email;
    Double payNo, shipmentNo, taxNo, totalPayNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        container.setVisibility(View.INVISIBLE);
        loading_global.setVisibility(View.VISIBLE);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
        transaction_id = pref.getString("transaction_id", "");
        token = pref.getString("token", "");
        email = pref.getString("email", "");
        getPayment(transaction_id, token);

    }

    @Override
    protected void setOnClickListeners() {
        bntSend.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnChange.setOnClickListener(this);

    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        bntSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.btnCancel);
        loading = findViewById(R.id.progressBar);
        pay = findViewById(R.id.importe_product);
        shipment = findViewById(R.id.importe_envio);
        tax = findViewById(R.id.importe_comision);
        card_to = findViewById(R.id.card_to);
        card_manage = findViewById(R.id.card_manage);
        total_to_pay = findViewById(R.id.importe_total);
        loading_global = findViewById(R.id.progressBar_global);
        btnChange = findViewById(R.id.btnChange);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btnSend): {
                registerPayment(transaction_id, email, token);
                break;
            }
            case (R.id.btnCancel): {
                cancelPayment(transaction_id, token);
                break;
            }
            case (R.id.btnChange): {
                if (btnChange.getText().equals("Cambiar a CUC")){
                    pay.setText(payNo / 25 + "");
                    shipment.setText(shipmentNo / 25 + "");
                    tax.setText(taxNo / 25 + "");
                    total_to_pay.setText(totalPayNo / 25 + "");
                    btnChange.setText("Cambiar a CUP");
                }else{
                    pay.setText(payNo  + "");
                    shipment.setText(shipmentNo  + "");
                    tax.setText(taxNo  + "");
                    total_to_pay.setText(totalPayNo  + "");
                    btnChange.setText("Cambiar a CUC");

                }

                break;
            }
        }
    }


    public void getPayment(final String transaction_id, final String token) {
        String url = String.format("https://josue95.pythonanywhere.com/api/dev/get_payment/?transaction_id=%s", transaction_id);
        Log.d("init", "iniciando el metodo get_payment");
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("Response json:", json.toString());

                            if (json.getString("status").equals("1001")) {
                                pay.setText(json.getString("pay"));
                                shipment.setText(json.getString("shipment"));
                                tax.setText(json.getString("tax"));
                                card_to.setText(json.getString("card_to"));
                                card_manage.setText(json.has("card_manage") ? json.getString("card_manage") : "-");
                                payNo = json.getDouble("pay");
                                shipmentNo = json.getDouble("shipment");
                                taxNo = json.getDouble("tax");
                                totalPayNo = payNo + shipmentNo + taxNo;
                                total_to_pay.setText(totalPayNo + "");
                                container.setVisibility(View.VISIBLE);
                                loading_global.setVisibility(View.INVISIBLE);


                            } else if (json.getString("status").equals("1002")) {
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());

                    }
                }
        ) {
            //            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("transaction_id", transaction_id);
//                return params;
//            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        VolleyQueue.getInstance().addToQueue(getRequest);
    }

    private void makeTransfers() {

    }

    public void registerPayment(final String transaction_id, final String email, final String token) {
        String url = "https://josue95.pythonanywhere.com/api/dev/payment/";
        Log.d("init", "iniciando el metodo register_payment");
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("Response json:", json.toString());

                            if (json.getString("status").equals("1001")) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(PayActivity.this);

                                builder2.setMessage(String.format("El pago se ha registrado correctamente. TRANSACTION_ID: %s", json.getString("transaction_id")))
                                        .setTitle("Pago concluido");
                                builder2.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        finish();
                                    }
                                });
                                builder2.setNegativeButton("Ir al listado", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // TODO: enviar al usuario a la activity de listado
                                    }
                                });

                                AlertDialog dialog2 = builder2.create();
                                dialog2.show();

                            } else if (json.getString("status").equals("1002")) {
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("transaction_id", transaction_id);
                params.put("email", email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        VolleyQueue.getInstance().addToQueue(postRequest);
    }

    public void cancelPayment(final String transaction_id, final String token) {
        String url = String.format("https://josue95.pythonanywhere.com/api/dev/cancel_payment/?transaction_id=%s", transaction_id);
        Log.d("init", "iniciando el metodo register_payment");
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("Response json:", json.toString());

                            if (json.getString("status").equals("1001")) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(PayActivity.this);

                                builder2.setMessage("El pago se ha cancelado correctamente.")
                                        .setTitle("Pago cancelado");
                                builder2.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        finish();
                                    }
                                });
                                builder2.setNegativeButton("Ir al listado", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // TODO: enviar al usuario a la activity de listado
                                    }
                                });

                                AlertDialog dialog2 = builder2.create();
                                dialog2.show();

                            } else if (json.getString("status").equals("1002")) {
                                Toast.makeText(getApplicationContext(), "La cancelacion del pago falló, intente más tarde.",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());

                    }
                }
        ) {
            //            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("transaction_id", transaction_id);
//                return params;
//            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        VolleyQueue.getInstance().addToQueue(postRequest);
    }
}