package obessonov.com.revisor.Documents;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import obessonov.com.revisor.Entity;

public class DocRow implements Parcelable {
    private Integer rowNo;
    private Entity entity;
    private Double qty;

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public Entity getEntity() {
        return entity;
    }

    public Double getQty() {
        return qty;
    }


    public DocRow(){
    }

    //ParcelConstructor
    public DocRow(Parcel parcel){
        Bundle bundle = parcel.readBundle();
               rowNo  = bundle.getInt("rowNo");
               qty    = bundle.getDouble("qty");
               entity = bundle.getParcelable("entity");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt("rowNo",rowNo);
        bundle.putParcelable("entity",entity);
        bundle.putDouble("qty",qty);
        parcel.writeBundle(bundle);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public DocRow createFromParcel(Parcel in) {
            return new DocRow(in);
        }

        @Override
        public DocRow[] newArray(int size) {
            return new DocRow[size];
        }
    };

}
