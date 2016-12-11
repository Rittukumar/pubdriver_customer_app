package com.oceanstyxx.pubdriver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.oceanstyxx.pubdriver.R;

/**
 * Created by mohsin on 27/11/16.
 */

public class TextViewOutLined extends TextView
{
    public static int textSize = 30;  //The size of the text can be changed later
    public TextViewOutLined(Context context)
    {
        super(context);
        init();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setBackgroundResource(R.drawable.custom_shape);
    }
    private void init(){
        setTextSize(textSize);
        setPadding(5, 5, 5, 5);  //This will set the padding, and by increasing decreasing this
// parameters the text padding can be changed
    }
}
