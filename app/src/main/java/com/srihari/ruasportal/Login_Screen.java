package com.srihari.ruasportal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pl.droidsonroids.gif.GifImageView;

public class Login_Screen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText Username, Password;
    private Button Login;
    private GifImageView loading;
    private String Name, Registration, Branch;
    private boolean exit_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);
        Username = findViewById(R.id.Email_text);   Username.setText("");
        Password = findViewById(R.id.Password_text);  Password.setText("");
        Login = findViewById(R.id.Login_Button);
        loading = findViewById(R.id.loader);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        loading.setVisibility(View.GONE);

        if(mAuth.getCurrentUser() != null) startActivity(new Intent(Login_Screen.this, Dashboard.class));

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Username.getText().toString();
                String pass = Password.getText().toString();

                if(name.isEmpty()) {
                    Toast.makeText(Login_Screen.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(name).matches()) {
                    Toast.makeText(Login_Screen.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(pass.isEmpty()) {
                    Toast.makeText(Login_Screen.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(pass.length() < 5){
                    Toast.makeText(Login_Screen.this, "Password Too Short", Toast.LENGTH_SHORT).show();
                    return;
                }

                loading.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(name, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Login_Screen.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    loading.setVisibility(View.GONE);
                                    startActivity(new Intent(Login_Screen.this, Dashboard.class));
                                }
                                else
                                {
                                    Toast.makeText(Login_Screen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    loading.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }



    @Override
    public void onBackPressed() {
        if(exit_flag){
            exit_flag = false;
            Toast.makeText(this, "Press Back again to leave", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            exit_flag = true;
        }
    }
}
