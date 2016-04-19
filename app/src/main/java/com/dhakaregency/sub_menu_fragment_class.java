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
import android.widget.Button;
import android.widget.ListView;
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

/**
 * Created by Administrator on 29/02/2016.
 */
public class sub_menu_fragment_class extends Fragment{

    Activity a;
    ListView listView;
    Communicator communicator;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            a=(Activity) context;
            communicator= (Communicator) a;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView= (ListView) getView().findViewById(R.id.lstSubMenu);
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sub_menu_layout,container,false);
    }
    public void clearSubMenu()
    {
        ArrayList<String> arrayList1=new ArrayList<>();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayList1);
        listView.setAdapter(arrayAdapter);
    }
    public void ChangeSubMenu(String menu_group_id)
    {
       // Toast.makeText(getActivity(),menu_group_id.toString(),Toast.LENGTH_LONG).show();
        GetSubMainMenu getSubMainMenu=new  GetSubMainMenu();
        getSubMainMenu.execute(menu_group_id);

    }
    public void PopulateModule(ArrayList<SubMenuList> menuListArrayList) {
        ArrayList<String> arrayList=new ArrayList<>();
        for(SubMenuList menuList:menuListArrayList)
        {
            arrayList.add(menuList.getDescription());
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String subdescription=((String) listView.getItemAtPosition(position));
                String submenucode = subdescription.substring(subdescription.indexOf("(")+1,subdescription.indexOf(")") );
                communicator= (Communicator) getActivity();
                communicator.LoadItemList(submenucode);
            }
        });
    }
    public class GetSubMainMenu extends AsyncTask<String, Void, ArrayList<SubMenuList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SubMenuList> menuListArrayList)
        {
            super.onPostExecute(menuListArrayList);
            PopulateModule(menuListArrayList);
        }

        @Override
        protected ArrayList<SubMenuList> doInBackground(String ... params) {

            String str = "http://192.168.99.12:8080/AuthService.svc/GetSubMenuList";
            String response = "";
            ArrayList<SubMenuList> menuListArrayList= new ArrayList<>();

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
             String menucode= params[0].toString();

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
                        .key("groupid").value(menucode)//Todo place your variable here
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
                JSONArray jsonArray = (JSONArray) jObject.getJSONArray("GetSubMenuListResult");
                try {

                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        SubMenuList outletsEntity = new SubMenuList();
                        outletsEntity .setCode(object.getString("code"));
                        outletsEntity.setDescription(object.getString("description"));
                        menuListArrayList.add(outletsEntity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return menuListArrayList;
        }

    }

}

