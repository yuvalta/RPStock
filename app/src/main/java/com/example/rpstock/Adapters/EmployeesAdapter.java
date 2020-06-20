package com.example.rpstock.Adapters;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpstock.Objects.Employee;
import com.example.rpstock.R;
import com.example.rpstock.UserInfoDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.MyViewHolder> {
    private ArrayList<Employee> mDataset;
    private AdapterView.OnItemClickListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, password, email, phone;
        public ImageButton options;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.email);
            password = view.findViewById(R.id.password);
            phone = view.findViewById(R.id.phone);
            options = view.findViewById(R.id.item_options);
        }

        public void bind(final Employee item, final int position) {

            name.setText(item.getName());
            email.setText(item.getEmail());
            password.setText(item.getPassword());
            phone.setText(item.getPhone()); // why it is null?

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), options);
                    popup.inflate(R.menu.employees_item_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_delete:
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("users").child(item.getID()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(itemView.getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(itemView.getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return true;
                                case R.id.menu_update:
                                    UserInfoDialog dialog = new UserInfoDialog(itemView.getContext(), item);
                                    dialog.show();

                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
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