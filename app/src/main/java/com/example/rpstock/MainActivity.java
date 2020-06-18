package com.example.rpstock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private String ADMIN_EMAIL = "tamir.yuval1@gmail.com";
    private FirebaseAuth mAuth;

    private TextView userNameTV;
    private String userEmail;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFragmentRoutine();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        userNameTV = findViewById(R.id.user_name);

        userNameTV.setText(userEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (userEmail.equals(ADMIN_EMAIL)) {
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
            case R.id.menu_home:

                if (checkIfFragmentExist("home")) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_frame, new ItemsListFragment(), "home").commit();
                }

                return true;

            case R.id.menu_manage:
                if (checkIfFragmentExist("manage_employees")) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_frame, new ManageEmployeesFragment(), "manage_employees").commit();
                }
                return true;

            case R.id.menu_items:

                return true;

            case R.id.menu_employees_stock:

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    private boolean checkIfFragmentExist(String tag) {
        Fragment fragmentA = fragmentManager.findFragmentByTag(tag);

        if (fragmentA == null) {
            return true;
        } else {
            return false;
        }
    }
}
