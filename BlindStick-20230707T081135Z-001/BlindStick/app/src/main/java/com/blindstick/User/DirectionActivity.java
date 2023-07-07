package com.blindstick.User;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blindstick.R;

public class DirectionActivity extends AppCompatActivity {

    Button btnsearch;
    EditText edtsrc,edtdst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        edtsrc = (EditText)findViewById(R.id.edtsrc);
        edtdst = (EditText)findViewById(R.id.edtdst);
        btnsearch = (Button) findViewById(R.id.btnsearch);

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = edtsrc.getText().toString();
                String dest = edtdst.getText().toString();

                if (TextUtils.isEmpty(dest)){
                    Toast.makeText(DirectionActivity.this, "Please enter destination address !", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?f=d&hl=en&saddr="+ source +"&daddr="+ dest));
                    startActivity(intent);
                }
            }
        });
    }
}
