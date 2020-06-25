package com.example.rpstock;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rpstock.Activities.MainActivity;
import com.example.rpstock.Objects.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoDialog extends Dialog {

    private static final String EMAIL_SUFFIX = "@RPS.com";

    private TextInputLayout emailET;
    private TextInputLayout passwordET;
    private TextInputLayout nameET;
    private TextInputLayout phoneET;
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
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

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
        emailET.getEditText().setText(oldEmployee.getEmail());
        passwordET.getEditText().setText(oldEmployee.getPassword());
        nameET.getEditText().setText(oldEmployee.getName());
        phoneET.getEditText().setText(oldEmployee.getPhone());
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

            if (passwordET.getEditText().getText().length() >= 6) {


                progressBar.setVisibility(View.VISIBLE);
                final Employee updateEmployee = new Employee(oldEmployee.getID(),
                        nameET.getEditText().getText().toString(),
                        addEmailToEmployeeID(emailET.getEditText().getText().toString()),
                        passwordET.getEditText().getText().toString(),
                        phoneET.getEditText().getText().toString(),
                        isAdminCB.isChecked());

                mDatabase.child("users").child(updateEmployee.getID()).setValue(updateEmployee).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), R.string.employee_updated, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "נא לבדוק שכתובת אימייל תקינה וסיסמא בעלת 6 תווים לפחות!", Toast.LENGTH_SHORT).show();
            }

        }
    };

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
