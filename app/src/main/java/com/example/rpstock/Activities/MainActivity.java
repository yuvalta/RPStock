package com.example.rpstock.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.rpstock.Fragments.AdminMainFragment;
import com.example.rpstock.Objects.Employee;
import com.example.rpstock.Fragments.ItemsListFragment;
import com.example.rpstock.Fragments.ManageEmployeesFragment;
import com.example.rpstock.R;
import com.google.android.material.navigation.NavigationView;
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

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    private ActionBarDrawerToggle t;

    private DatabaseReference usersDatabaseRef;
    private FirebaseUser currentUser;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                } else {
                    currentEmployeeUser = new Employee();
                    currentEmployeeUser.setAdmin(true);
                }

                setDrawerMenu();
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

    private void setDrawerMenu() {


//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        mNavigationView = (NavigationView) findViewById(R.id.navigation);
//
//
//
//        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, myToolbar,R.string.app_name,
//                R.string.app_name);
//
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (currentEmployeeUser.isAdmin() || currentUser.equals(ADMIN_EMAIL)) {
            getMenuInflater().inflate(R.menu.admin_menu, menu);
            startFragmentRoutine(true);
        } else {
            getMenuInflater().inflate(R.menu.employee_menu, menu);
            startFragmentRoutine(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void startFragmentRoutine(boolean isAdmin) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (isAdmin) {
            fragmentTransaction.replace(R.id.fragment_frame, new AdminMainFragment(), "home");
        }
        else {
            fragmentTransaction.replace(R.id.fragment_frame, new ItemsListFragment(), "home");
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home_user: {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_frame, new ItemsListFragment(), "home").commit();

                return true;
            }
            case R.id.menu_home_admin: {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_frame, new AdminMainFragment(), "admin_home").commit();

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
