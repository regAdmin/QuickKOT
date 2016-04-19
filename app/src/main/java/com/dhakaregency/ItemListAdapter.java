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
 * Created by Administrator on 08/03/2016.
 */
class SingleRow
        {
            String codes;
            String descriptions;
            String saless;

            SingleRow(String descriptions,String saless,String _code)
            {
                this.descriptions=descriptions;
                this.saless=saless;
                this.codes=_code;
            }
        }

class SingleRowCheckout
{
    private String codes;
    private String descriptions;
    private String saless;
    private String qty;
    private String Preparation;
    private String cost;
    private String canmodify;
    SingleRowCheckout()
    {

    }
    SingleRowCheckout(String codes,String descriptions,String qtys,String saless,String costs)
    {
        this.setCodes(codes);
        this.setDescriptions(descriptions);
        this.setSaless(saless);
        this.setQty(qtys);
        this.setCost(costs);
    }
    SingleRowCheckout(String codes,String descriptions,String qtys,String saless,String costs,String _canmodify)
    {
        this.setCodes(codes);
        this.setDescriptions(descriptions);
        this.setSaless(saless);
        this.setQty(qtys);
        this.setCost(costs);
        this.setCanmodify(_canmodify);
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getSaless() {
        return saless;
    }

    public void setSaless(String saless) {
        this.saless = saless;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPreparation() {
        return Preparation;
    }

    public void setPreparation(String preparation) {
        Preparation = preparation;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCanmodify() {
        return canmodify;
    }

    public void setCanmodify(String canmodify) {
        this.canmodify = canmodify;
    }
}

public class ItemListAdapter extends BaseAdapter {
    ArrayList<SingleRow> list;
    Context _context;
    ItemListAdapter(Context context,ArrayList<Item> listArrayList)
    {
        list=new ArrayList<SingleRow>();
        _context=context;
        int i=0;
        for(Item item:listArrayList){

            list.add(new SingleRow(item.getDescription(),item.getSales(),item.getCode()));
            i++;
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row= layoutInflater.inflate(R.layout.item_single_row, parent,false );
        TextView tdesc= (TextView) row.findViewById(R.id.txtDescription);
        TextView tsales= (TextView) row.findViewById(R.id.txtSalesPrice);
        TextView tcode=(TextView) row.findViewById(R.id.txtItemCodes);
     SingleRow temp= list.get(position);

        tdesc.setText(temp.descriptions);
        tsales.setText(temp.saless);
        tcode.setText(temp.codes);
        return row;
    }
}
