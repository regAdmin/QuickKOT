package com.dhakaregency;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 12/03/2016.
 */
public class Final_Bill implements Parcelable{

    private  String itemCode;
    private String itemDescription;
    private String itemQty;
    private  String itemPrep;
    private String itemSalesPrice;
    private String itemCostPrice;
    private String itemKitchen;

    public  Final_Bill(){

    }

    public  Final_Bill(String _itemCode,String _itemDescription,String _itemQty,String _itemPrep,
                       String _itemSalesPrice,String _itemCostPrice,String _itemKitchen){
        this.setItemCode(_itemCode);
        this.setItemDescription(_itemDescription);
        this.setItemQty(_itemQty);
        this.setItemPrep(_itemPrep);
        this.setItemSalesPrice(_itemSalesPrice);
        this.setItemCostPrice(_itemCostPrice);
        this.setItemKitchen(_itemKitchen);

    }
    public Final_Bill(Parcel input) {
        this.setItemCode(input.readString());
        this.setItemDescription(input.readString());
        this.setItemQty(input.readString());
        this.setItemPrep(input.readString());
        this.setItemSalesPrice(input.readString());
        this.setItemCostPrice(input.readString());
        this.setItemKitchen(input.readString());
    }

    public static final Creator<Final_Bill> CREATOR = new Creator<Final_Bill>() {
        @Override
        public Final_Bill createFromParcel(Parcel in) {
            return new Final_Bill(in);
        }

        @Override
        public Final_Bill[] newArray(int size) {
            return new Final_Bill[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getItemCode());
        dest.writeString(this.getItemDescription());
        dest.writeString(this.getItemQty());
        dest.writeString(this.getItemPrep());
        dest.writeString(this.getItemSalesPrice());
        dest.writeString(this.getItemCostPrice());
        dest.writeString(this.getItemKitchen());

    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemPrep() {
        return itemPrep;
    }

    public void setItemPrep(String itemPrep) {
        this.itemPrep = itemPrep;
    }

    public String getItemSalesPrice() {
        return itemSalesPrice;
    }

    public void setItemSalesPrice(String itemSalesPrice) {
        this.itemSalesPrice = itemSalesPrice;
    }

    public String getItemCostPrice() {
        return itemCostPrice;
    }

    public void setItemCostPrice(String itemCostPrice) {
        this.itemCostPrice = itemCostPrice;
    }

    public String getItemKitchen() {
        return itemKitchen;
    }

    public void setItemKitchen(String itemKitchen) {
        this.itemKitchen = itemKitchen;
    }
}
