package cu.rm.defibank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import cu.rm.defibank.adapters.ItemsAdapter;
import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.objects.Pay;

public class DetailsActivity extends CustomActivityAnimated {

    protected Intent intent;
    protected Bundle extras;
    protected Pay pay;
    protected ListView list_items;
    protected TextView importe_total, id, app, propina, envio, impuesto, descuento, total_a_pagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        intent = getIntent();
        extras = intent.getExtras();

        assert extras != null;
        pay = (Pay) extras.getSerializable("pay");

        assert pay != null;
        id.setText(pay.getTransactionId());
        app.setText(pay.getApplication());
        importe_total.setText(String.format("%s", (pay.getDiscounts()+pay.getTotal()-pay.getTips())));
        propina.setText(String.format("%s", pay.getTips()));
        envio.setText(String.format("%s", pay.getShipment()));
        impuesto.setText(String.format("%s", pay.getTaxs()));
        descuento.setText(String.format("%s", pay.getDiscounts()));
        total_a_pagar.setText(String.format("%s", (pay.getTotal()+pay.getShipment()+pay.getTaxs())));

        list_items.setAdapter(new ItemsAdapter(this, pay.getItems()));
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        list_items = findViewById(R.id.list_items);
        id = findViewById(R.id.id);
        app = findViewById(R.id.app);
        propina = findViewById(R.id.propina);
        envio = findViewById(R.id.envio);
        impuesto = findViewById(R.id.impuesto);
        descuento = findViewById(R.id.descuento);
        total_a_pagar = findViewById(R.id.total);
        importe_total = findViewById(R.id.importe_total);
    }

    @Override
    protected void setOnClickListeners() {

    }

    @Override
    public void onClick(View v) {

    }
}