package com.dhakaregency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dhakaregency.quickkot.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 13/03/2016.
 */
public class KOTAdapter  extends BaseAdapter{
    private Context _context;
    private ArrayList<Final_Bill> final_bills;
    private static LayoutInflater inflater = null;

    public KOTAdapter()
    {

    }
    public KOTAdapter(Context context, int textViewResourceId,ArrayList<Final_Bill> _finalbill){
        try {
            this._context= context;
            this.final_bills= _finalbill;


        } catch (Exception e) {

        }
    }

    @Override
    public int getCount() {
        return final_bills.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=null;

        try {
            LayoutInflater layoutInflater= (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.kot_items_layout, parent,false );

            TextView tcode=(TextView) row.findViewById(R.id.txtKOTCode);
            TextView tdescription=(TextView) row.findViewById(R.id.txtKOTDescription);;
            TextView tqty=(TextView) row.findViewById(R.id.txtKOTQTY);
            TextView tpep=(TextView) row.findViewById(R.id.txtKOTPrep);
            TextView tsp=(TextView) row.findViewById(R.id.txtKOTSP);

            Final_Bill temp= final_bills.get(position);

            if(temp!=null) {
                tcode.setText(temp.getItemCode());
                tdescription.setText(temp.getItemDescription());
                tqty.setText(temp.getItemQty());
                tpep.setText(temp.getItemPrep());
                tsp.setText((temp.getItemSalesPrice()));
            }

        } catch (Exception e) {


        }
        return row;
    }
}
