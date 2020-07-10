package com.example.retn4;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AppService {

    @POST("generate-otp/")
    Call<JsonElement> generate_otp(@Body String phone_number);

    @POST("verify-otp/")
    Call<JsonElement> verify_otp(@Body OTP_verification_format otp);

    @POST("login/")
    Call<JsonElement> login(@Body Login_user login_user);

}
