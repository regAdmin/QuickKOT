package com.dhakaregency;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dhakaregency.quickkot.R;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class guest_comments extends AppCompatActivity implements  Button.OnClickListener{

    Button buttonSubmit;
    String tableid;
    RadioGroup radioGroup;
    String strComments="";
    TextView textView;
    Button buttonBack;
Context _context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_comments);

        Bundle b = getIntent().getExtras();
        tableid= b.getString("tableid");
        textView = (TextView) findViewById(R.id.textviewComTable);
        buttonBack= (Button) findViewById(R.id.btnBackToConfirmTable);
        textView.setText(tableid);

        buttonSubmit= (Button) findViewById(R.id.btnSubmitComments);
        buttonSubmit.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        radioGroup= (RadioGroup) findViewById(R.id.rdoGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rdoGood:
                        strComments="Good";
                        break;
                    case R.id.rdoAverage:
                        strComments="Average";
                        break;
                    case R.id.rdoPoor:
                        strComments="Poor";
                        break;
                    case R.id.rdoNeedImprovement:
                        strComments="Needs Improvement";
                        break;
                }
            }
       });
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        _context=context;
        return super.onCreateView(name, context, attrs);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSubmitComments:
                new AlertDialog.Builder(_context)
                        .setTitle("Confirm Submit")
                        .setMessage("Are you sure you want to submit your comments?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                TakeServey takeServey=new TakeServey();
                                String[] sparam=new String[2];
                                sparam[0]=tableid;
                                sparam[1]=strComments;
                                takeServey.execute(sparam);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;
            case R.id.btnBackToConfirmTable:
                this.finish();
                break;
        }


    }

    public class TakeServey extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String  s) {
            super.onPostExecute(s);
            TakeServeyComments(s);
        }
        @Override
        protected String  doInBackground(String... params) {

            String str = "http://192.168.99.23:8080/AuthService.svc/TakeServey";
            String response = "";

            URL url = null;
            try {
                url = new URL(str);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String tableid= params[0].toString();
                String comments= params[1].toString();

                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");


                tableid= tableid.substring(tableid.indexOf("(") + 1, tableid.indexOf(")"));


                JSONObject jsonObject = new JSONObject();
                // Build JSON string
                JSONStringer userJson = new JSONStringer()
                        .object()
                        .key("tableid").value(tableid)
                        .key("comments").value(comments)
                        .endObject();

                //byte[] outputBytes = jsonParam.toString().getBytes("UTF-8");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                outputStreamWriter.write(userJson.toString());
                outputStreamWriter.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private void TakeServeyComments(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

    }
}
