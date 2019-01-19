package com.bilkent.subfly.getout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.*;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button login;
    private EditText email;
    private EditText passwordEdit2ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_layout);

        login = findViewById(R.id.logInButton);
        email = findViewById(R.id.emailArea);
        passwordEdit2ID = findViewById(R.id.passwordEdit);


        //Firebase
        mAuth = FirebaseAuth.getInstance();


        //Listeners
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredEmail = email.getText().toString();
                String bilkentMail = "@ug.bilkent.edu.tr";
                boolean doesContain = false;
                // Check whether the mail that was entered is bilkentMail or not
                for (int i = 0; i <= enteredEmail.length() - bilkentMail.length(); i++) {
                    if (enteredEmail.substring(i, i + bilkentMail.length()).equals(bilkentMail)) {
                        doesContain = true;
                    }
                }
                if( doesContain)
                {
                    String enteredPassword = passwordEdit2ID.getText().toString();
                    mAuth.signInWithEmailAndPassword(enteredEmail, enteredPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //User user = new User(email.getContext().toString());
                            if (task.isSuccessful()) {
                                //String id = databaseReference.push().getKey();
                                //databaseReference.child(id).setValue(user);
                                Toast.makeText(getApplication(), "Successful sign in!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplication(), "Sign in unsuccessful!", Toast.LENGTH_LONG).show();
                            }
                            // ...
                        }
                    });
                }
                else
                    Toast.makeText(LoginActivity.this, "Enter a bilkent email!", Toast.LENGTH_LONG).show();

            }
        });

        //Auth import
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "user signed in");
                } else
                    Log.d(TAG, "user signed out");
            }
        };
    }
}
