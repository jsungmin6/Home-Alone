package ga.homealoneapi.src.user.models;


import lombok.Setter;

@Setter
public final class AppleTokenResponse {
    private String access_token;
    private Long expires_in;
    private String id_token;
    private String refresh_token;
    private String token_type;
    private String error;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "TokenResponse [access_token=" + access_token + ", expires_in=" + expires_in + ", id_token=" + id_token
                + ", refresh_token=" + refresh_token + ", token_type=" + token_type + "]";
    }
}