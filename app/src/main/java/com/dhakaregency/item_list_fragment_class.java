package com.dhakaregency;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Administrator on 29/02/2016.
 */
public class
        item_list_fragment_class extends Fragment{

    Communicator communicator;
    ListView listView;
    Activity activity;
    Context _c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.itemlist_layout, container, false);


    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            activity=(Activity) context;
            communicator= (Communicator) activity;
            _c=context;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView= (ListView) getView().findViewById(R.id.lstItem);
    }
    public void callMenu(String submenucode,String moduleid)
    {
        if(listView!=null) {

            ArrayList<String> passing = new ArrayList<String>();
            passing.add(submenucode);
            passing.add(moduleid);

            GetItemList getItemList=new GetItemList();
            getItemList.execute(passing);
        }

    }
    public  void populateItemList(ArrayList<Item> listArrayList)
    {

        ItemListAdapter itemListAdapter=new ItemListAdapter(getActivity(),listArrayList);
        listView.setAdapter(itemListAdapter);

        //ArrayAdapter<String[]> arrayAdapter=new ArrayAdapter<String[]>(activity,android.R.layout.simple_list_item_2,android.R.id.text1, itemlist);

//        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // String menucode=(String)((String) listView.getItemAtPosition(position)).substring(0,1);
                TextView textdesc= (TextView) view.findViewById(R.id.txtDescription);
                TextView textcode= (TextView) view.findViewById(R.id.txtItemCodes);
                TextView textSp= (TextView) view.findViewById(R.id.txtSalesPrice);
                TextView textKitchen=(TextView) view.findViewById(R.id.txtKitchen);

                String desc=textdesc.getText().toString();
                String salesprice=textSp.getText().toString();
                String code=textcode.getText().toString();
                String qty="1";
                String kitchen=textKitchen.getText().toString();
                SingleRowCheckout singleRow=new SingleRowCheckout(code,desc,qty,salesprice,"0","0",kitchen);
                communicator= (Communicator) getActivity();
                communicator.ParseItem(singleRow);

            }
        });

    }


public void clearItemList(){
    ArrayList<String> arrayList2=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayList2);
    listView.setAdapter(arrayAdapter);
}

    public class GetItemList extends AsyncTask<ArrayList<String>, Void, ArrayList<Item>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Item> itemArrayList )
        {
            super.onPostExecute(itemArrayList);
            populateItemList(itemArrayList);
        }

        @Override
        protected ArrayList<Item> doInBackground(ArrayList<String> ... params) {

            String str = "http://192.168.99.23:8080/AuthService.svc/GetItem";
            String response = "";
            ArrayList<Item> itemArrayList= new ArrayList<>();

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
                //   ArrayList<String> passed = params[0];

                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");

                ArrayList<String> passed = params[0];

                JSONObject jsonObject = new JSONObject();
                // Build JSON string
                JSONStringer userJson = new JSONStringer()
                        .object()
                        .key("subgroupid").value(passed.get(0).toString())//Todo place your variable here
                        .key("moduleid").value(passed.get(1).toString())//Todo place your variable here
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
                JSONArray jsonArray = (JSONArray) jObject.getJSONArray("GetItemResult");
                try {

                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Item item= new Item();
                        item.setCode(object.getString("code"));
                        item.setDescription(object.getString("description"));
                        item.setSales(object.getString("sales"));
                        item.setKitchen(object.getString("kitchen"));
                        itemArrayList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return itemArrayList;
        }

    }
}
