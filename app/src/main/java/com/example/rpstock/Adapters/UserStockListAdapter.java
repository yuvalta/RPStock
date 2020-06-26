package com.example.rpstock.Adapters;

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

public class UserStockListAdapter extends RecyclerView.Adapter<UserStockListAdapter.MyViewHolder> {

    ArrayList<Item> listItems = new ArrayList<Item>();
    private Employee employee;
    private boolean isAdmin;

    public UserStockListAdapter(ArrayList<Item> itemArrayList, Employee _employee, boolean _isAdmin) {
        Collections.reverse(itemArrayList);
        listItems = itemArrayList;
        employee = _employee;
        isAdmin = _isAdmin;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.stock_item, parent, false);

        UserStockListAdapter.MyViewHolder vh = new UserStockListAdapter.MyViewHolder(listItem, employee, isAdmin);
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
        private static final int MAX_VALUE = 100;
        private static final int MIN_VALUE = 0;


        public TextView name, kind, diameter, phone;
        public ImageButton options;
        private ImageButton increaseAmount;
        private ImageButton decreaseAmount;
        private Employee employeeInner;
        private NumberPicker amountOfItem;
        private TextView itemName;
        private boolean innerIsAdmin;

        public MyViewHolder(View view, Employee employee, boolean isAdmin) {
            super(view);
            employeeInner = employee;
            innerIsAdmin = isAdmin;
        }

        public void bind(final Item item, final int position) {
            diameter = itemView.findViewById(R.id.diameter_in_item);
            diameter.setText(item.getDiameter());

            kind = itemView.findViewById(R.id.kind_in_item);
            kind.setText(item.getKind());

            itemName = itemView.findViewById(R.id.name_of_item);
            itemName.setText(item.getName());

            amountOfItem = itemView.findViewById(R.id.amount);
            setNumberPicker(item.getAmount());

            increaseAmount = itemView.findViewById(R.id.increase_amount);
            decreaseAmount = itemView.findViewById(R.id.decrease_amount);

            if (innerIsAdmin) {
                increaseAmount.setVisibility(View.VISIBLE);
                decreaseAmount.setClickable(true);
                amountOfItem.setClickable(true);
                amountOfItem.setEnabled(true);
            } else {
                increaseAmount.setVisibility(View.GONE);
                decreaseAmount.setClickable(false);
                amountOfItem.setClickable(false);
                amountOfItem.setEnabled(false);
            }

            increaseAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAmountByValue(item, 1, TYPE_INCR);
                }
            });

            decreaseAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAmountByValue(item, -1, TYPE_DECR);
                }
            });

            amountOfItem.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    changeAmountByValue(item, newVal, TYPE_REPLACE_VALUE);
                }
            });

        }

        private void setNumberPicker(int amount) {
            amountOfItem.setMinValue(MIN_VALUE);
            amountOfItem.setMaxValue(MAX_VALUE);
            amountOfItem.setValue(amount);
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
                                amountOfItem.setValue((int) value);
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
