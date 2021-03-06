package com.uv.rpstock.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uv.rpstock.Objects.Item;
import com.uv.rpstock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    public ArrayList<Item> mDataset;
    private ArrayList<String> employeesIDArrayList;


    public ItemsAdapter(ArrayList<Item> itemsArrayList, ArrayList<String> _employeesIDArrayList) {
        mDataset = itemsArrayList;
        employeesIDArrayList = _employeesIDArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemKind;
        private TextView itemDiameter;
        private TextView itemSeq;
        ProgressBar progressBar;
        ImageView deleteItem;

        public MyViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.item_item_name);
            itemDiameter = view.findViewById(R.id.item_item_diameter);
            itemKind = view.findViewById(R.id.item_item_kind);
            itemSeq = view.findViewById(R.id.item_item_seq);
            deleteItem = view.findViewById(R.id.item_options);
            progressBar = view.findViewById(R.id.add_item_progress);

        }

        public void bind(final Item item, final ArrayList<String> employeeList) {

            itemName.setText(item.getName());
            String nippleLength = item.getNippleLength();

            if (nippleLength.equals("ללא")) {
                itemKind.setText(item.getKind());
            } else {
                itemKind.setText(nippleLength);
            }

            itemDiameter.setText(item.getDiameter());
            itemSeq.setText(String.valueOf(item.getSeq()));

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemView.getContext());
                    alertDialog.setMessage("האם אתה בטוח שאת/ה רוצה למחוק אביזר זה?")
                            .setPositiveButton("מחק", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteItemFromDB(item, employeeList);
                                }
                            })
                            .setNegativeButton("בטל מחיקה", null)
                            .show();

                }
            });
        }

        private void deleteItemFromDB(final Item item, final ArrayList<String> employeeList) {
            final DatabaseReference mDatabase = getInstance().getReference();
            for (String employeeKey : employeeList) {
                mDatabase.child("users").child(employeeKey).child("items").child(item.getID()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(itemView.getContext(), R.string.delete_item_falied, Toast.LENGTH_SHORT).show();
                    }
                });
            }
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

