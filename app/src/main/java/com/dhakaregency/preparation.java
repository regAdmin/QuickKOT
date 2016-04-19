package com.dhakaregency;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhakaregency.quickkot.R;

import java.util.ArrayList;

public class preparation extends AppCompatActivity implements  Button.OnClickListener {

    EditText editTextPrep;
    Button buttonPrepEnter;
    String index;
    Communicator communicator;
    Button buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation);
        buttonPrepEnter= (Button) findViewById(R.id.btnPreEnter);

        buttonPrepEnter.setOnClickListener(this);
        editTextPrep= (EditText) findViewById(R.id.editTextPrep);
        buttonBack= (Button) findViewById(R.id.btnBack);

        buttonBack.setOnClickListener(this);
        Bundle b = getIntent().getExtras();
        index= b.getString("index");
        editTextPrep.setText(b.getString("prep"));

    }

    @Override
    public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("myData", editTextPrep.getText().toString());
                Intent intent1 = new Intent();
                intent1.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent1);
                finish();
    }
}
