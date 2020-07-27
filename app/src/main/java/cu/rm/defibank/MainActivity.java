package cu.rm.defibank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import cu.rm.defibank.adapters.PaysAdapter;
import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.objects.Item;
import cu.rm.defibank.objects.Pay;
import cu.rm.defibank.utils.GlobalPrefs;
import cu.rm.defibank.utils.VolleyQueue;

public class MainActivity extends CustomActivityAnimated implements AdapterView.OnItemClickListener {

    ListView listTransactions;
    List<Pay> datos;
    TextView listEmptyLabel;
    String token, email;
    ConstraintLayout loading, errorContainer;
    Button btnReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = new LinkedList<>();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(GlobalPrefs.PREFS_FILE_NAME, MODE_PRIVATE);
        token = pref.getString("token", "");
        email = pref.getString("email", "");
        getPayments(email);
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        listTransactions = findViewById(R.id.listTrans);
        listEmptyLabel = findViewById(R.id.list_empty);
        loading = findViewById(R.id.progressBarLoading);
        errorContainer = findViewById(R.id.errorContainer);
        btnReload = findViewById(R.id.btnReload);
    }

    @Override
    protected void setOnClickListeners() {
        listTransactions.setOnItemClickListener(this);
        btnReload.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReload:
                getPayments(email);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        detailsIntent.putExtra("pay", datos.get(position));
        detailsIntent.putExtra("listado", true);
        goActivity(detailsIntent);
    }

    public void getPayments(final String email) {
        String url = String.format("https://josue95.pythonanywhere.com/api/dev/list_pays/?email=%s", email);
        Log.d("init", "iniciando el metodo get_payments");
        loading.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.INVISIBLE);
        listEmptyLabel.setVisibility(View.INVISIBLE);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    loading.setVisibility(View.INVISIBLE);
                    // response
                    try {
                        JSONObject json = new JSONObject(response);
                        Log.d("Response json:", json.toString());

                        if (json.getString("status").equals("1001")) {
                            JSONArray pays = json.getJSONArray("pays");
                            if (pays.length() == 0)
                                listEmptyLabel.setVisibility(View.VISIBLE);

                            for (int i = 0; i < pays.length(); i++) {
                                JSONObject obj = pays.getJSONObject(i);
                                JSONArray items = obj.getJSONArray("items");
                                List<Item> itemsArray = new LinkedList<>();
                                for (int j = 0; j < items.length(); j++) {
                                    itemsArray.add(new Item(items.getJSONObject(j).getString("title"), items.getJSONObject(j).getString("description"),
                                            items.getJSONObject(j).getDouble("tip"), items.getJSONObject(j).getDouble("discount"),
                                            items.getJSONObject(j).getDouble("cost")));
                                }
                                datos.add(new Pay(obj.getString("transaction_id"), obj.getString("application"), obj.getDouble("total"), obj.getDouble("discounts"), obj.getDouble("shipment"), obj.getDouble("taxs"), obj.getDouble("tips"), itemsArray));
                            }
                            listTransactions.setAdapter(new PaysAdapter(this, datos));

                            if (datos.size() > 0)
                                listEmptyLabel.setVisibility(View.INVISIBLE);
//
//                            container.setVisibility(View.VISIBLE);
//                            loading_global.setVisibility(View.INVISIBLE);
//                            if (container != null)
//                                container.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.container_in));


                        } else if (json.getString("status").equals("1002")) {
                            Toast.makeText(getApplicationContext(), "El registro falló, intente más tarde.",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.d("Response", response);
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                        loading.setVisibility(View.INVISIBLE);
                        errorContainer.setVisibility(View.VISIBLE);
                    }
                });
//        ) {
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "x-www-form-urlencoded");
//                params.put("Authorization", "Bearer " + token);
//
//                return params;
//            }
//        };

        VolleyQueue.getInstance().addToQueue(getRequest);
    }
}