package com.example.rpstock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;
import com.example.rpstock.UserStockDialog;

import java.util.ArrayList;
import java.util.Collections;

public class UserStockListAdapter extends RecyclerView.Adapter<UserStockListAdapter.MyViewHolder> {

    ArrayList<Item> listItems = new ArrayList<Item>();

    public UserStockListAdapter(ArrayList<Item> employeeArrayList) {
        Collections.reverse(employeeArrayList);
        listItems = employeeArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.stock_item, parent, false);

        UserStockListAdapter.MyViewHolder vh = new UserStockListAdapter.MyViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(listItems.get(position), position);
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

        public TextView name, password, email, phone;
        public ImageButton options;
        private Button increaseAmount;
        private Button decreaseAmount;
        private TextView itemName;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.email);
            password = view.findViewById(R.id.password);
            phone = view.findViewById(R.id.phone);
        }

        public void bind(final Item item, final int position) {

            TextView text = itemView.findViewById(R.id.name_of_item);
            text.setText(item.getName());
            EditText amount = itemView.findViewById(R.id.amount);
            amount.setText(String.valueOf(item.getAmount()));

            increaseAmount = itemView.findViewById(R.id.increase_amount);
            increaseAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "+1", Toast.LENGTH_SHORT).show();
                }
            });

            decreaseAmount = itemView.findViewById(R.id.decrease_amount);
            decreaseAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "-1", Toast.LENGTH_SHORT).show();
                }
            });
            itemName = itemView.findViewById(R.id.name_of_item);
        }
    }
}
