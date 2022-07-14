package dds.servicios.apiHogares;

public class Admision {
    private Boolean perros; //la variable tiene que tener el mismo nombre que el del JSON
    private Boolean gatos;  //la variable tiene que tener el mismo nombre que el del JSON

    public Admision(Boolean perros, Boolean gatos) {
        this.perros = perros;
        this.gatos = gatos;
    }

    public Boolean getPerros() {
        return perros;
    }

    public void setPerros(Boolean perros) {
        this.perros = perros;
    }

    public Boolean getGatos() {
        return gatos;
    }

    public void setGatos(Boolean gatos) {
        this.gatos = gatos;
    }
}
