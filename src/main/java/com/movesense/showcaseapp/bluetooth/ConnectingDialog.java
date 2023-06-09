package com.movesense.showcaseapp.bluetooth;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

import com.movesense.showcaseapp.R;

/**
 * Singleton for Connecting dialog
 */

public enum ConnectingDialog {
    INSTANCE;

    private AlertDialog alertDialog;

    public void showDialog(Context context, String deviceAddress) {
        if (alertDialog == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle(context.getString(R.string.connecting) + ": " + deviceAddress + " -anturiin")
                    .setMessage(R.string.please_wait_connecting);

            alertDialog = alertDialogBuilder.show();
        }
    }

    public void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}
