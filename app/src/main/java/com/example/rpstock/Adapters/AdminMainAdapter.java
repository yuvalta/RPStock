package com.example.rpstock.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpstock.Objects.Employee;
import com.example.rpstock.R;
import com.example.rpstock.UserStockDialog;

import java.util.ArrayList;

public class AdminMainAdapter extends RecyclerView.Adapter<AdminMainAdapter.MyViewHolder> {

    private ArrayList<Employee> mDataset;
    private AdapterView.OnItemClickListener listener;

    public AdminMainAdapter(ArrayList<Employee> employeeArrayList) {
        mDataset = employeeArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.employee_item_items, parent, false);

        AdminMainAdapter.MyViewHolder vh = new AdminMainAdapter.MyViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        } else {
            return 0;
        }

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, password, email, phone;
        public ImageButton options;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.email);
            password = view.findViewById(R.id.password);
            phone = view.findViewById(R.id.phone);
        }

        public void bind(final Employee item, final int position) {

            name.setText(item.getName());
            email.setText(item.getEmail());
            password.setText(item.getPassword());
            phone.setText(item.getPhone());

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    UserStockDialog dialog = new UserStockDialog(itemView.getContext(), item, v);
                    dialog.show();
                    return false;
                }
            });
        }
    }
}
