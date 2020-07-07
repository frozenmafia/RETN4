package com.example.retn4;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_view);

        Paper.init(this);

        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language","en");

        updateView((String)Paper.book().read("language"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.language_en)
        {
            Paper.book().write("language","en");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.language_hi){

            Paper.book().write("language","hi");
            updateView((String)Paper.book().read("language"));
        }

        return true;
    }
}