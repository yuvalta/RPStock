package com.example.rpstock;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpstock.Adapters.UserStockListAdapter;
import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Objects.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class UserStockDialog extends Dialog {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView listView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    Employee oldEmployee;
    Context context;

    UserStockListAdapter adapter;
    ArrayList<Item> listItems = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_stock_dialog);

        listView = findViewById(R.id.stock_items_list);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

//        getListFromDB();
    }

//    private void getListFromDB() {
//        for (Map.Entry<String, Integer> entry : oldEmployee.getItems().entrySet()) {
//            Item item = new Item();
//            item.setAmount(entry.getValue());
//            item.setName(entry.getKey());
//            listItems.add(item);
//        }
//        setAdapterToList();
//    }

    private void setAdapterToList() {
        listView.setHasFixedSize(true);

        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getContext());
        }
        listView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listView.getContext(),
                DividerItemDecoration.VERTICAL);
        listView.addItemDecoration(dividerItemDecoration);

        if (mAdapter == null) {
            mAdapter = new UserStockListAdapter(listItems);
        }
        listView.setAdapter(mAdapter);
    }

    public UserStockDialog(@NonNull Context context) {
        super(context);
    }

    public UserStockDialog(Context context, Employee oldEmployee, View v) {
        super(context);
        this.oldEmployee = oldEmployee;
        this.context = context;
    }


}
