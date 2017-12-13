package com.example.vimal.projectproposal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                            //This is where we send the class id from login to registration
                            String data = (String) getIntent().getSerializableExtra("data_");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User userObject = new User(mEmailField.getText().toString(), mFirstNameField.getText().toString(), mLastNameField.getText().toString(), AccountType);
                            String userId = user.getUid();
                            try {
                                mDatabase.child("users").child(userId).setValue(userObject);
                                if (data != null) {
                                    mDatabase.child("users").child(userId).child("classList").push().setValue(data);
                                    mDatabase.child("classes").child(data).child("studentList").push().setValue(userId);
                                }
                            } catch (Exception e) {
                                Log.e("bad news", e.toString());
                            }
                            Intent registationIntent = new Intent(RegistrationActivity.this, TeacherInitialScreen.class);
                            registationIntent.putExtra("UID", userId);

                            startActivity(registationIntent);
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
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            mPasswordField.setError("Required and must be 6 or more characters.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText("WHATSUP!!");
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
