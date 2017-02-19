package obessonov.com.revisor.Documents;


import obessonov.com.revisor.Entity;

public class DocRow {
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
}
