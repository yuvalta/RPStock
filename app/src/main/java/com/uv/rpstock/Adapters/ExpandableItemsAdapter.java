package com.uv.rpstock.Adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uv.rpstock.Objects.Employee;
import com.uv.rpstock.Objects.Item;
import com.uv.rpstock.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                    item.setNippleLength(entry.getValue().getNippleLength());
                    item.setSeq(entry.getValue().getSeq());
                    items.add(item);
                }
            }
            sortArrayBySeq();
        }

        private void sortArrayBySeq() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Comparator<Item> comparator = Comparator.comparing(Item::getSeq);
                comparator = comparator.thenComparing((Item strDiameter) -> Item.convertDiameter(strDiameter.getDiameter()));
                comparator = comparator.thenComparing((Item nippleLength) -> Item.convertLength(nippleLength.getNippleLength()));

                Stream<Item> personStream = items.stream().sorted(comparator);

                items = (ArrayList<Item>) personStream.collect(Collectors.toList());
            }
        }

        private void inflateEmployeesList() {
            allItemsRecycler.setHasFixedSize(true);

            if (layoutManager == null) {
                layoutManager = new GridLayoutManager(itemView.getContext(), 2);
            }
            allItemsRecycler.setLayoutManager(layoutManager);

            if (mAdapter == null) {
                mAdapter = new UserStockListAdapter(items, employee, isAdmin);

            }
            allItemsRecycler.setAdapter(mAdapter);
        }
    }
}
