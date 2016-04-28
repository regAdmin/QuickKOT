package com.dhakaregency;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

public class sendorder extends AppCompatActivity implements Communicator,Button.OnClickListener {


    private static final String ITEM_LIST = "item_list";
    Button buttonFinalize;

    ListView listFinal;

    String userid;
    String moduleid;
    String tableid;
    String pax;
String isEditMode;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendorder);

        Bundle b = getIntent().getExtras();

        moduleid= b.getString("moduleId");
        userid= b.getString("userid");
        tableid= b.getString("tableid");
        pax= b.getString("pax");
        isEditMode=b.getString("isedit");

        final Button buton= (Button) findViewById(R.id.btnFood);
        final Button buttonBev=(Button) findViewById(R.id.btnBev);
        final Button buttonShisha=(Button) findViewById(R.id.btnShisha);
        final Button buttonHD=(Button) findViewById(R.id.btnHD);
        final Button buttonOthers=(Button) findViewById(R.id.btnOthers);
        final Button buttonBackToOrderTable=(Button) findViewById(R.id.btnBackToOrderTable);


        buttonFinalize= (Button) findViewById(R.id.btnFinalize);


       listFinal= (ListView) findViewById(R.id.lstItemCheckout);

        buton.setOnClickListener((View.OnClickListener) this);
        buttonBev.setOnClickListener((View.OnClickListener) this);
        buttonShisha.setOnClickListener((View.OnClickListener) this);
        buttonHD.setOnClickListener((View.OnClickListener) this);
        buttonOthers.setOnClickListener((View.OnClickListener) this);
        buttonBackToOrderTable.setOnClickListener((View.OnClickListener) this);

        buttonFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), final_checkout_bill.class);
                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("userid", userid);
                bundle.putString("moduleId", moduleid);
                bundle.putString("tableid", tableid);
                bundle.putString("pax",pax);
                bundle.putString("isedit",isEditMode);
                intent .putExtras(bundle);

                ArrayList<Final_Bill> arrayList=new ArrayList<Final_Bill>();
                if(listFinal!=null) {

                    for(int position=0;position<listFinal.getCount();position++)
                    {

                        SingleRowCheckout singlerow= (SingleRowCheckout) listFinal.getItemAtPosition(position);
                        Final_Bill finalbill=new Final_Bill(singlerow.getCodes(), singlerow.getDescriptions(), singlerow.getQty(), singlerow.getPreparation(),singlerow.getSaless(),singlerow.getCost(),singlerow.getKitchen());
                        arrayList.add(finalbill);
                    }

                    if(arrayList.size()!=0)
                    {
                        intent.putParcelableArrayListExtra(ITEM_LIST, arrayList);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No Item Choosen",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Null",Toast.LENGTH_LONG).show();
                }
            }
        });
        if(Integer.parseInt(isEditMode)==1){
            String   _tableid = tableid.substring(tableid.indexOf("(") + 1, tableid.indexOf(")"));
            PopulateItems populateItems=new PopulateItems();
            populateItems.execute(_tableid);
        }
        }
    @Override
    public void onClick(View v) {
        int foodtype=0;
        if(v == findViewById(R.id.btnFood)){
            foodtype=0;
        }
        else if(v == findViewById(R.id.btnBev)){
            foodtype=1;
        }
        else if(v == findViewById(R.id.btnShisha)){
            foodtype=3;
        }
        else if(v == findViewById(R.id.btnHD)){
            foodtype=4;
        }else if(v == findViewById(R.id.btnOthers)){
            foodtype=2;
        }
        else if(v == findViewById(R.id.btnBackToOrderTable)){

            Intent intent=new Intent(getApplicationContext(),tablechoise.class);

            Bundle bundle = new Bundle();
            //Add your data to bundle
            bundle.putString("userid", userid);
            bundle.putString("moduleId", moduleid);
            intent.putExtras(bundle);
            //startActivityForResult(intent, 0);
            startActivityForResult(intent, 0) ;
        }


        sub_menu_fragment_class subMenuFragmentClass= (sub_menu_fragment_class) getFragmentManager().findFragmentById(R.id.list_sub);
        subMenuFragmentClass.clearSubMenu();

        item_list_fragment_class itemListFragmentClass= (item_list_fragment_class ) getFragmentManager().findFragmentById(R.id.list_item);
        itemListFragmentClass.clearItemList();

        main_menu_fragment_class fragment = (main_menu_fragment_class) getFragmentManager().findFragmentById(R.id.list_main);
        ArrayList<String> arrayList=new ArrayList<String>();
        arrayList.add(0,moduleid);
        arrayList.add(1,foodtype+"");
        fragment.callMenu(arrayList);

    }
    @Override
    public void LoadSubMenu(String main_group_id) {
        try {
            sub_menu_fragment_class subMenuFragmentClass = (sub_menu_fragment_class) getFragmentManager().findFragmentById(R.id.list_sub);
            subMenuFragmentClass.ChangeSubMenu(main_group_id);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void LoadItemList(String subgroup_id) {
        item_list_fragment_class itemListFragmentClass= (item_list_fragment_class ) getFragmentManager().findFragmentById(R.id.list_item);
        itemListFragmentClass.callMenu(subgroup_id,moduleid);
    }

    @Override
    public void ParseItem(SingleRowCheckout singleRow) {
        Item_Check_Fragment_Class item_check_fragment_class= (Item_Check_Fragment_Class) getFragmentManager().findFragmentById(R.id.list_billing);
        item_check_fragment_class.SetItemList(singleRow);
    }

    @Override
    public void UpdatePreparation(String prep) {

    }
    public void populateItemsToListView(ArrayList<SingleRowCheckout> s)
    {
        Item_Check_Fragment_Class item_check_fragment_class= (Item_Check_Fragment_Class) getFragmentManager().findFragmentById(R.id.list_billing);
        item_check_fragment_class.PopulateKotItems(s);
    }

    public class PopulateItems extends AsyncTask<String, Void, ArrayList<SingleRowCheckout>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SingleRowCheckout> s) {
            super.onPostExecute(s);
            populateItemsToListView(s);
        }

        @Override
        protected ArrayList<SingleRowCheckout> doInBackground(String... params) {
            String str = "http://192.168.99.23:8080/AuthService.svc/GetKOT";
            String response = "";
            ArrayList<SingleRowCheckout> arrayList=arrayList=new ArrayList<>();;

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
                //KotEntity kotEntity=params[0];
                String queueno= params[0].toString();


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
                        .key("tableid").value(queueno)
                        .endObject();
                //byte[] outputBytes = jsonParam.toString().getBytes("UTF-8");
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                    outputStreamWriter.write(userJson.toString());
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

                JSONObject jObject = null;
                if (!response.isEmpty()) {
                    try {
                        jObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    JSONArray jsonArray = (JSONArray) jObject.getJSONArray("GetKOTItemsResult");
                    try {

                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            SingleRowCheckout singleRowCheckout=new SingleRowCheckout();
                            singleRowCheckout.setCodes(object.getString("code"));
                            singleRowCheckout.setDescriptions(object.getString("description"));
                            singleRowCheckout.setQty(object.getString("qty"));
                            singleRowCheckout.setSaless(object.getString("sales"));
                            singleRowCheckout.setPreparation(object.getString("preparation"));
                            singleRowCheckout.setCanmodify(object.getString("canmodify"));

                            arrayList.add(singleRowCheckout);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return arrayList;
        }
    }
}
