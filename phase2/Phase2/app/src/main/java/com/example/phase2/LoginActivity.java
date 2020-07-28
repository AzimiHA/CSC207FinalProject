package com.example.phase2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phase2.phase2.AdminActions;
import com.example.phase2.phase2.TraderManager;

public class LoginActivity extends AppCompatActivity {
    private AdminActions adminActions;
    private TraderManager traderManager;

    private int nextSystem;
    private String nextUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        traderManager = (TraderManager) bundle.get("TraderManager");
        adminActions = (AdminActions) bundle.get("AdminActions");
    }

    public void onLoginClicked(View view){
        EditText userEditText = findViewById(R.id.editTextTextPersonName);
        EditText passEditText = findViewById(R.id.editTextTextPassword);
        String username = userEditText.getText().toString();
        String password = passEditText.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("NextUser", username);

        if (traderManager.login(username, password)){
            intent.putExtra("NextSystem", 2);
            startActivity(intent);
        }
        else if (adminActions.checkCredentials(username, password)){
            intent.putExtra("NextSystem", 3);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, R.string.login_error, Toast.LENGTH_LONG).show();
        }
    }

    public void onSignupClicked(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("AdminActions", adminActions);
        intent.putExtra("TraderManager", traderManager);
        startActivity(intent);
    }
}
