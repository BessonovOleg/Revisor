package obessonov.com.revisor;

public class Entity {

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
}

