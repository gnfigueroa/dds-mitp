package dds.servicios.apiHogares;

import java.util.ArrayList;
import java.util.List;

public class RespuestaApiHogares {
    private String total;
    private String offset;
    private List<HogarDeTransito> hogares = new ArrayList<>();

    public RespuestaApiHogares(String total, String offset, List<HogarDeTransito> hogares) {
        this.total = total;
        this.offset = offset;
        this.hogares = hogares;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public List<HogarDeTransito> getHogares() {
        return hogares;
    }

    public void setHogares(List<HogarDeTransito> hogares) {
        this.hogares = hogares;
    }
}
