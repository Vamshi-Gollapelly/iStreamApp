package com.vamshigollapelly.istreamapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.vamshigollapelly.istreamapp.data.AppDatabase;
import com.vamshigollapelly.istreamapp.data.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If already logged in, skip to home
        SessionManager session = new SessionManager(this);
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        AppDatabase db = AppDatabase.getDatabase(this);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                User user = db.appDao().login(username, password);
                runOnUiThread(() -> {
                    if (user != null) {
                        session.saveUser(user.userId, user.username);
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        findViewById(R.id.btnGoSignUp).setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class))
        );
    }
}