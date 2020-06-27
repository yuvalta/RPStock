package com.example.rpstock.Adapters;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class AllUserStockListAdapter extends RecyclerView.Adapter<AllUserStockListAdapter.MyViewHolder> {

    ArrayList<Item> listItems = new ArrayList<Item>();
    public TextView name, kind, diameter, amountOfItem;
    private static int MIN_VALUE_ALERT = 5;

    public AllUserStockListAdapter(ArrayList<Item> itemArrayList) {
        Collections.reverse(itemArrayList);
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
        kind.setText(listItems.get(position).getKind());

        name = holder.itemView.findViewById(R.id.name_of_item_stock);
        name.setText(listItems.get(position).getName());

        amountOfItem = holder.itemView.findViewById(R.id.amount_of_item_stock);
        amountOfItem.setText(String.valueOf(listItems.get(position).getAmount()));
        if (listItems.get(position).getAmount() < MIN_VALUE_ALERT) {
            amountOfItem.setBackgroundColor(Color.RED);
        }
        else {
//            Color.parseColor("#FF0000")
            amountOfItem.setBackgroundColor(Color.GREEN);
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
