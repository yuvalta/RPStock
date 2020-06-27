package com.example.rpstock.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rpstock.Adapters.AdminMainAdapter;
import com.example.rpstock.Adapters.ExpandableUserStockAdapter;
import com.example.rpstock.Objects.Employee;
import com.example.rpstock.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllStockFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;

    private ArrayList<Employee> employees = new ArrayList<>();
    private RecyclerView employessList;
    private RecyclerView.Adapter mAdapter;

    public AllStockFragment() {
    }

    public static AllStockFragment newInstance(String param1, String param2) {
        AllStockFragment fragment = new AllStockFragment();
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
        View v = inflater.inflate(R.layout.fragment_all_stock, container, false);

        employessList = v.findViewById(R.id.all_items_stock_RV);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getListFromDB();

        return v;
    }

    private void getListFromDB() {
        mDatabase.child("users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employees.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        Employee e = ds1.getValue(Employee.class);

                        employees.add(e);
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

    private void inflateEmployeesList() {
        employessList.setHasFixedSize(true);

        employessList.setLayoutManager(new LinearLayoutManager(getContext()));

        if (mAdapter == null) {
            mAdapter = new ExpandableUserStockAdapter(employees);
        }
        employessList.setAdapter(mAdapter);
    }
}
