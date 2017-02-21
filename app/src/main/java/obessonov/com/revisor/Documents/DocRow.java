package obessonov.com.revisor.Documents;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
        super();
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
        bundle.putDouble("qty",qty);
        bundle.putParcelable("entity",entity);
        parcel.writeBundle(bundle);
    }


    public static final Parcelable.Creator<DocRow> CREATOR = new Parcelable.Creator<DocRow>(){
        @Override
        public DocRow createFromParcel(Parcel inp) {
            return new DocRow(inp);
        }

        @Override
        public DocRow[] newArray(int size) {
            return new DocRow[size];
        }
    };

}
