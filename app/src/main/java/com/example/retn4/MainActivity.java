package com.example.retn4;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button hindi_ln;
    Button english_ln;
    Intent login_signup_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_view);
        hindi_ln = (Button) findViewById(R.id.hindi_ln);
        english_ln = (Button) findViewById(R.id.english_ln);
        login_signup_intent = new Intent(this,login_signup.class);



        Paper.init(this);

        String language = Paper.book().read("language");
        if(language!=null)
//            Paper.book().write("language","en");
            startActivity(login_signup_intent);



//        updateView((String)Paper.book().read("language"));

        hindi_ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Paper.book().write("language","hi");
                updateView((String)Paper.book().read("language"));

            }
        });

        english_ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("language","en");
                updateView((String)Paper.book().read("language"));
            }
        });


    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this,language);
        Resources resources = context.getResources();

        textView.setText(resources.getString(R.string.heaven));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }
}