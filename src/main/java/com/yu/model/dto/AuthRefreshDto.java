package com.yu.model.dto;

public class AuthRefreshDto {

    public final String id_token = ""; // not supported
    public String access_token;
    public final String token_type = "Bearer";
    public long expires_in; // in seconds

}
