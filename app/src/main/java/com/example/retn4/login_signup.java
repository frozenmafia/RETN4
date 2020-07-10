package com.example.retn4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.hbb20.CountryCodePicker;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class login_signup extends AppCompatActivity {

    Visibility visibility;
    Button login;
    Button signUp;
    static public EditText password;
    TextView signUp_text;
    TextView login_text;
    Boolean user_wants_to_signup;
    public final static String LOG_TAG = "==========>>>>";
    static public CountryCodePicker ccp;
    static public EditText phone_number;
    String phone_error,password_error;
    AppService appService;
    Intent verify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        initializeGlobalVariables();

        setVisiblityOfViews();

        Paper.init(this);
        updateView((String)Paper.book().read("language"));
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_wants_to_signup = true;
                setVisiblityOfViews();
            }
        });
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_wants_to_signup = false;
                setVisiblityOfViews();
            }
        });
        
        
        /*
        This function to login------as soon as login button is tapped user will be logged in
        */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate("login"))
                    login_the_user();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate("signup"))
                    signup_the_user();
            }
        });

        
    }

    private boolean validate(String to_beValidated) {


        int error;

        if(to_beValidated.equals("login")) {
            error = 0;
            if ((ccp.getFullNumber().length() - ccp.getSelectedCountryCode().length()) != 10) {
                phone_number.setError(phone_error);
                error = error + 1;
            }
            if (password.getText().toString().length() < 6) {
                password.setError(password_error);
                error = error + 1;
            }
            if (error == 0)
                return true;
            else
                return false;
        }
        else if(to_beValidated.equals("signup")){
            error = 0;
            if ((ccp.getFullNumber().length() - ccp.getSelectedCountryCode().length()) != 10) {
                phone_number.setError(phone_error);
                error = error + 1;
            }
            if(error ==0)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    private void signup_the_user() {

        Call<JsonElement> call = appService.generate_otp(ccp.getFormattedFullNumber());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.i(LOG_TAG,response.body().toString());
                if(response.body().getAsJsonObject().get("user_existence").getAsBoolean())
                    Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_LONG).show();
                else
                    startActivity(verify);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.i(LOG_TAG,t.getMessage());

            }
        });

    }


    private void login_the_user() {
        //I have declared a login class that defines a login user
        Login_user login_user = new Login_user(ccp.getFormattedFullNumber(),password.getText().toString());

        Call<JsonElement> call = appService.login(login_user);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.i(LOG_TAG,response.body().toString());
                if(response.body().getAsJsonObject().get("logined").toString().equals("null")){
                    Toast.makeText(getApplicationContext(),"User NOT registered",Toast.LENGTH_LONG).show();
                }
                else if(response.body().getAsJsonObject().get("logined").getAsBoolean() && response.body().getAsJsonObject().get("user_registered").getAsBoolean()){
                    Toast.makeText(getApplicationContext(),"User Logged In",Toast.LENGTH_LONG).show();
                }
                else if(!response.body().getAsJsonObject().get("logined").getAsBoolean() && response.body().getAsJsonObject().get("user_registered").getAsBoolean()){
                    Toast.makeText(getApplicationContext(),"Incorrect Password",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.i(LOG_TAG,t.getMessage());
            }
        });
    }

    private void setVisiblityOfViews() {
        visibility.makeGone(signUp,login_text);
        if(user_wants_to_signup){
            visibility.makeVisible(signUp,login_text);
            visibility.makeGone(login,signUp_text);
        }
        else{
            visibility.makeVisible(login,signUp_text);
            visibility.makeGone(signUp,login_text);
        }
    }

    private void initializeGlobalVariables() {
        login = (Button) findViewById(R.id.verify);
        signUp = (Button)findViewById(R.id.signUp);
        ccp = (CountryCodePicker)findViewById(R.id.ccp);
        phone_number = (EditText) findViewById(R.id.phoneText);
        ccp.registerCarrierNumberEditText(phone_number);
        password = (EditText) findViewById(R.id.otp);
        signUp_text = (TextView) findViewById(R.id.signUp_text);
        login_text = (TextView) findViewById(R.id.login_text);
        user_wants_to_signup = false;
        appService = ServiceGenerator.createService(AppService.class);
        verify = new Intent(this,VerifyOTP.class);
    }

    @SuppressLint("ResourceType")
    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this,language);
        Resources resources = context.getResources();

        login.setText(resources.getString(R.string.login));
        signUp.setText(resources.getString(R.string.sign_up));
        phone_number.setHint(resources.getString(R.string.phone));

        password.setHint(resources.getString(R.string.password));
        signUp_text.setText(resources.getString(R.string.sign_up));
        login_text.setText(resources.getString(R.string.login));
        phone_error = resources.getString(R.string.phone_error);
        password_error = resources.getString(R.string.password_error);
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
