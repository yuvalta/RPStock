package com.example.rpstock;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rpstock.Adapters.UserStockListAdapter;
import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Objects.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserStockDialog extends Dialog {


    private Button increaseAmount;
    private Button decreaseAmount;
    private TextView itemName;
    private ListView listView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    Employee oldEmployee;
    Context context;

    UserStockListAdapter adapter;
    ArrayList<Item> listItems = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_stock_dialog);

        increaseAmount = findViewById(R.id.increase_amount);
        increaseAmount.setOnClickListener(increaseAmountClick);
        decreaseAmount = findViewById(R.id.decrease_amount);
        decreaseAmount.setOnClickListener(decreaseAmountClick);
        itemName = findViewById(R.id.name_of_item);
        listView = findViewById(R.id.stock_items_list);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getListFromDB();


    }

    private void getListFromDB() {
        mDatabase.child("items");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listItems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //for (DataSnapshot ds1 : ds.getChildren()) {
                        Item e = ds.getValue(Item.class);

                        listItems.add(e);
                    //}
                }
                setAdapterToList();            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error in loading", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapterToList() {
        adapter = new UserStockListAdapter(context, listItems);
        listView.setAdapter(adapter);
    }

    public UserStockDialog(@NonNull Context context) {
        super(context);
    }

    public UserStockDialog(Context context, Employee oldEmployee) {
        super(context);
        this.oldEmployee = oldEmployee;
        this.context = context;
    }

    View.OnClickListener increaseAmountClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "+1", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener decreaseAmountClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "-1", Toast.LENGTH_SHORT).show();
        }
    };


}
