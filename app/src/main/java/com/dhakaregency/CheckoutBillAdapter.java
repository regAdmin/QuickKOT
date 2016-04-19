package com.dhakaregency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dhakaregency.quickkot.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 08/03/2016.
 */
public class CheckoutBillAdapter extends ArrayAdapter<SingleRowCheckout> {
    private Context _context;
    private ArrayList<SingleRowCheckout> singleRowCheckouts;
    private static LayoutInflater inflater = null;


    public CheckoutBillAdapter (Context context, int textViewResourceId,ArrayList<SingleRowCheckout> _singleRowCheckouts) {
        super(context, textViewResourceId, _singleRowCheckouts);
        try {
            this._context= context;
            this.singleRowCheckouts = _singleRowCheckouts;


        } catch (Exception e) {

        }
    }

    public int getCount() {
        return singleRowCheckouts.size();
    }

    public SingleRowCheckout getItem(SingleRowCheckout position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        View row=null;

        try {
                LayoutInflater layoutInflater= (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=layoutInflater.inflate(R.layout.single_row_checkout, parent,false );



                TextView tcode=(TextView) row.findViewById(R.id.txtCode);
                TextView tdescription=(TextView) row.findViewById(R.id.txtDescription);;
                TextView tsales=(TextView) row.findViewById(R.id.txtSalesPrice);
                TextView tqty=(TextView) row.findViewById(R.id.txtQty);



            SingleRowCheckout temp= singleRowCheckouts.get(position);

            if(temp!=null) {
                tcode.setText(temp.getCodes());
                tdescription.setText(temp.getDescriptions());
                tqty.setText(temp.getQty());
                tsales.setText(temp.getSaless());

            }


        } catch (Exception e) {


        }
        return row;
    }
}
