package com.example.rpstock.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rpstock.Adapters.AdminMainAdapter;
import com.example.rpstock.Adapters.EmployeesAdapter;
import com.example.rpstock.Objects.Employee;
import com.example.rpstock.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// the idea is: show all users in recycler view here
// long press on user opens UserStockDialog
// in this dialog there is listview with all items
// each item has text and buttons
public class AdminMainFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;

    private ArrayList<Employee> employees = new ArrayList<>();
    private RecyclerView employessList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public AdminMainFragment() {
        // Required empty public constructor
    }
    public static AdminMainFragment newInstance(String param1, String param2) {
        AdminMainFragment fragment = new AdminMainFragment();
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
        View v = inflater.inflate(R.layout.fragment_admin_main, container, false);

        employessList = v.findViewById(R.id.employee_items_recycler);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getListFromDB();

        return v;
    }

    private void getListFromDB() { // TODO: problem here! why its give my the items and not users?
//        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
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
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error in loading", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void inflateEmployeesList() {
        employessList.setHasFixedSize(true);

        if (layoutManager == null) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        }
        employessList.setLayoutManager(layoutManager);

        if (mAdapter == null) {
            mAdapter = new AdminMainAdapter(employees);
        }
        employessList.setAdapter(mAdapter);
    }
}
