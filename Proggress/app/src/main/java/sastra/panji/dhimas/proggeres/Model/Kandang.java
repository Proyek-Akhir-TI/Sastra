package sastra.panji.dhimas.proggeres.Model;

import java.util.List;

public class Kandang {
    private String name;
    private long lat,lang;
private List<String> kandang;

    public List<String> getKandang() {
        return kandang;
    }

    public void setKandang(List<String> kandang) {
        this.kandang = kandang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLang() {
        return lang;
    }

    public void setLang(long lang) {
        this.lang = lang;
    }
}
