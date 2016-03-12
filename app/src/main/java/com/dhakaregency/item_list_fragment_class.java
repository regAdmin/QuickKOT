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
public class item_list_fragment_class extends Fragment{

    Communicator communicator;
    ListView listView;
    Activity activity;
Context _c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.itemlist_layout,container,false);
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
    public void callMenu(String submenucode)
    {
        if(listView!=null) {

            GetItemList getItemList=new GetItemList();
            getItemList.execute(submenucode);
        }

    }
    public  void populateItemList(ArrayList<Item> listArrayList)
    {

        listView.setAdapter(new ItemListAdapter(_c,listArrayList));

        //ArrayAdapter<String[]> arrayAdapter=new ArrayAdapter<String[]>(activity,android.R.layout.simple_list_item_2,android.R.id.text1, itemlist);

//        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // String menucode=(String)((String) listView.getItemAtPosition(position)).substring(0,1);
                TextView textdesc= (TextView) view.findViewById(R.id.txtDescription);
                //TextView textcode= (TextView) view.findViewById(R.id.txtCode);
                TextView textSp= (TextView) view.findViewById(R.id.txtSalesPrice);

                String desc=textdesc.getText().toString();
                String salesprice=textSp.getText().toString();
                String code="0000001";
                String qty="80";
                SingleRowCheckout singleRow=new SingleRowCheckout(code,desc,salesprice,qty);
                communicator.ParseItem(singleRow);

            }
        });

    }


    public class GetItemList extends AsyncTask<String, Void, ArrayList<Item>> {

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
        protected ArrayList<Item> doInBackground(String ... params) {

            String str = "http://192.168.99.12:8080/AuthService.svc/GetItem";
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


                JSONObject jsonObject = new JSONObject();
                // Build JSON string
                JSONStringer userJson = new JSONStringer()
                        .object()
                        .key("moduleid").value(params.toString())//Todo place your variable here
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
