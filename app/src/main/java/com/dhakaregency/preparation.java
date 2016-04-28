package com.dhakaregency;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class preparation extends AppCompatActivity implements  Button.OnClickListener {

    EditText editTextPrep;
    Button buttonPrepEnter;
    String index;
    Communicator communicator;
    Button buttonBack;
    Spinner spinner;
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


        spinner= (Spinner) findViewById(R.id.cmbKitchen);
        LoadKitchen loadKitchen=new LoadKitchen();
        loadKitchen.execute();


    }
    private void LoadCombo(ArrayList<Kitchen> myarray)
    {

        List<String> list = new ArrayList<String>();


        // looping through all item nodes <item>
        for ( int i = 0; i < myarray.size();i++) {
            String value=myarray.get(i).getKitchen_name().concat("(").concat(myarray.get(i).getKitchen_id()).concat(")");
            list.add(i,value);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);
        spinner.setAdapter(dataAdapter);
    }


    @Override
    public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("myData", editTextPrep.getText().toString());
        bundle.putString("kit", spinner.getSelectedItem().toString());
                Intent intent1 = new Intent();
                intent1.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent1);
                finish();
    }

    public class LoadKitchen extends AsyncTask<Void, Void, ArrayList<Kitchen>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(ArrayList<Kitchen> kitchenArrayList) {
            super.onPostExecute(kitchenArrayList);
            LoadCombo(kitchenArrayList);
        }



        @Override
        protected ArrayList<Kitchen> doInBackground(Void... params) {

            String str = "http://192.168.99.23:8080/AuthService.svc/GetKitchen";
            String response = "";
            ArrayList<Kitchen> kitchenArrayList = new ArrayList<>();

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


                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");



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
                JSONArray jsonArray = (JSONArray) jObject.getJSONArray("GetKitchenResult");
                try {

                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Kitchen kitchen= new Kitchen();
                        kitchen.setKitchen_id (object.getString("code"));
                        kitchen.setKitchen_name(object.getString("name"));
                        kitchenArrayList.add(kitchen);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return kitchenArrayList;
        }
    }
}
