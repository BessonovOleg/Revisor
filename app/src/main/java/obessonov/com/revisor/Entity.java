package obessonov.com.revisor;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Entity implements Parcelable{

    private Integer ent_id;
    private String ent_guid;
    private String ent_name;
    private String barcode;

    public Entity() {
         ent_id = 0;
         ent_guid = "";
         ent_name = "";
         barcode = "";
    }

    public void setEnt_id(Integer ent_id) {
        this.ent_id = ent_id;
    }

    public Integer getEnt_id() {
        return ent_id;
    }

    public void setEnt_guid(String ent_guid) {
        this.ent_guid = ent_guid;
    }

    public void setEnt_name(String ent_name) {
        this.ent_name = ent_name;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getEnt_guid() {
        return ent_guid;
    }

    public String getEnt_name() {
        return ent_name;
    }

    public String getBarcode() {
        return barcode;
    }


    //Parcelable
    public Entity(Parcel parcel){
        Bundle bundle = parcel.readBundle();
        ent_id   = bundle.getInt("ent_id");
        ent_guid = bundle.getString("ent_guid");
        ent_name = bundle.getString("ent_name");
        barcode  = bundle.getString("barcode");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt("ent_id",ent_id);
        bundle.putString("ent_guid",ent_guid);
        bundle.putString("ent_name",ent_name);
        bundle.putString("barcode",barcode);
        parcel.writeBundle(bundle);
    }

    public static final Parcelable.Creator<Entity> CREATOR = new Parcelable.Creator<Entity>(){
        @Override
        public Entity createFromParcel(Parcel in) {
            return new Entity(in);
        }

        @Override
        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };

}

