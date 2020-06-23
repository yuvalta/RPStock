package com.example.rpstock.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rpstock.Adapters.ItemsAdapter;
import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddItemFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    EditText itemName;
    EditText itemDiameter;
    EditText itemKind;
    Button addItemButton;

    Employee currentEmployee;

    RecyclerView itemsRecycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

//    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private ArrayList<Item> itemsArrayList = new ArrayList<>();
    private ArrayList<String> employeeIDArrayList = new ArrayList<>();

    public AddItemFragment() {
    }

    public AddItemFragment(Employee _currentEmployee) {
        currentEmployee = _currentEmployee;
    }


    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        itemName = view.findViewById(R.id.item_name_ET);
        itemKind = view.findViewById(R.id.item_kind_ET);
        itemDiameter = view.findViewById(R.id.item_diameter_ET);

        addItemButton = view.findViewById(R.id.add_item_button);

        addItemButton.setOnClickListener(addButtonClick);
        itemsRecycler = view.findViewById(R.id.items_list);


        getListFromDB();


        return view;
    }

    private void getListFromDB() {
        mDatabase.child("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsArrayList.clear();
                employeeIDArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        Employee e = ds1.getValue(Employee.class);

                        employeeIDArrayList.add(ds1.getKey());

                    }
                    getListOfItemsFromEmployee(ds.child(employeeIDArrayList.get(0)).getValue(Employee.class));
                }
                inflateAllItemsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error in loading", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListOfItemsFromEmployee(Employee employee) {

        for (Map.Entry<String, Item> employeeItem : employee.getItems().entrySet()) {
            Item item = new Item();
            item.setAmount(employeeItem.getValue().getAmount());
            item.setName(employeeItem.getValue().getName());
            item.setKind(employeeItem.getValue().getKind());
            item.setDiameter(employeeItem.getValue().getDiameter());
            item.setID(employeeItem.getValue().getID());
            itemsArrayList.add(item);
        }

    }

    private void inflateAllItemsList() {
        itemsRecycler.setHasFixedSize(true);

        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getContext());
            itemsRecycler.setLayoutManager(layoutManager);
        }

        if (mAdapter == null) {
            mAdapter = new ItemsAdapter(itemsArrayList, employeeIDArrayList);
        }
        itemsRecycler.setAdapter(mAdapter);
    }

    View.OnClickListener addButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            progressBar.setVisibility(View.VISIBLE);

            final Item newItem = new Item(itemName.getText().toString(),
                    0,
                    itemDiameter.getText().toString(),
                    itemKind.getText().toString(),
                    String.valueOf(UUID.randomUUID()));


            for (String employeeKey : employeeIDArrayList) {
                mDatabase.child("users").child(employeeKey).child("items").child(newItem.getID()).setValue(newItem)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Item Added!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };
}
