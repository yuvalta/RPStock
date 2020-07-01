package com.uv.rpstock.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uv.rpstock.Adapters.ExpandableItemsAdapter;
import com.uv.rpstock.Objects.Employee;
import com.uv.rpstock.Objects.Item;
import com.uv.rpstock.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemsListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<String> itemsHeader = new ArrayList<>();
    private Employee employee;
    private RecyclerView employessList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private boolean isAdmin;


    public ItemsListFragment() {
    }

    public ItemsListFragment(Employee _employee, boolean _isAdmin) {
        employee = _employee;
        isAdmin = _isAdmin;
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

        getListOfItemsFromEmployee(employee);
        inflateEmployeesList();

        return v;
    }

    private void getListOfItemsFromEmployee(Employee employee) {
        for (Map.Entry<String, Item> entry : employee.getItems().entrySet()) {
            Item item = new Item();
            item.setAmount(entry.getValue().getAmount());
            item.setName(entry.getValue().getName());
            item.setID(entry.getKey());
            item.setKind(entry.getValue().getKind());
            item.setDiameter(entry.getValue().getDiameter());
            item.setSeq(entry.getValue().getSeq());
            item.setNippleLength(entry.getValue().getNippleLength());
            items.add(item);

        }
        sortHeaderArrayBySeq();
        makeDistinct();
    }

    private void makeDistinct() {

        for (Item i : items) {
            if (!itemsHeader.contains(i.getName())) {
                itemsHeader.add(i.getName());
            }
        }
    }

    private void sortHeaderArrayBySeq() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Comparator<Item> comparator = Comparator.comparing(Item::getSeq);
            comparator = comparator.thenComparing((Item strDiameter) -> Item.convertDiameter(strDiameter.getDiameter()));
            comparator = comparator.thenComparing((Item nippleLength) -> Item.convertLength(nippleLength.getNippleLength()));

            Stream<Item> personStream = items.stream().sorted(comparator);

            items = (ArrayList<Item>) personStream.collect(Collectors.toList());
        }
    }

    private void inflateEmployeesList() {
        employessList.setHasFixedSize(true);

        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getContext());
            employessList.setLayoutManager(layoutManager);
        }

        if (mAdapter == null) {
            mAdapter = new ExpandableItemsAdapter(itemsHeader, employee, isAdmin);
        }
        employessList.setAdapter(mAdapter);
    }
}
