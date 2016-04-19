package com.dhakaregency;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dhakaregency.quickkot.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class tablechoise extends AppCompatActivity implements Button.OnClickListener{

    String userid="";
    String moduleid="";
    String tableid="";
    TextView textViewNotice;
    Button buttonModify;
    Button buttonServed;
    Button buttonTakeServey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablechoise);
        textViewNotice= (TextView) findViewById(R.id.textViewTC);
        buttonModify= (Button) findViewById(R.id.btnTCModifyKot);
        buttonServed= (Button) findViewById(R.id.btnTCServed);
        buttonTakeServey=(Button) findViewById(R.id.btnTakeServey);
        buttonModify.setOnClickListener(this);
        buttonServed.setOnClickListener(this);
        buttonTakeServey.setOnClickListener(this);
            Bundle b = getIntent().getExtras();
            moduleid = b.getString("moduleId");
            userid = b.getString("userid");
            LoadTables loadTables = new LoadTables();
            loadTables.execute(moduleid);
}
    public void PopulateOutlets(ArrayList<TableList> tableListArrayList) {
        boolean isFirstTime = true;
        boolean isColumnCountingFinished = false;
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableListLayout);
        TableRow tableRow = null;
        int buttonId=0;
        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);

        int leftMargin=8;
        int topMargin=3;
        int rightMargin=8;
        int bottomMargin=3;

        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        TableRow.LayoutParams tableButtonParams=
                new TableRow.LayoutParams
                        (TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT);
                       tableButtonParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        int col = 0;
        for (final TableList tableList: tableListArrayList) {

            if (isFirstTime) {
                tableRow = new TableRow(getApplicationContext());
                tableRow.setLayoutParams(tableRowParams);
                tableLayout.addView(tableRow);
                isFirstTime = false;
            }
            if (!isFirstTime && isColumnCountingFinished){
                tableRow = new TableRow(getApplicationContext());
                tableRow.setLayoutParams(tableRowParams);
                tableLayout.addView(tableRow);
                isColumnCountingFinished = false;
                col = 0;
            }
            if (!isColumnCountingFinished) {
                //TableRow.LayoutParams layoutParams=new TableRow(getApplication())

                final Button button = new Button(getApplicationContext());
                button.setText(tableList.getDescription().toString().concat("(").concat(tableList.getCode()).concat(")"));
                button.setLayoutParams(tableButtonParams);

                int tableused = tableList.getUsed();



                if(tableused==0)// table is open to use for KOT
                {
                   // button.setBackgroundColor(Color.parseColor("#8CB63D"));
                    button.setTextColor(Color.parseColor("#000000")); // custom color
                }
                else {
                   // button.setBackgroundColor(Color.parseColor("#730000"));
                    button.setTextColor(Color.parseColor("#FF0000")); // custom color
                }

                button.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {

                                                  if (button.getCurrentTextColor()!=Color.RED) {
                                                      String _tableid = (String) button.getText();
                                                   //   _tableid = _tableid.substring(_tableid.indexOf("(") + 1, _tableid.indexOf(")"));
                                                      GotoPax(userid, moduleid, _tableid);

                                                  }
                                                  else {

                                                      textViewNotice.setText("Table # :" + button.getText());
                                                      buttonModify.setEnabled(true);
                                                      buttonServed.setEnabled(true);
                                                      buttonTakeServey.setEnabled(true);
                                                  }
                                              }
                                          }
                );
               // button.setPadding(1, 1, 1, 1);
                button.setBackgroundResource(R.mipmap.tabs);
                button.setPadding(0,0,0,10);
                 tableRow.addView(button);
            }
            if (col >7) {
                isColumnCountingFinished = true;
            }
            else {
                col++;
            }
            buttonId++;
        }

    }
    private void GotoPax(String _userId, String _moduleId, String _tableid)
    {
        Intent intent = new Intent(getApplicationContext(),pax.class );
        //Create the bundle
        Bundle bundle = new Bundle();
        //Add your data to bundle
        bundle.putString("userid", _userId);
        bundle.putString("moduleId",_moduleId);
        bundle.putString("tableid",_tableid);
        bundle.putString("isedit", "0");
        //Add the bundle to the intent
         intent .putExtras(bundle);
        startActivity(intent);
    }

    public void callModify()
    {

        Intent intent=new Intent(getApplicationContext(),sendorder.class);
        String _tableid = textViewNotice.getText().toString();
        // _tableid = _tableid.substring(_tableid.indexOf("(") + 1, _tableid.indexOf(")"));
        Bundle bundle = new Bundle();
        //Add your data to bundle
        bundle.putString("userid", userid);
        bundle.putString("moduleId", moduleid);
        bundle.putString("tableid", _tableid);
        bundle.putString("pax","1");
        bundle.putString("isedit","1");

        intent .putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        String _tableid = textViewNotice.getText().toString();
        switch (v.getId()) {

            case R.id.btnTCModifyKot:

                callModify();
                break;
            case R.id.btnTCServed:                // service provided

                SaveToServed savetoservice=new SaveToServed();
                savetoservice.execute(_tableid);
                break;
            case R.id.btnTakeServey: // serey taking option
                TakeServeyComments();
                break;
        }
    }
    private void TakeServeyComments() {
        String _tableid = textViewNotice.getText().toString();
        Intent intent=new Intent(getApplicationContext(),guest_comments.class);
        Bundle bundle = new Bundle();
        //Add your data to bundle
        bundle.putString("userid", userid);
        bundle.putString("moduleId", moduleid);
        bundle.putString("tableid", _tableid);
        bundle.putString("pax","1");
        bundle.putString("isedit","1");

        intent .putExtras(bundle);
        startActivity(intent);

    }
    public class LoadTables extends AsyncTask<String, Void, ArrayList<TableList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(ArrayList<TableList> tableListArrayList) {
            super.onPostExecute(tableListArrayList);
            PopulateOutlets(tableListArrayList);
        }

        @Override
        protected ArrayList<TableList> doInBackground(String... params) {

            String str = "http://192.168.99.12:8080/AuthService.svc/GetTableList";
            String response = "";
            ArrayList<TableList> tableListArrayList = new ArrayList<>();

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
                String moduleid = params[0].toString();

                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");


                JSONObject jsonObject = new JSONObject();
                // Build JSON string
                JSONStringer userJson = new JSONStringer()
                        .object()
                        .key("moduleid").value(moduleid)//Todo place your variable here
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
            Integer result = 0;
            JSONObject jObject = null;
            if (!response.isEmpty()) {
                try {
                    jObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            try {
                JSONArray jsonArray = (JSONArray) jObject.getJSONArray("GetTableListResult");
                try {

                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        TableList tableList= new TableList();
                        tableList.setCode(object.getString("code"));
                        tableList.setDescription(object.getString("description"));
                        tableList.setUsed(object.getInt("used"));
                        tableListArrayList.add(tableList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tableListArrayList;
        }
    }
    private void ServiceProvide(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    public class SaveToServed extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String  s) {
            super.onPostExecute(s);
            ServiceProvide(s);
        }


        @Override
        protected String  doInBackground(String... params) {

            String str = "http://192.168.99.12:8080/AuthService.svc/SaveService";
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
                        .key("tableid").value(tableid)//Todo place your variable here
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


}


