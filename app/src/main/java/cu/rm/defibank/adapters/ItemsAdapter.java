package cu.rm.defibank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cu.rm.defibank.R;
import cu.rm.defibank.objects.Item;

public class ItemsAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    List<Item> datos;

    public ItemsAdapter(Context context, List<Item> datos) {
        this.context = context;
        this.datos = datos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.list_element_pay, null);
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
