package cu.rm.defibank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cu.rm.defibank.R;
import cu.rm.defibank.objects.Pay;

public class PaysAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<Pay> datos;

    public PaysAdapter(Context context, List<Pay> datos) {
        this.context = context;
        this.datos = datos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.transaction_element_list, null);
        ((TextView)view.findViewById(R.id.app_name)).setText("Revolico Droid App");
        ((TextView)view.findViewById(R.id.total)).setText("15678.87");
        ((TextView)view.findViewById(R.id.shipment)).setText("168.21");
        ((TextView)view.findViewById(R.id.taxs)).setText("200.35");
        ((TextView)view.findViewById(R.id.tips)).setText("20.55");
        ((TextView)view.findViewById(R.id.discount)).setText("185.99");
        if (position == 0)
            view.setPadding(0, 80, 0, 0);
        return view;
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
