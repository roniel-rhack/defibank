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
import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.objects.Item;
import cu.rm.defibank.objects.Pay;

public class MainActivity extends CustomActivityAnimated implements AdapterView.OnItemClickListener {

    ListView listTransactions;
    List<Pay> datos;
    TextView listEmptyLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Item[] items = new Item[4];
        items[0] = new Item("Pizza", "Pizza Napolitana", (float) 2.5, (float) 4.6, (float) 10.0);
        items[1] = new Item("Pizza1", "Pizza Napolitana1 de queso doble gouda con aderesos y otras cosas mas para rellenar espacio", (float) 2.1, (float) 4.2, (float) 10.5);
        items[2] = new Item("Pizza2", "Pizza Napolitana2", (float) 2.2, (float) 4.3, (float) 10.6);
        items[3] = new Item("Pizza3", "Pizza Napolitana3", (float) 2.3, (float) 4.4, (float) 10.7);
        datos = new LinkedList<>();
        datos.add(new Pay("RR", "App Tray", (float) 19480.29, (float) 485.38, (float) 879.8, (float) 437.48, (float) 978.48, items));
        datos.add(new Pay("RR", "App Revolico", (float) 3847.75, (float) 233, (float) 29384, (float) 2908, (float) 322));
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
        goActivity(detailsIntent);
    }
}