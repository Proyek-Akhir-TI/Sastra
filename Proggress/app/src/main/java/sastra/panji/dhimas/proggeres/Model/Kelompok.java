package sastra.panji.dhimas.proggeres.Model;

public class Kelompok {
    private  String kelompok_id, kelompok_nama;

    public Kelompok(String kelompok_id, String kelompok_nama) {
        this.kelompok_id = kelompok_id;
        this.kelompok_nama = kelompok_nama;
    }

    public Kelompok() {
    }

    public String getKelompok_id() {
        return kelompok_id;
    }

    public void setKelompok_id(String kelompok_id) {
        this.kelompok_id = kelompok_id;
    }

    public String getKelompok_nama() {
        return kelompok_nama;
    }

    public void setKelompok_nama(String kelompok_nama) {
        this.kelompok_nama = kelompok_nama;
    }

    @Override
    public String toString() {
        return kelompok_nama;
    }
}
