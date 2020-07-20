package cu.rm.defibank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cu.rm.defibank.R;
import cu.rm.defibank.objects.Transaction;

public class TransactionsAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<Transaction> datos;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.transaction_element_list, null);
        TextView appName = view.findViewById(R.id.app_name);
        TextView importe = view.findViewById(R.id.importe);
        appName.setText(datos.get(position).getAppName());
        importe.setText(""+datos.get(position).getImporte());
        return view;
    }

    public TransactionsAdapter(Context context, List<Transaction> datos) {
        this.context = context;
        this.datos = datos;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
