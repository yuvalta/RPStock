package com.example.rpstock.Adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class ExpandableUserStockAdapter extends RecyclerView.Adapter<ExpandableUserStockAdapter.MyViewHolder> {

    ArrayList<Employee> listEmployees = new ArrayList<>();

    public ExpandableUserStockAdapter(ArrayList<Employee> employeesNames) {
        this.listEmployees = employeesNames;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.all_user_header_item, parent, false);

        ExpandableUserStockAdapter.MyViewHolder vh = new ExpandableUserStockAdapter.MyViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(listEmployees.get(position));
    }

    @Override
    public int getItemCount() {
        return listEmployees.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private ImageView dropDownArrow;
        private LinearLayout openCloseLL;

        private ProgressBar progressBar;

        private RecyclerView allItemsRecycler;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager layoutManager;

        private DatabaseReference mDatabase;

        private ArrayList<Item> items = new ArrayList<>();

        public MyViewHolder(View view) {
            super(view);

            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        public void bind(final Employee employee) {
            progressBar = itemView.findViewById(R.id.add_item_progress_stock);
            progressBar.bringToFront();

            allItemsRecycler = itemView.findViewById(R.id.all_stock_header_recycler);

            openCloseLL = itemView.findViewById(R.id.open_close_stock_LL);

            dropDownArrow = itemView.findViewById(R.id.drop_down_arrow_stock);

            itemName = itemView.findViewById(R.id.all_user_header_title);
            itemName.setText(employee.getName());

            openCloseLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (allItemsRecycler.getVisibility() == View.GONE) {
                        allItemsRecycler.setVisibility(View.VISIBLE);

                        dropDownArrow.setImageResource(R.drawable.arrow_up);
                        getListOfItemsFromEmployeeName(employee);

                    } else {
                        dropDownArrow.setImageResource(R.drawable.arrow_down);
                        allItemsRecycler.setVisibility(View.GONE);
                        items.clear();
                    }
                }
            });


        }

        private void getListOfItemsFromEmployeeName(Employee userName) {

            progressBar.setVisibility(View.VISIBLE);
            mDatabase.child("users").child(userName.getID()).child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    items.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Item e = ds.getValue(Item.class);
                        items.add(e);

                    }
                    inflateEmployeesList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(itemView.getContext(), "Error in loading", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void inflateEmployeesList() {
            allItemsRecycler.setHasFixedSize(true);

            if (layoutManager == null) {
                layoutManager = new LinearLayoutManager(itemView.getContext());
            }
            allItemsRecycler.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(allItemsRecycler.getContext(),
                    DividerItemDecoration.VERTICAL);
            allItemsRecycler.addItemDecoration(dividerItemDecoration);

            if (mAdapter == null) {
                mAdapter = new AllUserStockListAdapter(items);

            }
            allItemsRecycler.setAdapter(mAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }
}
