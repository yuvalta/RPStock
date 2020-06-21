package com.example.rpstock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;

import java.util.ArrayList;

public class UserStockListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<Item> listItems = new ArrayList<Item>();
    private Button increaseAmount;
    private Button decreaseAmount;
    private TextView itemName;

    public UserStockListAdapter(Context context, ArrayList<Item> data) {
        listItems = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listItems.size();
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.stock_item, null);
        }

        TextView text = vi.findViewById(R.id.name_of_item);
        text.setText(listItems.get(position).getName());
        EditText amount = vi.findViewById(R.id.amount);
//        amount.setText(listItems.get(position).getAmount());

        increaseAmount = vi.findViewById(R.id.increase_amount);
        increaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(), "+1", Toast.LENGTH_SHORT).show();
            }
        });

        decreaseAmount = vi.findViewById(R.id.decrease_amount);
        decreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(), "-1", Toast.LENGTH_SHORT).show();
            }
        });
        itemName = vi.findViewById(R.id.name_of_item);

        return vi;
    }
}
