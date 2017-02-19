package obessonov.com.revisor;

public class Agent {
    private Integer ag_id;
    private String ag_guid;
    private String ag_name;

    public void setAg_id(Integer ag_id) {
        this.ag_id = ag_id;
    }

    public void setAg_guid(String ag_guid) {
        this.ag_guid = ag_guid;
    }

    public void setAg_name(String ag_name) {
        this.ag_name = ag_name;
    }

    public Integer getAg_id() {

        return ag_id;
    }

    public String getAg_guid() {
        return ag_guid;
    }

    public String getAg_name() {
        return ag_name;
    }
}
