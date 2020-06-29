package com.example.rpstock.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rpstock.Adapters.ItemsAdapter;
import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddItemFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextInputLayout itemName;
    EditText seqET;
    Spinner itemDiameter;
    Spinner itemKind;
    Spinner itemNippleLength;
    Button addItemButton;

    ProgressBar progressBar;

    private String kindSelected;
    private String diameterSelected;
    private String lengthSelected;

    Employee currentEmployee;

    RecyclerView itemsRecycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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

        progressBar = view.findViewById(R.id.add_item_progress);
        itemName = view.findViewById(R.id.item_name_ET);
        itemKind = view.findViewById(R.id.item_kind_ET);
        itemDiameter = view.findViewById(R.id.item_diameter_ET);
        itemNippleLength = view.findViewById(R.id.item_nipple_length);

        seqET = view.findViewById(R.id.item_seq);

        addItemButton = view.findViewById(R.id.add_item_button);

        addItemButton.setOnClickListener(addButtonClick);
        itemsRecycler = view.findViewById(R.id.items_list);

        setKindSpinner(view.getContext());
        setDiameterSpinner(view.getContext());
        setItemNippleLengthSpinner(view.getContext());

        getListFromDB();

        return view;
    }

    private void setKindSpinner(Context context) {
        ArrayAdapter<CharSequence> adapterKind = ArrayAdapter.createFromResource(context,
                R.array.kind_array, android.R.layout.simple_spinner_item);
        adapterKind.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemKind.setAdapter(adapterKind);
        itemKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kindSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDiameterSpinner(Context context) {
        ArrayAdapter<CharSequence> adapterDiameter = ArrayAdapter.createFromResource(context,
                R.array.diameter_array, android.R.layout.simple_spinner_item);
        adapterDiameter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemDiameter.setAdapter(adapterDiameter);
        itemDiameter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                diameterSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setItemNippleLengthSpinner(Context context) {
        ArrayAdapter<CharSequence> adapterDiameter = ArrayAdapter.createFromResource(context,
                R.array.length_array, android.R.layout.simple_spinner_item);
        adapterDiameter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemNippleLength.setAdapter(adapterDiameter);
        itemNippleLength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lengthSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getListFromDB() {
        progressBar.setVisibility(View.VISIBLE);
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
            item.setSeq(employeeItem.getValue().getSeq());
            itemsArrayList.add(item);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            itemsArrayList.sort(new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {

                    Integer x1 = o1.getSeq();
                    Integer x2 = o2.getSeq();
                    return x2.compareTo(x1);
                }
            });
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
        progressBar.setVisibility(View.GONE);
    }

    View.OnClickListener addButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            progressBar.setVisibility(View.VISIBLE);

            final Item newItem = new Item(itemName.getEditText().getText().toString(),
                    0,
                    diameterSelected,
                    kindSelected,
                    String.valueOf(UUID.randomUUID()),
                    Integer.parseInt(seqET.getText().toString()),
                    lengthSelected);


            for (String employeeKey : employeeIDArrayList) {
                mDatabase.child("users").child(employeeKey).child("items").child(newItem.getID()).setValue(newItem)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                itemName.getEditText().setText("");
                                progressBar.setVisibility(View.GONE);

                                itemsArrayList.add(newItem);

//                                Toast.makeText(getActivity(), R.string.item_added_suc, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), R.string.add_item_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
