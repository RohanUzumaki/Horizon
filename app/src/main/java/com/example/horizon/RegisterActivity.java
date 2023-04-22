package com.example.horizon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText edEmail, edPassword;
    EditText edConfirmPassword, edFirstName, edLastName, edPhone;
    Button btn;
    TextView tv;
    String emailPattern = "[a-zA-Z0-9_.]+@[a-z]+\\.+[a-z]+";

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://horizon-61340-default-rtdb.firebaseio.com/");
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edEmail = findViewById(R.id.editTextRegEmailAddress);
        edPassword = findViewById(R.id.editTextRegPassword);
        edConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        edFirstName = findViewById(R.id.editTextFirstName);
        edLastName = findViewById(R.id.editTextLastName);
        edPhone = findViewById(R.id.editTextPhone);
        btn = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewExistingUser);

        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();


        tv.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        btn.setOnClickListener(view -> perForAuth());
    }

    private void perForAuth() {
        String email=edEmail.getText().toString().trim();
        String password=edPassword.getText().toString().trim();
        String confirm_password=edConfirmPassword.getText().toString().trim();
        String FirstName=edFirstName.getText().toString().trim();
        String LastName=edLastName.getText().toString().trim();
        String Phone=edPhone.getText().toString().trim();


        if(!email.matches(emailPattern))
        {
            edEmail.setError(("Enter Correct Email"));
        } else if (password.isEmpty()  || password.length()<6)
        {
            edPassword.setError("Enter Proper Password");
        } else if (!password.equals(confirm_password))
        {
            edConfirmPassword.setError("Password Doesn't Match Both Fields");
        } else
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    Patient patient = new Patient(FirstName,LastName,Phone,email);
                    myRef.child("Patient").child("Rohan").setValue(patient).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                {

                    Toast.makeText(RegisterActivity.this, ""+task.getException(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}