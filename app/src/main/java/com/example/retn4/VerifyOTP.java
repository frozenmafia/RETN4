package com.example.retn4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyOTP extends AppCompatActivity {

    TextView otp_heading;
    Button verify_btn;
    EditText otp;
    String otp_error;
    AppService appService;
    public final static String LOG_TAG = "==========>>>>";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        initializeGlobalVariables();

        Paper.init(this);
        updateView((String)Paper.book().read("language"));
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().length()!=6)
                    otp.setError(otp_error);
                else
                    send_otp_for_verification();

            }
        });
    }

    private void send_otp_for_verification() {

        OTP_verification_format otp_verification_format = new OTP_verification_format(
                login_signup.ccp.getFormattedFullNumber(),
                otp.getText().toString(),
                login_signup.password.getText().toString());

        Call<JsonElement> call = appService.verify_otp(otp_verification_format);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.i(LOG_TAG,response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.i(LOG_TAG,t.getMessage());

            }
        });

    }

    private void initializeGlobalVariables() {
        otp_heading = (TextView)findViewById(R.id.otp_heading);
        verify_btn = (Button)findViewById(R.id.verify);
        otp = (EditText)findViewById(R.id.otp);
        appService = ServiceGenerator.createService(AppService.class);
    }

    @SuppressLint("ResourceType")
    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this,language);
        Resources resources = context.getResources();
        otp_heading.setText(resources.getString(R.string.otp_message));
        verify_btn.setText(resources.getString(R.string.verify));
        otp.setHint(resources.getString(R.string.otp));
        otp_error = resources.getString(R.string.otp_error);


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
            Paper.book().write("language", "en");
            updateView((String)Paper.book().read("language"));
        }
        else if(item.getItemId()==R.id.language_hi){
            Paper.book().write("language","hi");
            updateView((String)Paper.book().read("language"));
        }
        return true;
    }
}