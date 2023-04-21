package com.example.horizon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText edEmail, edPassword;
    EditText edConfirmPassword, edFirstName, edLastName, edPhone;
    Button btn;
    TextView tv;
    String emailPattern = "[a-zA-Z0-9_-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

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
        progressDialog=new ProgressDialog(this );
        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String confirm_password = edConfirmPassword.getText().toString();
                String first_name = edFirstName.getText().toString();
                String last_name = edLastName.getText().toString();
                String phone = edPhone.getText().toString();
                if (email.length() == 0 || password.length() == 0 || confirm_password.length() == 0 || first_name.length() == 0 || last_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
                } else {
                   if(password.compareTo(confirm_password)==0){
                       if(isValid(password)){



                           Toast.makeText(getApplicationContext(),"Record Inserted",Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                       }
                       else{
                           Toast.makeText(getApplicationContext(),"Password must contain at least 8 characters, having letter, digit and special symbol",Toast.LENGTH_SHORT).show();
                       }
                   }
                   else{
                       Toast.makeText(getApplicationContext(),"Password and Confirm Password does not match", Toast.LENGTH_SHORT).show();
                       PerforAuth();
                   }

                }
            }
        });
    }

    private void PerforAuth() {
        String email=edEmail.getText().toString();
        String password=edPassword.getText().toString();
        String  confirm_password=edConfirmPassword.getText().toString();
        String FirstName=edFirstName.getText().toString();
        String LastName=edLastName.getText().toString();


        if(!email.matches(emailPattern))
        {
            edEmail.setError(("Enter Correct Email"));

        } else if (password.isEmpty()  || password.length()<6)
        {
            edPassword.setError("Enter Proper Password");
        } else if (password.equals(confirm_password))
        {
            edConfirmPassword.setError("Password Doesn't Match Both Fields");
        } else
        {
            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(),Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }


    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public static boolean isValid(String passwordhere){
        int f1=0,f2=0,f3=0;
        if(passwordhere.length()<0){
            return false;
        }
        else{
            for(int p=0; p<passwordhere.length(); p++){
                if(Character.isLetter(passwordhere.charAt(p))){
                    f1=1;
                }
            }
            for(int r=0; r< passwordhere.length(); r++){
                if(Character.isDigit(passwordhere.charAt(r))){
                    f2=1;
                }
            }
            for(int s=0; s < passwordhere.length(); s++){
                char c = passwordhere.charAt(s);
                if(c>=33&&c<=46||c==64){
                    f3=1;
                }
            }
            if(f1==1 && f2==1 && f3==1)
                return true;
            return false;
        }
    }
}