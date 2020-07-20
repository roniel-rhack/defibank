package cu.rm.defibank;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import cu.rm.defibank.adapters.TransactionsAdapter;
import cu.rm.defibank.customsCompatActivity.CustomActivityAnimated;
import cu.rm.defibank.objects.Transaction;

public class MainActivity extends CustomActivityAnimated {

    ListView listTransactions;
    List<Transaction> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datos = new LinkedList<>();
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        datos.add(new Transaction("DD", "WW", (float) 18293.23));
        listTransactions.setAdapter(new TransactionsAdapter(this, datos));
    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        listTransactions = findViewById(R.id.listTrans);
    }

    @Override
    protected void setOnClickListeners() {

    }


    @Override
    public void onClick(View v) {

    }
}