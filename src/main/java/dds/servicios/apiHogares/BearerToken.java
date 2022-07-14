package dds.servicios.apiHogares;

public class BearerToken {
    private String bearer_token;

    public BearerToken(String bearer_token) {
        this.bearer_token = bearer_token;
    }

    public String getBearer_token() {
        return bearer_token;
    }

    public void setBearer_token(String bearer_token) {
        this.bearer_token = bearer_token;
    }
}
