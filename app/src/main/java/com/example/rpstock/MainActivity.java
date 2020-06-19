package com.example.rpstock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    private String ADMIN_EMAIL = "tamir.yuval1@gmail.com";
    private FirebaseAuth mAuth;

    private TextView userNameTV;
    private String userEmail;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Query query;
    Employee currentEmployeeUser;

    private DatabaseReference usersDatabaseRef;
    private FirebaseUser currentUser;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFragmentRoutine();

        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail();


        getCurrentUserDataFromDB();

        userNameTV = findViewById(R.id.user_name);
    }

    private void getCurrentUserDataFromDB() {
        usersDatabaseRef = database.getReference("users");
        usersDatabaseRef.orderByChild("email").equalTo(userEmail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot != null) {
                    currentEmployeeUser = dataSnapshot.getValue(Employee.class);
                    userNameTV.setText("Hi " + currentEmployeeUser.getName());
                }
                else {
                    currentEmployeeUser = new Employee();
                    currentEmployeeUser.setAdmin(true);
                }

                Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                setSupportActionBar(myToolbar);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (currentEmployeeUser.isAdmin() || currentUser.equals(ADMIN_EMAIL)) {
            getMenuInflater().inflate(R.menu.admin_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.employee_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void startFragmentRoutine() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_frame, new ItemsListFragment(), "home");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home: {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_frame, new ItemsListFragment(), "home").commit();

                return true;
            }

            case R.id.menu_manage: {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_frame, new ManageEmployeesFragment(), "manage_employees").commit();

                return true;
            }

            case R.id.menu_items: {

                return true;
            }
            case R.id.menu_employees_stock: {

                return true;
            }

            case R.id.log_out: {
                currentEmployeeUser = null;
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }
}
