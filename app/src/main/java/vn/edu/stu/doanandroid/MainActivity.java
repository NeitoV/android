package vn.edu.stu.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText txtUsername;
    private EditText txtPassword;
    private Button buttonLogin;
    private TextView validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                if(username.equals("admin") && password.equals("123456")) {
                    validate.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                } else {
                    validate.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void addControls() {
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        buttonLogin = findViewById(R.id.confirmLogin);
        validate = findViewById(R.id.validate);
    }


}