package com.blindstick.Admin;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blindstick.R;

public class AdminLogin extends AppCompatActivity {
    Button btnlogin;
    EditText edtpass,edtemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        btnlogin=(Button) findViewById(R.id.btnlogin);
        edtpass=(EditText) findViewById(R.id.edtpass);
        edtemail=(EditText) findViewById(R.id.edtemail);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtemail.getText().toString().trim();
                String password = edtpass.getText().toString().trim();

                if (email.equals("admin@gmail.com") && password.equals("admin")) {
                    startActivity(new Intent(getApplicationContext(), AdminHome.class));
                    Toast.makeText(AdminLogin.this, "Welcome admin", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Snackbar.make(view, "Invalid credentials", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
