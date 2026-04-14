package com.vamshigollapelly.istreamapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.vamshigollapelly.istreamapp.data.AppDatabase;
import com.vamshigollapelly.istreamapp.data.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText etFullName = findViewById(R.id.etFullName);
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirm = findViewById(R.id.etConfirmPassword);
        AppDatabase db = AppDatabase.getDatabase(this);

        findViewById(R.id.btnCreateAccount).setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm = etConfirm.getText().toString().trim();

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                User existing = db.appDao().getUserByUsername(username);
                if (existing != null) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }
                db.appDao().insertUser(new User(fullName, username, password));
                runOnUiThread(() -> {
                    Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                });
            });
        });
    }
}