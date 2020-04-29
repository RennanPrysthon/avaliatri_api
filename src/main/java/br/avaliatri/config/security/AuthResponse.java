package br.avaliatri.config.security;

public class AuthResponse {
    private String token;
    private Integer user_id;

    public AuthResponse(String token, Integer user_id) {
        this.token = token;
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
