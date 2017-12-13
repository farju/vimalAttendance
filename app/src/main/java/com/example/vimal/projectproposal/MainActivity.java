package com.example.vimal.projectproposal;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText mUserNameField;
    private TextView mStatusTextView;
    private EditText mPasswordField;
    private DatabaseReference mDatabase;
    Uri data;
    String[] tokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();


        mStatusTextView = (TextView) findViewById(R.id.status);
        mUserNameField = (EditText) findViewById(R.id.usernameinput);
        mPasswordField = (EditText) findViewById(R.id.passwordinput);

        Intent intent = getIntent();
        data = intent.getData();
        if (data!= null) {
            tokens = data.toString().split("/");
        }


        findViewById(R.id.login).setOnClickListener(this);
        //This has to be placed in either RegistrationActivity or figure out how to add register on this page
        //findViewById(R.id.email_create_account_button).setOnClickListener(this);

        Button student = (Button) findViewById(R.id.register);
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                if (data!=null) {
                    Log.d("data", tokens[3]);
                    registerIntent.putExtra("data_", tokens[3]);
                }
                startActivity(registerIntent);
            }
        });
        
    }


    private void signIn(String email, String password) {
        Log.d("signin", "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signInSuccess", "signInWithUsername:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signInFail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
//SIGN OUT PORTION
    private void signOut() {
        mAuth.signOut();
        updateUI(null);
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
            mDatabase = FirebaseDatabase.getInstance().getReference();

            Intent loginIntent = new Intent(this, TeacherInitialScreen.class);
            //package token/uid into intent and send it with setExtra method
            loginIntent.putExtra("UID", user.getUid());

            if (data!=null) {
                Log.d("login", tokens[3]);
                mDatabase.child("users").child(user.getUid()).child("classList").push().setValue(tokens[3]);
                //Adds the student to the class's student list
                mDatabase.child("classes").child(tokens[3]).child("studentList").push().setValue(user.getUid());
            }
            startActivity(loginIntent);
        } else {
            findViewById(R.id.usernamePasswordLogin).setVisibility(View.VISIBLE);
        }
    }

    @Override
    //This function's purpose is to call specific functions based on which button is clicked.
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.login) {
            signIn(mUserNameField.getText().toString(), mPasswordField.getText().toString());
        }
    }
}
