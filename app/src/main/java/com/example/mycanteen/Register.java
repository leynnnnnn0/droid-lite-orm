package com.example.mycanteen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mycanteen.model.User;
import com.example.mycanteen.service.CurrentUser;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText username, email, password;
    TextView usernameError, emailError, passwordError, goToLoginPage;
    Button register;
    User userDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if(CurrentUser.getCurrentUserId(this) != -1)
            startActivity(new Intent(this, MainActivity.class));

        username = findViewById(R.id.username);
        usernameError = findViewById(R.id.usernameError);
        email = findViewById(R.id.email);
        emailError = findViewById(R.id.emailError);
        password = findViewById(R.id.password);
        passwordError = findViewById(R.id.passwordError);
        register = findViewById(R.id.register);
        userDb = new User(this);
        goToLoginPage = findViewById(R.id.goToLoginPage);

        goToLoginPage.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
        });


        register.setOnClickListener(v -> {
            String userUsername= username.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            usernameError.setText("");
            emailError.setText("");
            passwordError.setText("");

            if(userUsername.isEmpty()) usernameError.setText(R.string.required);
            if(userEmail.isEmpty()) emailError.setText(R.string.required);
            if(userPassword.isEmpty()) passwordError.setText(R.string.required);

            String uniqueEmail = "This email is already used." ;
            if(userDb.where("email", userEmail).get().getCount() != 0) emailError.setText(uniqueEmail);
            String uniqueUsername = "This username is already used.";
            if(userDb.where("username", userUsername).get().getCount() != 0) usernameError.setText(uniqueUsername);

            if(!usernameError.getText().toString().isEmpty() ||
                    !emailError.getText().toString().isEmpty() ||
                    !passwordError.getText().toString().isEmpty()) return;

            Boolean result = userDb.create(new HashMap<>() {{
                put("username", userUsername);
                put("email", userEmail);
                put("password", userPassword);
            }});

            toast(result, "Registered Successfully.");
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void toast(Boolean result, String message)
    {
        if(result) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}