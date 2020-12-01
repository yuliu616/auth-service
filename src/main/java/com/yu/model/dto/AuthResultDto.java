package com.yu.model.dto;

public class AuthResultDto {

    public final String id_token = ""; // not supported
    public String access_token;
    public final String refresh_token = ""; // not supported
    public final String token_type = "Bearer";

    public AuthResultDto(){}

    public AuthResultDto(String access_token) {
        this.access_token = access_token;
    }
}
