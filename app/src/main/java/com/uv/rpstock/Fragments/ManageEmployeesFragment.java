package com.uv.rpstock.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uv.rpstock.Adapters.EmployeesAdapter;
import com.uv.rpstock.Objects.Employee;
import com.uv.rpstock.Objects.Item;
import com.uv.rpstock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageEmployeesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String REFERENCE_EMPLOYEE_ID = "0101";
    private static final String EMAIL_SUFFIX = "@RPS.com";

    private String mParam1;
    private String mParam2;

    private TextInputLayout emailET;
    private TextInputLayout passwordET;
    private TextInputLayout nameET;
    private TextInputLayout phoneET;
    private Button signinButton;
    private CheckBox isAdminCB;
    private ProgressBar progressBar;

    private Employee newEmployee = new Employee();

    private RecyclerView employessList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
    private FirebaseDatabase database;

    private ArrayList<Employee> employeeArrayList = new ArrayList<>();

    public ArrayList<Employee> getEmployeeArrayList() {
        return employeeArrayList;
    }

    public ManageEmployeesFragment() {
    }

    public static ManageEmployeesFragment newInstance(String param1, String param2) {
        ManageEmployeesFragment fragment = new ManageEmployeesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_employees, container, false);

        emailET = view.findViewById(R.id.enter_email_ET);
        passwordET = view.findViewById(R.id.enter_password_ET);
        nameET = view.findViewById(R.id.enter_name_ET);
        phoneET = view.findViewById(R.id.enter_phone_ET);
        signinButton = view.findViewById(R.id.signin_button);
        isAdminCB = view.findViewById(R.id.is_admin_CB);

        progressBar = view.findViewById(R.id.progressbar);
        signinButton.setOnClickListener(signInClick);
        employessList = view.findViewById(R.id.employees_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getListFromDB();


        return view;
    }

    private void getListFromDB() {
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        Employee e = ds1.getValue(Employee.class);

                        if (!e.getID().equals(REFERENCE_EMPLOYEE_ID)) { // insert all users accept items reference user
                            employeeArrayList.add(e);
                        }
                    }
                }
                inflateEmployeesList();
                progressBar.setVisibility(View.GONE);
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
            layoutManager = new LinearLayoutManager(getContext());
            employessList.setLayoutManager(layoutManager);
        }

        if (mAdapter == null) {
            mAdapter = new EmployeesAdapter(employeeArrayList);
        }
        employessList.setAdapter(mAdapter);
    }

    View.OnClickListener signInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (passwordET.getEditText().getText().length() >= 6) {

                progressBar.setVisibility(View.VISIBLE);

                mDatabase.child("users").child(REFERENCE_EMPLOYEE_ID).child("items").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String, Item> map = new HashMap<>();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String id = ds.getKey();
                            Item item = ds.getValue(Item.class);

                            map.put(id, item);
                        }
                        newEmployee.setItems(map);

                        setEmployeeProps();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                displayErrorSnackbar("נא לבדוק שכתובת אימייל תקינה וסיסמא בעלת 6 תווים לפחות!", v);
            }
        }

    };

    private void setEmployeeProps() {

        newEmployee.setAdmin(isAdminCB.isChecked());
        newEmployee.setEmail(addEmailToEmployeeID(emailET.getEditText().getText().toString()));
        newEmployee.setName(nameET.getEditText().getText().toString());
        newEmployee.setPassword(passwordET.getEditText().getText().toString());
        newEmployee.setPhone(phoneET.getEditText().getText().toString());

        mAuth.createUserWithEmailAndPassword(newEmployee.getEmail(),
                newEmployee.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                newEmployee.setID(mAuth.getCurrentUser().getUid());
                mDatabase.child("users").child(newEmployee.getID()).setValue(newEmployee);

                FirebaseAuth.getInstance().signOut();

                progressBar.setVisibility(View.GONE);

                employeeArrayList.add(newEmployee);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                displayErrorSnackbar("תקלה", getView());
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                displayErrorSnackbar("משתמש נוסף בהצלחה", getView());


                resetAllET();
            }
        });
    }

    private void resetAllET() {
        emailET.getEditText().setText("");
        nameET.getEditText().setText("");
        phoneET.getEditText().setText("");
        passwordET.getEditText().setText("");
        isAdminCB.setChecked(false);
    }

    private void displayErrorSnackbar(String s, View v) {
        hideKeyboard(getActivity());
        Snackbar.make(v, s, Snackbar.LENGTH_LONG).show();
    }

    public String addEmailToEmployeeID(String number) {
        return (number.trim() + EMAIL_SUFFIX);
    }

    public String getIDFromEmail(String number) {
        int index = number.indexOf('@');
        return number.substring(0, index);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
