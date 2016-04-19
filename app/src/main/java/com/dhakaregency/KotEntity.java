package com.dhakaregency;

import java.util.ArrayList;

/**
 * Created by Administrator on 16/03/2016.
 */
public class KotEntity {
    private String tableid;
    private String pax;
    private ArrayList<Final_Bill> items;

    public KotEntity(String _tableid,String _pax,ArrayList<Final_Bill>  _items)
    {
        this.tableid=_tableid;
        this.pax=_pax;
        this.items=_items;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }

    public ArrayList<Final_Bill> getItems() {
        return items;
    }

    public void setItems(ArrayList<Final_Bill> items) {
        this.items = items;
    }
}
