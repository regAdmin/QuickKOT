package com.dhakaregency;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Administrator on 29/02/2016.
 */
public class Item_Check_Fragment_Class extends Fragment implements  Button.OnClickListener{

    String itemqty="";
    Communicator communicator;
    Activity activity;
    ListView listView;
    Context _context;
    ViewGroup viewGroup;


    ArrayList<SingleRowCheckout> list =new ArrayList<SingleRowCheckout>();
    ArrayAdapter<SingleRowCheckout> adapter;

    Button buttonOne;
    Button buttonTwo;
    Button buttonThree;
    Button buttonFour;
    Button buttonFive;
    Button buttonSix;
    Button buttonSeven;
    Button buttonEight;
    Button buttonNine;
    Button buttonZero;
    Button buttonEnter;
    Button buttonDel;
    Button buttonPrep;

    TextView txtPrep;
    int selectedIndex=-1;
    int previousSelectedIndex=-1;
    View view=null;
    TextView txtKitchen;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //this.viewGroup=container;

        try {
            view=inflater.inflate(R.layout.item_check_layout,container,false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (Activity) context;
        _context=context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String isEditmode;
        Bundle b =getActivity().getIntent().getExtras();
        if (b!=null) {
            isEditmode = b.getString("isedit");
        }


        listView= (ListView) getView().findViewById(R.id.lstItemCheckout);
        if (listView==null)
        {
            String a="asdf";
        }

        adapter = new ArrayAdapter<SingleRowCheckout>(getActivity(), R.layout.single_row_checkout, list);
         txtKitchen= (TextView) getView().findViewById(R.id.txtKitchenCheckout);



        buttonOne= (Button) view.findViewById(R.id.btn1);
        buttonTwo= (Button) view.findViewById(R.id.btn2);
        buttonThree= (Button) view.findViewById(R.id.btn3);
        buttonFour= (Button) view.findViewById(R.id.btn4);
        buttonFive= (Button) view.findViewById(R.id.btn5);
        buttonSix= (Button) view.findViewById(R.id.btn6);
        buttonSeven= (Button) view.findViewById(R.id.btn7);
        buttonEight= (Button) view.findViewById(R.id.btn8);
        buttonNine= (Button) view.findViewById(R.id.btn9);
        buttonZero= (Button) view.findViewById(R.id.btn0);
        buttonEnter= (Button) view.findViewById(R.id.btnEnter);
        buttonDel= (Button) view.findViewById(R.id.btnDel);
        buttonPrep= (Button) view.findViewById(R.id.btnPrep);


        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonFour.setOnClickListener(this);
        buttonFive.setOnClickListener(this);
        buttonSix.setOnClickListener(this);
        buttonSeven.setOnClickListener(this);
        buttonEight.setOnClickListener(this);
        buttonNine.setOnClickListener(this);
        buttonZero.setOnClickListener(this);
        buttonEnter.setOnClickListener(this);
        buttonDel.setOnClickListener(this);
        buttonPrep.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (selectedIndex != -1) {
                    View vw = listView.getChildAt(selectedIndex);
                    vw.setBackgroundColor(Color.LTGRAY); // previoius

                    View vw1 = listView.getChildAt(position);
                    if (vw1!=null) {
                        vw1.setBackgroundColor(Color.GREEN); // current
                    }

                }
                else
                {
                    View vw1 = listView.getChildAt(position);
                    if (vw1!=null) {
                        vw1.setBackgroundColor(Color.GREEN); // current
                    }
                }

                selectedIndex=position;

            }
        });

    }

    public void PopulateKotItems(ArrayList<SingleRowCheckout> s)
    {
        if(listView!=null) {
            try {

                for(SingleRowCheckout srow:s){
                    //arrayAdapter.add(srow);
                    list.add(srow);
                }


                CheckoutBillAdapter checkoutBillAdapter;
                checkoutBillAdapter = new CheckoutBillAdapter(getActivity(), 0, list);
                listView.setAdapter(checkoutBillAdapter);

                //CheckoutBillAdapter checkoutBillAdapter= new CheckoutBillAdapter(_context, 0, list);
               // checkoutBillAdapter.notifyDataSetChanged();
//                listView.setAdapter(checkoutBillAdapter);


               // arrayAdapter.notifyDataSetChanged();
               // listView.setAdapter(arrayAdapter);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getItemIndexPosition(String itemCode)
    {
        int indexPosition=-1;

        ArrayAdapter<SingleRowCheckout> arrayAdapter = (ArrayAdapter<SingleRowCheckout>) listView.getAdapter();

        for(int i=0;i<arrayAdapter.getCount();i++) {
           SingleRowCheckout singleRowCheckout= arrayAdapter.getItem(i);
           if(singleRowCheckout.getCodes().trim()==itemCode.trim())
           {
               indexPosition=i;
               break;
           }

        }
        return indexPosition;
    }

    public void SetItemList(SingleRowCheckout singleRow)
    {
        if(listView!=null)
        {
            String itemSerarch=singleRow.getCodes().trim();
            int indexpostion=-1;
            ArrayAdapter<SingleRowCheckout> arrayAdapter = (ArrayAdapter<SingleRowCheckout>) listView.getAdapter();
            if (arrayAdapter!=null) {
                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    SingleRowCheckout singleRowCheckout = arrayAdapter.getItem(i);
                    String listItemsCode=singleRowCheckout.getCodes().trim();
                    if (listItemsCode.equalsIgnoreCase(itemSerarch))
                    {
                        indexpostion = i;
                        break;
                    }
                }
                if (indexpostion == -1) {
                    arrayAdapter.add(singleRow);
                    arrayAdapter.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapter);
                } else {
                    SingleRowCheckout singleRowCheckout = arrayAdapter.getItem(indexpostion);
                    String itemQty = (String) singleRowCheckout.getQty();
                    try {
                        itemqty = (Double.parseDouble(itemQty) + 1) + "";
                        singleRowCheckout.setQty(itemqty);
                        arrayAdapter.notifyDataSetChanged();
                        listView.setAdapter(arrayAdapter);
                        itemqty = "";
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }
            }
            else
            {
                //list.add(singleRow);
try {
    list.add(singleRow);
    CheckoutBillAdapter checkoutBillAdapter;
    checkoutBillAdapter = new CheckoutBillAdapter(getActivity(), 0, list);
    listView.setAdapter(checkoutBillAdapter);
}
catch (Exception ex)
{
    ex.printStackTrace();
}

           //     arrayAdapter.add(singleRow);
           //     arrayAdapter.notifyDataSetChanged();
              //  listView.setAdapter(arrayAdapter);
            }
            /*
                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    SingleRowCheckout singleRowCheckout = arrayAdapter.getItem(i);
                    if (singleRowCheckout.getCodes().trim() != singleRow.getCodes().trim()) {
                        arrayAdapter.add(singleRow);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                    } else {
                        View vw = listView.getChildAt(i);
                        TextView txtqty = ((TextView) vw.findViewById(R.id.txtQty));
                        String itemQty = (String) txtqty.getText();
                        itemqty = (Integer.parseInt(itemQty) + 1) + "";
                        singleRowCheckout.setQty(itemqty);
                        arrayAdapter.notifyDataSetChanged();
                        itemqty = "";
                        break;
                    }*/
            //}
        }

    }
    public void UpdatePreparation(String preparation,String kitchenvalue)
    {

        View vw= listView.getChildAt(selectedIndex);
        txtPrep= ((TextView) vw.findViewById(R.id.txtPrepCheckout));
        txtKitchen=((TextView) vw.findViewById(R.id.txtKitchenCheckout));
        SingleRowCheckout singleRowCheckout = (SingleRowCheckout) listView.getItemAtPosition(selectedIndex);
        txtPrep.setText(preparation);
        txtKitchen.setText(kitchenvalue);
        singleRowCheckout.setPreparation(preparation);
        ArrayAdapter<SingleRowCheckout> arrayAdapter= (ArrayAdapter<SingleRowCheckout>) listView.getAdapter();

        arrayAdapter.getItem(selectedIndex).setPreparation(preparation);
        arrayAdapter.getItem(selectedIndex).setKitchen(kitchenvalue);
        arrayAdapter.notifyDataSetChanged();

//listView.setAdapter(arrayAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null) {
            Bundle bundle = data.getExtras();
            String prep = bundle.getString("myData");
            String kitch= bundle.getString("kit");
            UpdatePreparation(prep,kitch);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case  R.id.btn1: {
                itemqty="1";
                break;
            }

            case R.id.btn2: {
                itemqty=itemqty+"2";
                break;
            }
            case R.id.btn3: {
                itemqty=itemqty+"3";
                break;
            }

            case R.id.btn4: {
                itemqty=itemqty+"4";
                break;
            }

            case R.id.btn5: {
                itemqty=itemqty+"5";
                break;
            }

            case R.id.btn6: {
                itemqty=itemqty+"6";
                break;
            }

            case R.id.btn7: {
                itemqty=itemqty+"7";
                break;
            }

            case R.id.btn8: {
                itemqty=itemqty+"8";
                break;
            }

            case R.id.btn9: {
                itemqty=itemqty+"9";
                break;
            }

            case R.id.btn0: {
                itemqty=itemqty+"0";
                break;
            }
            case R.id.btnPrep: {

                if (selectedIndex != -1) {

                    SingleRowCheckout singleRowCheckout = (SingleRowCheckout) listView.getItemAtPosition(selectedIndex);

                    Intent intent = new Intent(getActivity(), preparation.class);
                    Bundle bundle = new Bundle();
                    //Add your data to bundle
                    bundle.putString("index", selectedIndex + "");
                    bundle.putString("prep", singleRowCheckout.getPreparation() );
                    intent.putExtras(bundle);
                    //startActivityForResult(intent, 0);
                   startActivityForResult(intent, 0) ;

                }
                break;
            }
            case R.id.btnDel: {
                if(selectedIndex!=-1) {
                    SingleRowCheckout singleRowCheckout = (SingleRowCheckout) listView.getItemAtPosition(selectedIndex);
                    if (singleRowCheckout != null) {
                        String isPrinted = singleRowCheckout.getCanmodify();
                        if (isPrinted == null) {
                            isPrinted = "0";
                        }
                        if (Integer.parseInt(isPrinted) == 1) {
                            Toast.makeText(getActivity(), "Already Printed", Toast.LENGTH_SHORT).show();
                        } else {

                            ArrayAdapter<SingleRowCheckout> arrayAdapter = (ArrayAdapter<SingleRowCheckout>) listView.getAdapter();

                            arrayAdapter.remove(singleRowCheckout);
                            arrayAdapter.notifyDataSetChanged();
                            selectedIndex=-1;
                        }
                    }
                }
                break;
            }

            case R.id.btnEnter: {
                if(selectedIndex!=-1) {

                    SingleRowCheckout singleRowCheckout = (SingleRowCheckout) listView.getItemAtPosition(selectedIndex);


                    String isPrinted=singleRowCheckout.getCanmodify();

                    if (isPrinted==null)
                    {
                        isPrinted="0";
                    }
                    if(Integer.parseInt( isPrinted)==1  )
                    {
                        String prev_qty=singleRowCheckout.getQty();
                        String new_qty=itemqty;
                        if(Double.parseDouble(prev_qty)<=Double.parseDouble(new_qty))
                        {
                            View vw= listView.getChildAt(selectedIndex);
                            TextView txtqty= ((TextView) vw.findViewById(R.id.txtQty));
                            if (itemqty!="") {
                                txtqty.setText(itemqty);
                                ArrayAdapter<SingleRowCheckout> arrayAdapter = (ArrayAdapter<SingleRowCheckout>) listView.getAdapter();
                                singleRowCheckout.setQty(itemqty);
                                arrayAdapter.notifyDataSetChanged();
                            }
                            itemqty="";
                        }
                        else {
                            Toast.makeText(getActivity(), "Already Printed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        View vw= listView.getChildAt(selectedIndex);
                        TextView txtqty= ((TextView) vw.findViewById(R.id.txtQty));
                        if (itemqty!="") {
                            txtqty.setText(itemqty);
                            ArrayAdapter<SingleRowCheckout> arrayAdapter = (ArrayAdapter<SingleRowCheckout>) listView.getAdapter();
                            singleRowCheckout.setQty(itemqty);
                            arrayAdapter.notifyDataSetChanged();
                        }
                        itemqty="";
                    }
                }
                break;
            }
            //.... etc
        }

    }


}
