package com.example.vimal.projectproposal;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText mUserNameField;
    private TextView mStatusTextView;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initilize connection to DB
        mAuth = FirebaseAuth.getInstance();

        //mStatusTextView = (TextView) findViewById(R.id.status);
        mUserNameField = (EditText) findViewById(R.id.usernameinput);
        mPasswordField = (EditText) findViewById(R.id.passwordinput);

        if(mAuth.getCurrentUser() != null){
            //valid user -> go to initial screen
            Log.d("Current User", mAuth.getCurrentUser().toString());
            Intent loginIntent = new Intent(this, TeacherInitialScreen.class);
            loginIntent.putExtra("UID", mAuth.getCurrentUser().getUid());
            Log.d("Current User", mAuth.getCurrentUser().getUid());
            startActivity(loginIntent);
        }else {
            Log.d("Current User", "is empty");
        }
        findViewById(R.id.login).setOnClickListener(this);
        //This has to be placed in either RegistrationActivity or figure out how to add register on this page
        //findViewById(R.id.email_create_account_button).setOnClickListener(this);

        Button new_user = (Button) findViewById(R.id.register);
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });
        
    }

    //signs in user
    private void signIn(String email, String password) {
        Log.d("signin", "signIn:" + email);
        if (!validateForm()) {
            //user login credentials are invalid
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signInSuccess", "signInWithUsername:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            login(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signInFail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //login(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }
//SIGN OUT
    private void signOut() {
        mAuth.signOut();
        //login(null);
    }
//EMAIL VERIFICATION PORTION
    /*
    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }
*/
    //validates user information
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

    private void login(FirebaseUser user) {
        if (user != null) {
            Log.d("Login","User is not null");
            Intent loginIntent = new Intent(this, TeacherInitialScreen.class);
            //package token/uid into intent and send it with setExtra method
            loginIntent.putExtra("UID", user.getUid());
            startActivity(loginIntent);
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
