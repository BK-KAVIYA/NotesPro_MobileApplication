package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginbutton;
    ProgressBar progressBar;
    TextView createaccounttextbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.Email_edit_text);
        password = findViewById(R.id.password_edit_text);
        loginbutton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.Progress_Bar);
        createaccounttextbutton = findViewById(R.id.create_account_text_button);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress=email.getText().toString();
                String Userpassword=password.getText().toString();


                boolean isValidate=validateData(emailAddress,Userpassword);
                if(!isValidate){
                    return;
                }
                loginAccountInFirebase(emailAddress,Userpassword);
            }
        });

        createaccounttextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
            }
        });
    }
    void  loginAccountInFirebase(String Email,String Password){
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    //login success
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to main activity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else{
                        //failed to log
                        Utility.showToast(LoginActivity.this,"Email not verified, Please verify your email.");

                    }
                }else{
                    //login fail
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });

    }
    boolean validateData(String emailAddress,String Userpassword){
        //validate the data

        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            email.setError("Email is Invalid");
            return false;
        }if(Userpassword.length()<6) {
            password.setError("Password Length is Invalid");
            return false;
        }
        return  true;
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginbutton.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginbutton.setVisibility(View.VISIBLE);
        }

    }
}
