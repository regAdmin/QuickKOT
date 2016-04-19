package com.dhakaregency;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhakaregency.quickkot.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class pax extends AppCompatActivity  {

    String userid;
    String moduleid;
    String tableid;
    Button buttonMenu;

    EditText editTextPax;
    String isEditMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pax);


        Bundle b = getIntent().getExtras();

        moduleid= b.getString("moduleId");
        userid= b.getString("userid");
        tableid= b.getString("tableid");
        isEditMode= b.getString("isedit");


        editTextPax= (EditText) findViewById(R.id.input_pax);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);



        buttonMenu= (Button) findViewById(R.id.btn_menue);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               Intent intent=new Intent(getApplicationContext(),sendorder.class );

                String pax1=editTextPax.getText().toString();

                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("userid", userid);
                bundle.putString("moduleId", moduleid);
                bundle.putString("tableid", tableid);
                bundle.putString("pax",pax1);
                bundle.putString("isedit",isEditMode);

                intent .putExtras(bundle);

               startActivity(intent);
                /*
                PrintKOT printKOT=new PrintKOT();
                printKOT.execute();*/

            }
        });

    }




}
