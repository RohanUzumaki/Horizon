package com.example.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText edEmail, edPassword;
    Button btn;
    TextView tv;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.editTextLoginEmailAddress);
        edPassword = findViewById(R.id.editTextLoginPassword);
        btn = findViewById(R.id.buttonLogin);
        tv = findViewById(R.id.textViewNewUser);

        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();



        btn.setOnClickListener(view -> userLogin());
        tv.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void userLogin() {
        String EditTextEmail = edEmail.getText().toString().trim();
        String EditTextPassword = edPassword.getText().toString().trim();

        if (EditTextEmail.isEmpty()){
            edEmail.setError("Email is required");
            edEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(EditTextEmail).matches()){
            edEmail.setError("Enter a valid Email");
            edEmail.requestFocus();
            return;
        }
        if(EditTextPassword.isEmpty()){
            edPassword.setError("Password is required");
            edPassword.requestFocus();
            return;
        }
        if (EditTextPassword.length() < 8){
            edPassword.setError("Password length must be at least 8 characters");
            edPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(EditTextEmail,EditTextPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                sendUserToNextActivity();
            }
            else{
                Toast.makeText(LoginActivity.this,"Check your credentials", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}

