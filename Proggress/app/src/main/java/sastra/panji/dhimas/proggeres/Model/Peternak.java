package sastra.panji.dhimas.proggeres.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Peternak implements Parcelable {
    String nama;
    String token;
    String photo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;
    int id;

    public int getKelompok_id() {
        return kelompok_id;
    }

    public void setKelompok_id(int kelompok_id) {
        this.kelompok_id = kelompok_id;
    }

    int kelompok_id;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Peternak() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nama);
        dest.writeString(this.token);
        dest.writeString(this.photo);
        dest.writeString(this.email);
        dest.writeInt(this.id);
        dest.writeInt(this.kelompok_id);
    }

    private Peternak(Parcel in) {
        this.nama = in.readString();
        this.token = in.readString();
        this.photo = in.readString();
        this.email = in.readString();
        this.id = in.readInt();
        this.kelompok_id = in.readInt();
    }

    public static final Creator<Peternak> CREATOR = new Creator<Peternak>() {
        @Override
        public Peternak createFromParcel(Parcel source) {
            return new Peternak(source);
        }

        @Override
        public Peternak[] newArray(int size) {
            return new Peternak[size];
        }
    };
}
