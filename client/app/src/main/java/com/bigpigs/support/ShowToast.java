package com.bigpigs.support;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {

    public static void showToastLong(Context context, String messenger) {
        Toast.makeText(context, messenger, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(Context context, String messenger) {
        Toast.makeText(context, messenger, Toast.LENGTH_SHORT).show();
    }

}
