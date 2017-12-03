package com.example.vimal.projectproposal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mUserNameField;
    private EditText mPasswordField;
    private EditText mEmailField;
    private RadioGroup radioGroup;
    private TextView mStatusTextView;
    private String AccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        mStatusTextView = (TextView) findViewById(R.id.status);
        mUserNameField = (EditText) findViewById(R.id.UsernameInput);
        mPasswordField = (EditText) findViewById(R.id.Password);
        mFirstNameField = (EditText) findViewById(R.id.firstNameInput);
        mLastNameField = (EditText) findViewById(R.id.LastNameInput);
        mEmailField = (EditText) findViewById(R.id.Email);
        radioGroup = (RadioGroup) findViewById(R.id.Type);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Teacher:
                        AccountType = "Teacher";
                        break;
                    case R.id.Student:
                        AccountType = "Student";
                        break;
                }
            }
        });

        findViewById(R.id.register).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //CREATION OF ACCOUNT AND HAS TO FIGURE OUT HOW TO CALL THIS IN REGISTRATION ACTIVITY.JAVA
    private void createAccount(String email, String password, String first, String last, String user, String type) {
        Log.d("Create", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("createuserSuccess", "createUserWithUsername:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("createuserFailed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String username = mUserNameField.getText().toString();
        if (TextUtils.isEmpty(username)) {
            mUserNameField.setError("Required.");
            valid = false;
        } else {
            mUserNameField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            //findViewById(R.id.usernamePasswordLogin).setVisibility(View.GONE);
            //findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            //findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            //findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

            //findViewById(R.id.usernamePasswordLogin).setVisibility(View.VISIBLE);
            //findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            //findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    @Override
    //This function's purpose is to call specific functions based on which button is clicked.
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register) {
            if (AccountType!=null) {
                if (AccountType.equals("Teacher")) {
                    createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString(), mFirstNameField.getText().toString(), mLastNameField.getText().toString(), mUserNameField.getText().toString(), AccountType);
                } else if (AccountType.equals("Student")) {
                    createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString(), mFirstNameField.getText().toString(), mLastNameField.getText().toString(), mUserNameField.getText().toString(), AccountType);
                }
            }
        } /*else if (i == R.id.login) {
            signIn(mUserNameField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_out_button) {
        //Potential sign out portion
            signOut();
        } else if (i == R.id.verify_email_button) {
        //Potential email verification portion
            sendEmailVerification();
        }*/
    }
}