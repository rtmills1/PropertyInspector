package sit374_team17.propertyinspector.Walkthrough;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class Electrical implements Parcelable {
    private String id;
    private String name;
    private Drawable logo;
    private String pitch;
    private String info;

    public void setId(String id) {this.id = id;}
    public String getId() {
        return id;
    }

    public void setName(String name) {this.name = name;}
    public String getName() {
        return name;
    }

    public void setLogo(Drawable logo) {this.logo = logo;}
    public Drawable getLogo() {
        return logo;
    }

    public void setPitch(String pitch) {this.pitch = pitch;}
    public String getPitch() {
        return pitch;
    }

    public void setInfo(String info) {this.info = info;}
    public String getInfo() {
        return info;
    }

    public Electrical(String id, String name, Drawable logo, String pitch, String info) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.pitch = pitch;
        this.info = info;
    }

    protected Electrical(Parcel in) {
        id = in.readString();
        name = in.readString();
        pitch = in.readString();
        info = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(pitch);
        dest.writeString(info);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Electrical> CREATOR = new Creator<Electrical>() {
        @Override
        public Electrical createFromParcel(Parcel in) {
            return new Electrical(in);
        }

        @Override
        public Electrical[] newArray(int size) {
            return new Electrical[size];
        }
    };

}
