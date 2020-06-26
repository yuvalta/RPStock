package com.example.rpstock.Adapters;

import android.opengl.Visibility;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Objects.Item;
import com.example.rpstock.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class ExpandableItemsAdapter extends RecyclerView.Adapter<ExpandableItemsAdapter.MyViewHolder> {

    ArrayList<String> listItemsDistinct = new ArrayList<>();
    private Employee employee;
    private boolean isAdmin;

    public ExpandableItemsAdapter(ArrayList<String> listItemsDistinct, Employee _employee, boolean _isAdmin) {
        this.listItemsDistinct = listItemsDistinct;
        employee = _employee;
        isAdmin = _isAdmin;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.items_header_item, parent, false);

        ExpandableItemsAdapter.MyViewHolder vh = new ExpandableItemsAdapter.MyViewHolder(listItem, employee, isAdmin);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(listItemsDistinct.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listItemsDistinct.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private ImageView dropDownArrow;
        private LinearLayout openCloseLL;

        private Employee employee;
        private boolean isAdmin;

        private RecyclerView allItemsRecycler;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager layoutManager;

        private ArrayList<Item> items = new ArrayList<>();

        public MyViewHolder(View view, Employee _employee, boolean _isAdmin) {
            super(view);
            employee = _employee;
            isAdmin = _isAdmin;
        }

        public void bind(final String item, final int position) {

            allItemsRecycler = itemView.findViewById(R.id.items_header_recycler);

            openCloseLL = itemView.findViewById(R.id.open_close_items_LL);

            dropDownArrow = itemView.findViewById(R.id.drop_down_arrow);

            itemName = itemView.findViewById(R.id.items_header_title);
            itemName.setText(item);

            openCloseLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (allItemsRecycler.getVisibility() == View.GONE) {
                        allItemsRecycler.setVisibility(View.VISIBLE);

                        dropDownArrow.setImageResource(R.drawable.arrow_up);
                        getListOfItemsFromEmployee(employee, item);
                        inflateEmployeesList();
                    } else {
                        dropDownArrow.setImageResource(R.drawable.arrow_down);
                        allItemsRecycler.setVisibility(View.GONE);
                        items.clear();
                    }
                }
            });



        }

        private void getListOfItemsFromEmployee(Employee employee, String headerName) {
            for (Map.Entry<String, Item> entry : employee.getItems().entrySet()) {
                if (entry.getValue().getName().equals(headerName)) {
                    Item item = new Item();
                    item.setAmount(entry.getValue().getAmount());
                    item.setName(entry.getValue().getName());
                    item.setID(entry.getKey());
                    item.setKind(entry.getValue().getKind());
                    item.setDiameter(entry.getValue().getDiameter());
                    items.add(item);
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                items.sort(new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        String x1 = o1.getKind();
                        String x2 = o2.getKind();
                        int sComp = x1.compareTo(x2);

                        if (sComp != 0) {
                            return sComp;
                        }

                        String x3 = (o1.getDiameter());
                        String x4 = (o2.getDiameter());
                        return x3.compareTo(x4);
                    }
                });
            }
        }

        private void inflateEmployeesList() {
            allItemsRecycler.setHasFixedSize(true);

            if (layoutManager == null) {
                layoutManager = new GridLayoutManager(itemView.getContext(),2);
            }
            allItemsRecycler.setLayoutManager(layoutManager);

            if (mAdapter == null) {
                mAdapter = new UserStockListAdapter(items, employee, isAdmin);

            }
            allItemsRecycler.setAdapter(mAdapter);
        }
    }
}
