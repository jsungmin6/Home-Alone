package ga.homealoneapi.src.user.models;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public class SocialParametersDTO implements Serializable {

    private static final long serialVersionUID = 3484800209656475818L;

    // code varaible returned from sign in request
    private String authorizationCode;


    // If Apple sign in authoriation sends user object as string
    private String user;

    // id token from Apple Sign in Authorization if asked
    private String identityToken;

    // kid or key identifier from mobile app authorization
    private String identifierFromApp;

    @Override
    public String toString() {
        return "SocialParametersDTO[ authorizationCode=" + authorizationCode + " , identityToken=" +
                identityToken + ",identifierFromApp=" + identifierFromApp + ",user=" + user + ", " +
                "]";
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIdentityToken() {
        return identityToken;
    }
    public void setIdToken(String identityToken) {
        this.identityToken = identityToken;
    }
    public String getAuthorizationCode() {
        return authorizationCode;
    }
    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
    public String getIdentifierFromApp() {
        return identifierFromApp;
    }
    public void setIdentifierFromApp(String identifierFromApp) {
        this.identifierFromApp = identifierFromApp;
    }
}
