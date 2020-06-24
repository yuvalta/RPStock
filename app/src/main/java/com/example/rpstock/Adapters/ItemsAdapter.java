package com.example.rpstock.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private ArrayList<Item> mDataset;
    private ArrayList<String> employeesIDArrayList;


    public ItemsAdapter(ArrayList<Item> itemsArrayList, ArrayList<String> _employeesIDArrayList) {
        mDataset = itemsArrayList;
        employeesIDArrayList = _employeesIDArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemKind;
        private TextView itemDiameter;
        private TextView itemAmount;
        ProgressBar progressBar;
        ImageView deleteItem;

        public MyViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.item_name);
            itemDiameter = view.findViewById(R.id.item_diameter);
            itemKind = view.findViewById(R.id.item_kind);
            itemAmount = view.findViewById(R.id.item_amount);
            deleteItem = view.findViewById(R.id.item_options);
            progressBar = view.findViewById(R.id.add_item_progress);

        }

        public void bind(final Item item, final ArrayList<String> employeeList) {

            itemName.setText(item.getName());
            itemKind.setText(item.getKind());
            itemDiameter.setText(item.getDiameter());
            itemAmount.setText(String.valueOf(item.getAmount()));

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    progressBar.setVisibility(View.VISIBLE);
                    DatabaseReference mDatabase = getInstance().getReference();
                    for (String employeeKey : employeeList) {
                        mDatabase.child("users").child(employeeKey).child("items").child(item.getID()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(itemView.getContext(), R.string.deleted_item, Toast.LENGTH_SHORT).show();
//                                        progressBar.setVisibility(View.GONE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(itemView.getContext(), R.string.delete_item_falied, Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_item, parent, false);

        ItemsAdapter.MyViewHolder vh = new ItemsAdapter.MyViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position), employeesIDArrayList);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

