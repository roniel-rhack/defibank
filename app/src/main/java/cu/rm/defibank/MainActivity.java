package cu.rm.defibank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import cu.rm.defibank.adapters.PaysAdapter;
import cu.rm.defibank.customsCompatActivity.CustomActivityFullAnimated;
import cu.rm.defibank.objects.Pay;

public class MainActivity extends CustomActivityFullAnimated implements AdapterView.OnItemClickListener {

    ListView listTransactions;
    List<Pay> datos;
    TextView listEmptyLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datos = new LinkedList<>();
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 233, (float) 29384, (float) 2908, (float) 322));
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 233, (float) 29384, (float) 2908, (float) 322));
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 233, (float) 29384, (float) 2908, (float) 322));
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 233, (float) 29384, (float) 2908, (float) 322));
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 233, (float) 29384, (float) 2908, (float) 322));
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 233, (float) 29384, (float) 2908, (float) 322));
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 233, (float) 29384, (float) 2908, (float) 322));
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 233, (float) 29384, (float) 2908, (float) 322));
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 233, (float) 29384, (float) 2908, (float) 322));
        listTransactions.setAdapter(new PaysAdapter(this, datos));

        if (datos.size() > 0)
            listEmptyLabel.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        listTransactions = findViewById(R.id.listTrans);
        listEmptyLabel = findViewById(R.id.list_empty);
    }

    @Override
    protected void setOnClickListeners() {
        listTransactions.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        detailsIntent.putExtra("pay", datos.get(position));
        goActivity(view, detailsIntent);
    }
}