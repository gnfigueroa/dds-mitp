package dds.servicios.apiHogares;

public class ReqBase {
    private String email;

    public ReqBase(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
