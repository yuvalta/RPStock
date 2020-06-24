package com.example.rpstock.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rpstock.Adapters.AdminMainAdapter;
import com.example.rpstock.Adapters.UserStockListAdapter;
import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ItemsListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;

    private ArrayList<Item> items = new ArrayList<>();
    private Employee employee;
    private RecyclerView employessList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public ItemsListFragment() {
    }

    public ItemsListFragment(Employee employee) {
        this.employee = employee;
    }

    public static ItemsListFragment newInstance(String param1, String param2) {
        ItemsListFragment fragment = new ItemsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items_list, container, false);

        employessList = v.findViewById(R.id.items_recycler_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();

//        getListFromDB();
        getListOfItemsFromEmployee(employee);
        inflateEmployeesList();

        return v;
    }

    @Override
    public void onDestroy() {
        try {
            getActivity().getSupportFragmentManager().popBackStack();
            super.onDestroy();
        } catch (Exception e) {
        }
    }

    private void getListFromDB() {
//        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        Employee e = ds1.getValue(Employee.class);

                        getListOfItemsFromEmployee(e);
                    }
                }
                inflateEmployeesList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error in loading", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListOfItemsFromEmployee(Employee employee) {
        for (Map.Entry<String, Item> entry : employee.getItems().entrySet()) {
            Item item = new Item();
            item.setAmount(entry.getValue().getAmount());
            item.setName(entry.getValue().getName());
            item.setID(entry.getKey());
            item.setKind(entry.getValue().getKind());
            item.setDiameter(entry.getValue().getDiameter());
            items.add(item);
        }
    }

    private void inflateEmployeesList() {
        employessList.setHasFixedSize(true);

        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getContext());
        }
        employessList.setLayoutManager(layoutManager);

        if (mAdapter == null) {
            mAdapter = new UserStockListAdapter(items, employee);
        }
        employessList.setAdapter(mAdapter);
    }
}
