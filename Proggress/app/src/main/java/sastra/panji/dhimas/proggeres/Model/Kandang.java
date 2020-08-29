package sastra.panji.dhimas.proggeres.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Kandang implements Parcelable {
    public static final Creator<Kandang> CREATOR = new Creator<Kandang>() {
        @Override
        public Kandang createFromParcel(Parcel source) {
            return new Kandang(source);
        }

        @Override
        public Kandang[] newArray(int size) {
            return new Kandang[size];
        }
    };
    int isActive;
    private String name;
    private String url;
    private String id;
    private String telpon;
    private String foto;
    private String latitude, longitude;
    private List<String> kandang;

    public Kandang() {
    }

    protected Kandang(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.id = in.readString();
        this.telpon = in.readString();
        this.foto = in.readString();
        this.isActive = in.readInt();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.kandang = in.createStringArrayList();
    }

    public String getTelpon() {
        return telpon;
    }

    public void setTelpon(String telpon) {
        this.telpon = telpon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.id);
        dest.writeString(this.telpon);
        dest.writeString(this.foto);
        dest.writeInt(this.isActive);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeStringList(this.kandang);
    }
}
