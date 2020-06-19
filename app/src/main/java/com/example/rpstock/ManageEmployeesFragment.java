package com.example.rpstock;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageEmployeesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageEmployeesFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private EditText emailET;
    private EditText passwordET;
    private EditText nameET;
    private EditText phoneET;
    private Button signinButton;
    private CheckBox isAdminCB;
    private ProgressBar progressBar;

    private RecyclerView employessList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

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
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        Employee e = new Employee();
                        e.setName(ds1.child("name").getValue(String.class));
                        e.setEmail(ds1.child("email").getValue(String.class));
                        e.setPassword(ds1.child("password").getValue(String.class));
                        employeeArrayList.add(e);
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

        layoutManager = new LinearLayoutManager(getContext());
        employessList.setLayoutManager(layoutManager);

        mAdapter = new EmployeesAdapter(employeeArrayList);
        employessList.setAdapter(mAdapter);
    }

    View.OnClickListener signInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);

            final Employee newEmployee = new Employee(nameET.getText().toString(),
                    emailET.getText().toString(),
                    passwordET.getText().toString(),
                    phoneET.getText().toString(),
                    isAdminCB.isChecked());

            mAuth.createUserWithEmailAndPassword(newEmployee.getEmail(),
                    newEmployee.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    mDatabase.child("users").child(newEmployee.getPhone()).setValue(newEmployee);

                    FirebaseAuth.getInstance().signOut();

                    progressBar.setVisibility(View.INVISIBLE);
                }
            });


        }
    };

}
