package cu.rm.defibank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.objects.Pay;

public class DetailsActivity extends CustomActivityAnimated {

    protected Intent intent;
    protected Bundle extras;
    protected Pay pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        intent = getIntent();
        extras = intent.getExtras();

        assert extras != null;
        pay = (Pay) extras.getSerializable("pay");
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
    }

    @Override
    protected void setOnClickListeners() {

    }

    @Override
    public void onClick(View v) {

    }
}