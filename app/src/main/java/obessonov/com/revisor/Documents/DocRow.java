package obessonov.com.revisor.Documents;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import obessonov.com.revisor.DAO;
import obessonov.com.revisor.Entity;


public class DocRow implements Parcelable {
    private Integer rowNo;
    private Integer entID;
    private Double qty;
    private Entity entity;

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    public void setEntity(int ID, Context ctx) {
        entity = new Entity(ID,ctx);
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public Integer getEntID() {
        return entID;
    }

    public void setEntID(Integer id){
        this.entID = id;
    }

    public Double getQty() {
        return qty;
    }


    public DocRow(){
        super();
        rowNo = 0;
        qty = 0.0;
        entID = 0;
    }

    public Entity getEntity(){
        return this.entity;
    }

    //ParcelConstructor
    public DocRow(Parcel parcel){
        Bundle bundle = parcel.readBundle();
               rowNo  = bundle.getInt("rowNo");
               qty    = bundle.getDouble("qty");
               entID  = bundle.getInt("entID");
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
        bundle.putInt("entID",entID);
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Qty=");
        sb.append(qty);
        sb.append(" RowNo=");
        sb.append(rowNo);
        sb.append(" EntID = ");
        sb.append(entID);
        return sb.toString();
    }
}
