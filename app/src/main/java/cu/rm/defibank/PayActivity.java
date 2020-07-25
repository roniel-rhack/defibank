package cu.rm.defibank;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.utils.CheckMessages;
import cu.rm.defibank.utils.GlobalPrefs;
import cu.rm.defibank.utils.USSDUtils;
import cu.rm.defibank.utils.VolleyQueue;

public class PayActivity extends CustomActivityAnimated {
    Button bntSend, btnCancel, btnChange;
    ProgressBar loading, loading_global;
    TextView pay, shipment, tax, card_to, card_manage, total_to_pay;
    String transaction_id, token, email, card_for_pay, card_for_tax;
    Double payNo, shipmentNo, taxNo, totalPayNo;

    public PayActivity() {
        super();
        containerInAnimDisable = true;
    }

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
        loading = findViewById(R.id.progressBarLoading);
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
                makeTransfers();
                break;
            }
            case (R.id.btnCancel): {
                cancelPayment(transaction_id, token);
                break;
            }
            case (R.id.btnChange): {
                if (btnChange.getText().equals("Cambiar a CUC")) {
                    pay.setText(payNo / 25 + "");
                    shipment.setText(shipmentNo / 25 + "");
                    tax.setText(taxNo / 25 + "");
                    total_to_pay.setText(totalPayNo / 25 + "");
                    btnChange.setText("Cambiar a CUP");
                } else {
                    pay.setText(payNo + "");
                    shipment.setText(shipmentNo + "");
                    tax.setText(taxNo + "");
                    total_to_pay.setText(totalPayNo + "");
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
                                card_for_pay = "9200069993304827";//json.getString("card_to").replaceAll("\\s+", "");
                                card_to.setText(json.getString("card_to"));
                                card_for_tax = json.has("card_manage") ? json.getString("card_manage") : "-";
                                card_for_tax = card_for_pay.replaceAll("\\s+", "");
                                card_manage.setText(json.has("card_manage") ? json.getString("card_manage") : "-");
                                payNo = json.getDouble("pay");
                                shipmentNo = json.getDouble("shipment");
                                taxNo = json.getDouble("tax");
                                totalPayNo = payNo + shipmentNo + taxNo;
                                total_to_pay.setText(totalPayNo + "");
                                container.setVisibility(View.VISIBLE);
                                loading_global.setVisibility(View.INVISIBLE);
                                if (container != null)
                                    container.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.container_in));


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

        if (card_for_tax.equals("-")) {
            // la carretera es P2V
            double importe_venta;
            if (btnChange.getText().equals("Cambiar a CUC")) {
                importe_venta = (payNo + shipmentNo + taxNo);
            } else {
                importe_venta = ((payNo + shipmentNo + taxNo) / 25);
            }
            USSDUtils.transferirTransfermovil(getApplicationContext(), card_for_pay, importe_venta);
            SmsRadar.initializeSmsRadarService(getApplicationContext(), new SmsListener() {
                @Override
                public void onSmsSent(Sms sms) {
                    Log.d("SMS Sent: ", sms.getMsg());
                }

                @Override
                public void onSmsReceived(Sms sms) {
                    Log.d("SMS received: ", sms.getAddress() + ": " + sms.getMsg());
                    if (CheckMessages.checkAddress(sms.getAddress()) && CheckMessages.check(sms.getMsg())) {

                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);

                    builder.setMessage(sms.getMsg())
                            .setTitle(sms.getAddress());
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            SmsRadar.stopSmsRadarService(getApplicationContext());
                            registerPayment(transaction_id, email, token);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        } else {
            final double importe_venta, importe_comision;
            if (btnChange.getText().equals("Cambiar a CUC")) {
                importe_venta = (payNo + shipmentNo);
                importe_comision = taxNo;
            } else {
                importe_venta = ((payNo + shipmentNo) / 25);
                importe_comision = taxNo / 25;

            }
            USSDUtils.transferirTransfermovil(getApplicationContext(), card_for_pay, importe_venta);
            SmsRadar.initializeSmsRadarService(getApplicationContext(), new SmsListener() {
                @Override
                public void onSmsSent(Sms sms) {
                    Log.d("SMS Sent: ", sms.getMsg());
                }

                @Override
                public void onSmsReceived(Sms sms) {
                    Log.d("SMS received: ", sms.getAddress() + ": " + sms.getMsg());
                    // TODO: validar si el mensaje es de PagoXMovil

                    AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);

                    builder.setMessage(sms.getMsg() + " A continuación se pagará la comisión.")
                            .setTitle(sms.getAddress());
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            SmsRadar.stopSmsRadarService(getApplicationContext());
                            USSDUtils.transferirTransfermovil(getApplicationContext(), card_for_tax, importe_comision);
                            SmsRadar.initializeSmsRadarService(getApplicationContext(), new SmsListener() {
                                @Override
                                public void onSmsSent(Sms sms) {
                                    Log.d("SMS Sent: ", sms.getMsg());
                                }

                                @Override
                                public void onSmsReceived(Sms sms) {
                                    final Sms smsCopy = sms;
                                    Log.d("SMS received: ", sms.getAddress() + ": " + sms.getMsg());
                                    // TODO: validar si el mensaje es de PagoXMovil

                                    AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);

                                    builder.setMessage(sms.getMsg() + " A continuación se pagará la comisión.")
                                            .setTitle(sms.getAddress());
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User clicked OK button
                                            SmsRadar.stopSmsRadarService(getApplicationContext());

                                            registerPayment(transaction_id, email, token);
                                        }
                                    });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                }
                            });
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        }

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
                            Log.d("json - reg payment:", json.toString());
                            USSDUtils.salirTransfermovil(getApplicationContext());

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
                                        goActivity(PayActivity.this, MainActivity.class);

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
        Log.d("init", "iniciando el metodo cancel_payment");
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