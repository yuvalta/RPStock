package com.example.rpstock;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rpstock.Objects.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInfoDialog extends Dialog {

    private EditText emailET;
    private EditText passwordET;
    private EditText nameET;
    private EditText phoneET;
    private Button signinButton;
    private CheckBox isAdminCB;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    Employee oldEmployee;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

        emailET = findViewById(R.id.enter_email_ET);
        passwordET = findViewById(R.id.enter_password_ET);
        nameET = findViewById(R.id.enter_name_ET);
        phoneET = findViewById(R.id.enter_phone_ET);
        signinButton = findViewById(R.id.signin_button);
        isAdminCB = findViewById(R.id.is_admin_CB);

        progressBar = findViewById(R.id.progressbar);
        signinButton.setOnClickListener(signInClick);

        setTextInET();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void setTextInET() {
        emailET.setText(oldEmployee.getEmail());
        passwordET.setText(oldEmployee.getPassword());
        nameET.setText(oldEmployee.getName());
        phoneET.setText(oldEmployee.getPhone());
        isAdminCB.setChecked(oldEmployee.isAdmin());

    }

    public UserInfoDialog(@NonNull Context context) {
        super(context);
    }

    public UserInfoDialog(Context context, Employee oldEmployee) {
        super(context);
        this.oldEmployee = oldEmployee;
        this.context = context;
    }

    View.OnClickListener signInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);

            final Employee updateEmployee = new Employee(oldEmployee.getID(),
                    nameET.getText().toString(),
                    emailET.getText().toString(),
                    passwordET.getText().toString(),
                    phoneET.getText().toString(),
                    isAdminCB.isChecked());

            mDatabase.child("users").child(updateEmployee.getID()).setValue(updateEmployee).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Failed, try again!!", Toast.LENGTH_SHORT).show();
                }
            });


        }
    };
}
