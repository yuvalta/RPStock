package com.uv.rpstock.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uv.rpstock.Objects.Item;
import com.uv.rpstock.R;

import java.util.ArrayList;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class AllUserStockListAdapter extends RecyclerView.Adapter<AllUserStockListAdapter.MyViewHolder> {

    private static int MIN_VALUE_ALERT = 5;
    ArrayList<Item> listItems = new ArrayList<Item>();
    public TextView name, kind, diameter, amountOfItem;

    public AllUserStockListAdapter(ArrayList<Item> itemArrayList) {
        listItems = itemArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.all_user_stock_item, parent, false);

        AllUserStockListAdapter.MyViewHolder vh = new AllUserStockListAdapter.MyViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        diameter = holder.itemView.findViewById(R.id.diameter_of_item_stock);
        diameter.setText(listItems.get(position).getDiameter());

        kind = holder.itemView.findViewById(R.id.kind_of_item_stock);

        String nippleLength = listItems.get(position).getNippleLength();
        if (nippleLength.equals("ללא")) {
            kind.setText(listItems.get(position).getKind());
        } else {
            kind.setText(nippleLength);
        }

        name = holder.itemView.findViewById(R.id.name_of_item_stock);
        name.setText(listItems.get(position).getName());

        amountOfItem = holder.itemView.findViewById(R.id.amount_of_item_stock);
        amountOfItem.setText(String.valueOf(listItems.get(position).getAmount()));
        if (listItems.get(position).getAmount() < MIN_VALUE_ALERT) {
            amountOfItem.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red));
        } else {
            amountOfItem.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.green));
        }

    }

    @Override
    public int getItemCount() {
        if (listItems != null) {
            return listItems.size();
        } else {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
        }
    }
}
