package com.example.rpstock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import javax.xml.namespace.QName;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.MyViewHolder> {
    private ArrayList<Employee> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, password, email;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            email = (TextView) view.findViewById(R.id.email);
            password = (TextView) view.findViewById(R.id.password);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EmployeesAdapter(ArrayList<Employee> employeeArrayList) {
        mDataset = employeeArrayList;
    }

    // Create new views (invoked by the layout manager)
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

        holder.name.setText(mDataset.get(position).getName());
        holder.email.setText(mDataset.get(position).getEmail());
        holder.password.setText(mDataset.get(position).getPassword());

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