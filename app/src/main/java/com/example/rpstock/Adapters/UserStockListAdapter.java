package com.example.rpstock.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class UserStockListAdapter extends RecyclerView.Adapter<UserStockListAdapter.MyViewHolder> {

    ArrayList<Item> listItems = new ArrayList<Item>();
    private Employee employee;

    public UserStockListAdapter(ArrayList<Item> itemArrayList, Employee _employee) {
        Collections.reverse(itemArrayList);
        listItems = itemArrayList;
        employee = _employee;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.stock_item, parent, false);

        UserStockListAdapter.MyViewHolder vh = new UserStockListAdapter.MyViewHolder(listItem, employee);
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

        private static final int TYPE_INCR = 0;
        private static final int TYPE_DECR = 1;
        private static final int TYPE_REPLACE_VALUE = 2;
        public TextView name, password, email, phone;
        public ImageButton options;
        private ImageButton increaseAmount;
        private ImageButton decreaseAmount;
        private Employee employeeInner;
        private EditText amountOfItem;
        private TextView itemName;

        public MyViewHolder(View view, Employee employee) {
            super(view);
            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.item_diameter);
            password = view.findViewById(R.id.item_kind);
            phone = view.findViewById(R.id.item_amount);

            employeeInner = employee;
        }

        public void bind(final Item item, final int position) {

            itemName = itemView.findViewById(R.id.name_of_item);
            itemName.setText(item.getName());
            amountOfItem = itemView.findViewById(R.id.amount);
            amountOfItem.setText(String.valueOf(item.getAmount()));

            increaseAmount = itemView.findViewById(R.id.increase_amount);
            increaseAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAmountByValue(item, 1, TYPE_INCR);
                }
            });

            decreaseAmount = itemView.findViewById(R.id.decrease_amount);
            decreaseAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAmountByValue(item, -1, TYPE_DECR);
                }
            });

            amountOfItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    changeAmountByValue(item, Integer.parseInt(amountOfItem.getText().toString()), TYPE_REPLACE_VALUE);
                }
            });
        }

        private void changeAmountByValue(Item item, final int i, final int type) {

            DatabaseReference mDatabase = getInstance().getReference();

            try {
                mDatabase.child("users").child(employeeInner.getID()).child("items").child(item.getID()).child("amount")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long value;
                                if (type == TYPE_INCR || type == TYPE_DECR) {
                                    value = (long) dataSnapshot.getValue();
                                    value = value + i;
                                    dataSnapshot.getRef().setValue(value);
                                } else {
                                    value = i;
                                    dataSnapshot.getRef().setValue(value);
                                }
                                amountOfItem.setText(String.valueOf(value));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            } catch (Exception e) {
                Toast.makeText(itemView.getContext(), "שגיאה, נסה שנית", Toast.LENGTH_SHORT).show();
        }
    }
}
}
