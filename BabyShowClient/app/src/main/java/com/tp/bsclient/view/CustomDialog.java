package com.tp.bsclient.view;

import android.app.Dialog;
import android.content.Context;

import com.tp.bsclient.R;

/**
 * Created by Administrator on 2015/12/10.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void create() {
        super.create();
        setContentView(R.layout.dialog_addalbum);
    }

}
