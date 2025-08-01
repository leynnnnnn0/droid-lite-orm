package com.example.mycanteen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {
    EditText email, password;
    TextView emailError ,passwordError;
    Button login;
    User userDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        if(CurrentUser.getCurrentUserId(this) != -1)
            startActivity(new Intent(this, MainActivity.class));

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        };

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordError = findViewById(R.id.passwordError);
        emailError = findViewById(R.id.emailError);
        login = findViewById(R.id.login);
        userDb = new User(this);

        login.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            passwordError.setText("");
            emailError.setText("");

            if(userEmail.isEmpty()) emailError.setText(R.string.required);
            if(userPassword.isEmpty()) passwordError.setText(R.string.required);

            Cursor result = userDb.where("email", userEmail).where("password", userPassword).first();
            if(result.getCount() > 0) {
                result.moveToNext();

                toast(true, "Login successfully");
                User user = userDb.mapCursor(userDb.where("email", userEmail).first());
                CurrentUser.setCurrentUserId(this, user.getId());
                CurrentUser.setCurrentUserRole(this, user.getRole());
                startActivity(new Intent(this, MainActivity.class));




            }else{
                String notFound = "Incorrect Credentials.";
                passwordError.setText(notFound);
            }
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