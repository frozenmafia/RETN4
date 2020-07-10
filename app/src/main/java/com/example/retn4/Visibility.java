package com.example.retn4;

import android.view.View;

public class Visibility {
    public static void makeVisible(View...views){
        for(View view:views){
            view.setVisibility(View.VISIBLE);
        }
    }
    public static void makeGone(View... views){
        for(View view:views){
            view.setVisibility(View.GONE);
        }
    }
    public  static void makeInvisible(View... views){
        for(View view:views){
            view.setVisibility(View.INVISIBLE);
        }
    }
}
