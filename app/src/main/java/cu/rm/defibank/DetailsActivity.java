package cu.rm.defibank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import cu.rm.defibank.adapters.ItemsAdapter;
import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.objects.Pay;

public class DetailsActivity extends CustomActivityAnimated {

    protected Intent intent;
    protected Bundle extras;
    protected Pay pay;
    protected ListView list_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        intent = getIntent();
        extras = intent.getExtras();

        assert extras != null;
        pay = (Pay) extras.getSerializable("pay");
        list_items.setAdapter(new ItemsAdapter(this, pay.getItems()));
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        list_items = findViewById(R.id.list_items);
    }

    @Override
    protected void setOnClickListeners() {

    }

    @Override
    public void onClick(View v) {

    }
}