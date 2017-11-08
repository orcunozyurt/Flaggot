package com.nerdz.flaggot.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.nerdz.flaggot.R;


public class MyCustomProgressDialog extends ProgressDialog {

    private AnimationDrawable animation;

    public static ProgressDialog ctor(Context context, int theme) {
        MyCustomProgressDialog dialog = new MyCustomProgressDialog(context,theme);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    public MyCustomProgressDialog(Context context) {
        super(context);
    }

    public MyCustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_custom_progress_dialog);

    }

    @Override
    public void show() {
        try {
            super.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}

