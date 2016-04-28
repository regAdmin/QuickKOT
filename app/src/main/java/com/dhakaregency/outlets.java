package com.dhakaregency;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import javax.net.ssl.HttpsURLConnection;

public class outlets extends AppCompatActivity {

    public String muserId="";
    public String moduleid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlets);

        Bundle b = getIntent().getExtras();
        muserId= b.getString("userid");

        ArrayList<String> passing = new ArrayList<String>();
        passing.add(muserId);
        passing.add(muserId);
        LoadOutlets loadOutlets = new LoadOutlets();
        loadOutlets.execute(passing);

    }

    public void PopulateOutlets(ArrayList<OutletsEntity> outletsEntityArrayList) {
        boolean isFirstTime = true;
        boolean isColumnCountingFinished = false;
        TableLayout tableLayout = (TableLayout) findViewById(R.id.outletsTableLayout);
        TableRow tableRow = null;
        int col = 0;

        int leftMargin=8;
        int topMargin=3;
        int rightMargin=8;
        int bottomMargin=3;

        TableRow.LayoutParams tableButtonParams=
                new TableRow.LayoutParams
                        (TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT);
        tableButtonParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        for (final OutletsEntity outletsEntity : outletsEntityArrayList) {
            if (isFirstTime) {
                tableRow = new TableRow(getApplicationContext());
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT, 1.0f
                ));
                tableLayout.addView(tableRow);
                isFirstTime = false;
            }
            if (!isFirstTime && isColumnCountingFinished){
                tableRow = new TableRow(getApplicationContext());
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT, 1.0f
                ));
                tableLayout.addView(tableRow);
                isColumnCountingFinished = false;
                col = 0;
            }
            if (!isColumnCountingFinished) {



                final Button button = new Button(getApplicationContext());
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f
                ));
                moduleid=outletsEntity.getId().toString();
                button.setText(outletsEntity.getName().toString());
            //    button.setBackgroundColor(Color.parseColor("#5D8AA8"));
                button.setBackgroundResource(R.drawable.customeborder);
                button.setLayoutParams(tableButtonParams);
                button.setTextColor(Color.BLACK);
                button.setPadding(10, 10, 10, 10);

                button.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {

                                                  GotoGuestTypeChoise(button.getText().toString());
                                              }
                                          }
                );
                tableRow.addView(button);
            }
            if (col > 0) {
                isColumnCountingFinished = true;
            }
            else {
                col++;
            }

        }

    }

    private void GotoGuestTypeChoise(String moduleId)
    {
     //   Intent intent = new Intent(getApplicationContext(),guestchoise.class );
        Intent intent = new Intent(getApplicationContext(),tablechoise.class );

        //Create the bundle

        Bundle b = getIntent().getExtras();

        Bundle bundle = new Bundle();
        //Add your data to bundle
        bundle.putString("userid", muserId);
        bundle.putString("moduleId", moduleId.toString().substring(0,2));
        //Add the bundle to the intent
        intent .putExtras(bundle);
        startActivity(intent);
    }

    public class LoadOutlets extends AsyncTask<ArrayList<String>, Void, ArrayList<OutletsEntity>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(ArrayList<OutletsEntity> outletsEntityArrayList) {
            super.onPostExecute(outletsEntityArrayList);
            PopulateOutlets(outletsEntityArrayList);
        }

        @Override
        protected ArrayList<OutletsEntity> doInBackground(ArrayList<String>... params) {

            String str = "http://192.168.99.23:8080/AuthService.svc/GetModules";
            String response = "";
            ArrayList<OutletsEntity> outletsEntities = new ArrayList<>();

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
                ArrayList<String> passed = params[0];

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
                        .key("user")
                        .object()
                        .key("userid").value(passed.get(0).toString())
                        .key("password").value(passed.get(1).toString())
                        .endObject()
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
                JSONArray jsonArray = (JSONArray) jObject.getJSONArray("GetModulesResult");
                try {

                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        OutletsEntity outletsEntity = new OutletsEntity();
                        outletsEntity .setId(object.getString("code"));
                        outletsEntity.setName(object.getString("description"));
                        outletsEntities.add(outletsEntity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return outletsEntities;
        }
    }
}
