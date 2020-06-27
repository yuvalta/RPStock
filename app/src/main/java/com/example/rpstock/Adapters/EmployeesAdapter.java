package com.example.rpstock.Adapters;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.MyViewHolder> {
    private ArrayList<Employee> mDataset;
    private AdapterView.OnItemClickListener listener;

    public TextView name, password, email, phone;
    public ImageButton options;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, password, email, phone;
        public ImageButton options;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.item_diameter);
            password = view.findViewById(R.id.item_kind);
            phone = view.findViewById(R.id.item_amount);
            options = view.findViewById(R.id.item_options);
        }

        public String getIDFromEmail(String number) {
            int index = number.indexOf('@');
            return number.substring(0, index);
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

        name = listItem.findViewById(R.id.name);
        email = listItem.findViewById(R.id.item_diameter);
        password = listItem.findViewById(R.id.item_kind);
        phone = listItem.findViewById(R.id.item_amount);
        options = listItem.findViewById(R.id.item_options);

        MyViewHolder vh = new MyViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        name.setText(mDataset.get(position).getName());
        email.setText(getIDFromEmail(mDataset.get(position).getEmail()));
        password.setText(mDataset.get(position).getPassword());
        phone.setText(mDataset.get(position).getPhone());

        if (position != 0) { // user can't delete first employee because i use it to get list of items
            options.setVisibility(View.VISIBLE);
            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(holder.itemView.getContext(), options);
                    popup.inflate(R.menu.employees_item_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_delete:
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("users").child(mDataset.get(position).getID()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    mDataset.remove(holder.itemView);
                                                    mDataset.remove(position);
                                                    notifyItemRemoved(position);

                                                    removeUserFromAuth(holder.itemView.getContext());
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.itemView.getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return true;
                                case R.id.menu_update:
                                    UserInfoDialog dialog = new UserInfoDialog(holder.itemView.getContext(), mDataset.get(position));
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

    private void removeUserFromAuth(final Context context) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getIDFromEmail(String number) {
        int index = number.indexOf('@');
        return number.substring(0, index);
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