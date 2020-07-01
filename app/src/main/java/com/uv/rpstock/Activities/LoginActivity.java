package com.example.rpstock.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.rpstock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String EMAIL_SUFFIX = "@RPS.com";
    private FirebaseAuth mAuth;

    private TextInputLayout emailET;
    private TextInputLayout passwordET;

    private ProgressBar progressBar;

    private Button signinButton;
    private boolean showHidePasswordFlag = false;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }


        emailET = findViewById(R.id.enter_email_ET);
        passwordET = findViewById(R.id.enter_password_ET);
        signinButton = findViewById(R.id.signin_button);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        signinButton.setOnClickListener(signInClick);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }



    View.OnClickListener signInClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            if (passwordET.getEditText().getText().length() >= 6) {

                String emailFromID = addEmailToEmployeeID(emailET.getEditText().getText().toString());

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(emailFromID, passwordET.getEditText().getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    displayErrorSnackbar("משתמש לא נמצא!", v);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayErrorSnackbar("משתמש לא נמצא!", v);
                    }
                });
            } else {
                displayErrorSnackbar("נא לבדוק שכתובת אימייל תקינה וסיסמא בעלת 6 תווים לפחות!", v);
            }
        }
    };

    private void displayErrorSnackbar(String s, View v) {
        hideKeyboard(LoginActivity.this);
        Snackbar.make(v, s, Snackbar.LENGTH_LONG).show();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("userEmail", currentUser.getEmail());
        startActivity(intent);
    }

    public void showHidePassword(View view) {
        if (showHidePasswordFlag) {
            passwordET.getEditText().setTransformationMethod(new PasswordTransformationMethod());
            showHidePasswordFlag = false;
        } else {
            passwordET.getEditText().setTransformationMethod(null);
            showHidePasswordFlag = true;
        }
    }

    public String addEmailToEmployeeID(String number) {
        return (number.trim() + EMAIL_SUFFIX);
    }
}
