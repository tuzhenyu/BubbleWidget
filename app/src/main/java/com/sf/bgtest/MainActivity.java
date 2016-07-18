package com.sf.bgtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    ViewGroup content;
    BubbleWidget bubbleWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = (ViewGroup) findViewById(R.id.layout_content);
        bubbleWidget = (BubbleWidget) findViewById(R.id.bubble);
    }

    int indexP = 0;
    int[] paddings = {0,8,16,48,64,0};

    public void addPadding (View v){
        if(indexP < paddings.length - 1){
            ++indexP;
        }else{
            indexP = 0;
        }
        int padding = paddings[indexP];
        bubbleWidget.setPadding(padding,padding,padding,padding);
    }

    int indexS = 0;
    int[] sizes = {32,64,124,248,264};
    public void changeSize (View v){
        if(indexS < sizes.length - 1){
            ++indexS;
        }else{
            indexS = 0;
        }
        int size = sizes[indexS];
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bubbleWidget.getLayoutParams();
        params.width = size;
        params.height = size;
        bubbleWidget.setLayoutParams(params);
    }

    int indexBP = 0;
    int[] bpaddings = {0,8,16,48,64,0};
    public void changBPadding (View v){
        if(indexBP < bpaddings.length - 1){
            ++indexBP;
        }else{
            indexBP = 0;
        }
        int padding = bpaddings[indexBP];
        bubbleWidget.setBPaddingTop(padding);
    }


    public void addView (View v){
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.app_launcher);
        content.addView(imageView);
    }


    public void removeView (View v){
        if(content.getChildCount() > 0){
            content.removeViewAt(0);
        }
    }

    int indexC = 0;

    public void changColor (View v){
        int[] colors = {
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.bg_blue),
                getResources().getColor(R.color.gray),
                getResources().getColor(R.color.swipe_yellow)
        };
        if(indexC < colors.length - 1){
            ++indexC;
        }else{
            indexC = 0;
        }
        int color = colors[indexC];
        bubbleWidget.setStaticBackGround(color);
    }

    int indexR = 0;
    int[] radiuss = {
            8,
            16,
            24,
            32,
            0
    };
    public void changRadius(View v){
        if(indexR < radiuss.length - 1){
            ++indexR;
        }else{
            indexR = 0;
        }
        int radius = radiuss[indexR];
        bubbleWidget.setBRadius(radius);
    }

    int indexT = 0;
    int[] HTS = {
            8,
            16,
            24,
            32,
            0
    };
    public void changHTriangle(View v){
        if(indexT < HTS.length - 1){
            ++indexT;
        }else{
            indexT = 0;
        }
        int length = HTS[indexT];
        bubbleWidget.setLengthOfTriangle(length);

    }


}
