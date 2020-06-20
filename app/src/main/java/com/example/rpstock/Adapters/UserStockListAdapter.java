package com.example.rpstock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;

import java.util.ArrayList;

public class UserStockListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<Item> listItems = new ArrayList<Item>();

    public UserStockListAdapter(Context context, ArrayList<Item> data) {
        listItems = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.stock_item, null);
        TextView text = (TextView) vi.findViewById(R.id.name_of_item);
        text.setText(listItems.get(position).getName());
        EditText amount = vi.findViewById(R.id.amount);
        amount.setText(listItems.get(position).getAmount());

        return vi;
    }
}
