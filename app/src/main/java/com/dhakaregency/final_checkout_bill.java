package com.dhakaregency;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class final_checkout_bill extends AppCompatActivity {

    ListView listView;
    String moduleid;
    String userid;
    String tableid;
    String pax;
    String preparation;
    Button buttonBack;
    Button buttonSendKOT;
    TextView textViewTable;
    String iseditmode="0";
    Context _context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_final_checkout_bill);
        listView= (ListView) findViewById(R.id.lstFinalKOT);
        textViewTable= (TextView) findViewById(R.id.txtTable);
        buttonBack= (Button) findViewById(R.id.btnFinalBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonSendKOT= (Button) findViewById(R.id.btnFinalKOT);
        Bundle b = getIntent().getExtras();

        moduleid = b.getString("moduleId");
        userid = b.getString("userid");
        tableid = b.getString("tableid");
        pax= b.getString("pax");
        iseditmode=b.getString("isedit");

        textViewTable.setText("Table Check- ".concat(tableid));
        ArrayList<Final_Bill> final_bills  = getIntent().getParcelableArrayListExtra("item_list");
        if(final_bills!=null){

            if(listView!=null) {

                try {
                    KOTAdapter kotAdapter;
                    kotAdapter= new KOTAdapter (getApplicationContext(), 0, final_bills  );
                    listView.setAdapter(kotAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String a="Nothing";
            }
        }
        buttonSendKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// SENDING KOT TO THE DATBASE
                new AlertDialog.Builder(_context)
                        .setTitle("Confirm Submit")
                        .setMessage("Are you sure you want send order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<Final_Bill> _finalbilllistt=new ArrayList<Final_Bill>();

                                for (int i = 0; i < listView.getCount(); i++) {

                                    View vw=listView.getChildAt(i);

                                    String code=((TextView)vw.findViewById(R.id.txtKOTCode)).getText().toString();
                                    String desc=((TextView)vw.findViewById(R.id.txtKOTDescription)).getText().toString();
                                    String qty=((TextView)vw.findViewById(R.id.txtKOTQTY)).getText().toString();

                                    String prep=((TextView)vw.findViewById(R.id.txtKOTPrep)).getText().toString();
                                    String sp=((TextView)vw.findViewById(R.id.txtKOTSP)).getText().toString();
                                    Final_Bill final_bill= new Final_Bill(code,desc,qty,prep,sp,"0");
                                    _finalbilllistt.add(final_bill);
                                }

                                if(Integer.parseInt(iseditmode.trim())==0) {
                                    KotEntity kotEntity = new KotEntity(tableid, pax, _finalbilllistt);
                                    SendKOTToDb sendKOT = new SendKOTToDb();
                                    sendKOT.execute(kotEntity);

                                }
                                else
                                {
                                    KotEntity kotEntity = new KotEntity(tableid, pax, _finalbilllistt);// edit mode enabled
                                    ModifyKOTToDb modifyKOTToDb=new ModifyKOTToDb();
                                    modifyKOTToDb.execute(kotEntity);
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        _context=context;
        return super.onCreateView(name, context, attrs);
    }

    public void ShowKOT(String kotNumber)
    {
        if (kotNumber.trim()!="0") {
            Intent intent = new Intent(getApplicationContext(), kot_confirmation.class);
            Bundle bundle = new Bundle();
             bundle.putString("moduleId",moduleid );
             bundle.putString("userid",userid );
            bundle.putString("kot", kotNumber);
            bundle.putString("isedit",iseditmode);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Kot Already Posted in this table",Toast.LENGTH_SHORT).show();
        }
    }

    public class SendKOTToDb extends AsyncTask<KotEntity, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ShowKOT(s);
        }

        @Override
        protected String doInBackground(KotEntity... params) {
            String str = "http://192.168.99.12:8080/AuthService.svc/SendKOT";
            String response ="";
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
                KotEntity kotEntity=params[0];
                String tableid= kotEntity.getTableid();
                String pax= kotEntity.getPax();
                ArrayList<Final_Bill> kotEntities=kotEntity.getItems();


                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");


                JSONObject jsonObject = new JSONObject();
                // Build JSON string
                JSONStringer v = new JSONStringer();


               // for(Final_Bill final_bill: kotEntities)
               // {
                /*
                v.object();
                v.key("items");
                    v.object();

                        v.key("code").value(final_bill.getItemCode());
                        v.key("description").value(final_bill.getItemDescription());
                        v.key("qty").value(final_bill.getItemQty());
                        v.key("preparation").value(final_bill.getItemPrep());
                        v.key("cost").value(final_bill.getItemCostPrice());
                        v.key("sales").value(final_bill.getItemSalesPrice());

                    v.endObject();
                 v.endObject();
               // }
                //v.endObject();
                JSONArray arr = new JSONArray();
                arr.put(v);

                JSONStringer vehicle = new JSONStringer();
                vehicle.object();
                vehicle.key("kotdata").value(arr);
                vehicle.endObject();
                */
                String   _tableid = tableid.substring(tableid.indexOf("(") + 1, tableid.indexOf(")"));

                JSONStringer userJson = new JSONStringer()
                        .object()
                        .key("userid").value(userid)
                        .endObject();

                JSONStringer userPax = new JSONStringer()
                        .object()
                        .key("pax").value(pax)
                        .endObject();

                JSONStringer userTable= new JSONStringer()
                        .object()
                        .key("table_no").value(_tableid)
                        .endObject();

                JSONStringer userModule= new JSONStringer()
                        .object()
                        .key("moduleid").value(moduleid)
                        .endObject();


                JSONObject jResult = new JSONObject();// main object
                JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

                for (int i = 0; i < kotEntities.size(); i++) {
                    JSONObject jGroup = new JSONObject();// /sub Object

                    try {
                        jGroup.put("code", kotEntities.get(i).getItemCode());
                        jGroup.put("description", kotEntities.get(i).getItemDescription());
                        jGroup.put("qty", kotEntities.get(i).getItemQty());
                        jGroup.put("preparation", kotEntities.get(i).getItemPrep());
                        jGroup.put("cost", kotEntities.get(i).getItemCostPrice());
                        jGroup.put("sales", kotEntities.get(i).getItemSalesPrice());

                        jArray.put(jGroup);

                        // /itemDetail Name is JsonArray Name
                        jResult.put("items", jArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                //byte[] outputBytes = jsonParam.toString().getBytes("UTF-8");
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                    outputStreamWriter.write(jResult.toString());
                    outputStreamWriter.write(userJson.toString());
                    outputStreamWriter.write(userPax.toString());
                    outputStreamWriter.write(userTable.toString());
                    outputStreamWriter.write(userModule.toString());

                    outputStreamWriter.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                int responseCode =0;
                try {
                    responseCode =conn.getResponseCode();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response+= line;
                    }

                } else {
                    response =conn.getErrorStream().toString();
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            response=response.substring(1,response.length()-1);

            return response;
        }
    }
    public class ModifyKOTToDb extends AsyncTask<KotEntity, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ShowKOT(s);
        }

        @Override
        protected String doInBackground(KotEntity... params) {
            String str = "http://192.168.99.12:8080/AuthService.svc/ModifyKOT";
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
                KotEntity kotEntity=params[0];
                String tableid= kotEntity.getTableid();
                tableid= tableid.substring(tableid.indexOf("(") + 1, tableid.indexOf(")"));
                String pax= kotEntity.getPax();
                ArrayList<Final_Bill> kotEntities=kotEntity.getItems();


                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");


                JSONObject jsonObject = new JSONObject();
                // Build JSON string
                JSONStringer v = new JSONStringer();



                JSONStringer userJson = new JSONStringer()
                        .object()
                        .key("userid").value(userid)
                        .endObject();


                JSONStringer userTable= new JSONStringer()
                        .object()
                        .key("table_no").value(tableid)
                        .endObject();

                JSONStringer userModule= new JSONStringer()
                        .object()
                        .key("moduleid").value(moduleid)
                        .endObject();


                JSONObject jResult = new JSONObject();// main object
                JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

                for (int i = 0; i < kotEntities.size(); i++) {
                    JSONObject jGroup = new JSONObject();// /sub Object

                    try {
                        jGroup.put("code", kotEntities.get(i).getItemCode());
                        jGroup.put("description", kotEntities.get(i).getItemDescription());
                        jGroup.put("qty", kotEntities.get(i).getItemQty());
                        jGroup.put("preparation", kotEntities.get(i).getItemPrep());
                        jGroup.put("cost", kotEntities.get(i).getItemCostPrice());
                        jGroup.put("sales", kotEntities.get(i).getItemSalesPrice());

                        jArray.put(jGroup);

                        // /itemDetail Name is JsonArray Name
                        jResult.put("items", jArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //byte[] outputBytes = jsonParam.toString().getBytes("UTF-8");
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                    outputStreamWriter.write(jResult.toString());
                    outputStreamWriter.write(userJson.toString());
                    outputStreamWriter.write(userTable.toString());
                    outputStreamWriter.write(userModule.toString());

                    outputStreamWriter.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                int responseCode =0;
                try {
                    responseCode =conn.getResponseCode();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response =conn.getErrorStream().toString();
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return response;
        }
    }

    }
