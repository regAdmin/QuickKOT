package com.dhakaregency;

import android.view.ViewGroup;

/**
 * Created by Administrator on 02/03/2016.
 */
public interface Communicator {
    public void LoadSubMenu(String main_group_id);
    public void LoadItemList(String subgroup_id);
    public void ParseItem(SingleRowCheckout singleRow);
    public void UpdatePreparation(String prep);

}
