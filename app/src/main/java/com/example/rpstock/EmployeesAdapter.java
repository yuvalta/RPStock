package com.example.rpstock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.MyViewHolder> {
    private ArrayList<Employee> mDataset;
    private AdapterView.OnItemClickListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, password, email, phone;

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
                    Toast.makeText(v.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    public EmployeesAdapter(ArrayList<Employee> employeeArrayList) {
        mDataset = employeeArrayList;
    }

    @Override
    public EmployeesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.employee_item, parent, false);

        MyViewHolder vh = new MyViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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
}