package com.dhakaregency;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * Created by Administrator on 19/04/2016.
 */
public class KotDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alerBilder=new AlertDialog.Builder (getActivity());
        alerBilder.setTitle("Confirmation");
        alerBilder.setMessage("Are You Sure To Post This Transaction?");
        alerBilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alerBilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return alerBilder.create();
    }
}

