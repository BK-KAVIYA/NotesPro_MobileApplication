package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    EditText email, password, confirmpassword;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email = findViewById(R.id.Email_edit_text);
        password = findViewById(R.id.password_edit_text);
        confirmpassword = findViewById(R.id.Confirm_Password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_button);
        progressBar = findViewById(R.id.Progress_Bar);
        loginButton = findViewById(R.id.loging_text_view_button);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress=email.getText().toString();
                String Userpassword=password.getText().toString();
                String Userconfirmpassword=confirmpassword.getText().toString();

                boolean isValidate=validateData(emailAddress,Userpassword,Userconfirmpassword);
                if(!isValidate){
                    return;
                }
                createAccountInFirebase(emailAddress,Userpassword);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

   /* void createAccount() {

    }*/

    boolean validateData(String emailAddress,String Userpassword,String Userconfirmpassword){
        //validate the data

        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            email.setError("Email is Invalid");
            return false;
        }if(Userpassword.length()<6){
            password.setError("Password Length is Invalid");
            return  false;
        }if(!Userpassword.equals(Userconfirmpassword)){
            confirmpassword.setError("Password Not Matched");
            return false;
        }
        return  true;
    }

    void createAccountInFirebase(String emailAddress,String Userpassword){
        changeInProgress(true);
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailAddress,Userpassword).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
               if(task.isSuccessful()){
                   //creation is done
                   Toast.makeText(CreateAccountActivity.this,"Succesfully create account, Check email to verfy",Toast.LENGTH_SHORT).show();
                   firebaseAuth.getCurrentUser().sendEmailVerification();
                   firebaseAuth.signOut();
                   finish();
               }else{
                   //failure
                   Toast.makeText(CreateAccountActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();


               }
            }
        });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }

    }
}
