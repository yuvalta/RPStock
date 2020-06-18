package com.example.rpstock;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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
    private Button signinButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    public ManageEmployeesFragment() {
        // Required empty public constructor
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
        signinButton = view.findViewById(R.id.signin_button);

        progressBar = view.findViewById(R.id.progressbar);
        signinButton.setOnClickListener(signInClick);



        return view;
    }

    View.OnClickListener signInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });



        }
    };

}
