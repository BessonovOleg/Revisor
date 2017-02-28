package obessonov.com.revisor.Documents;


public class DocumentTitle {
    private Long   doc_id;
    private String doc_date;
    private int    doc_state;
    private String document_caption;
    private String doc_ag_guid;
    private String doc_no;

    public void setDocument_caption(String document_caption) {
        this.document_caption = document_caption;
    }

    public String getDocument_caption() {
        return document_caption;
    }

    public void setDoc_state(int doc_state) {
        this.doc_state = doc_state;
    }

    public int getDoc_state() {

        return doc_state;
    }

    public Long getDoc_id() {
        return doc_id;
    }

    public String getDoc_date() {
        return doc_date;
    }

    public void setDoc_id(Long doc_id) {
        this.doc_id = doc_id;
    }

    public void setDoc_date(String doc_date) {
        this.doc_date = doc_date;
    }

    public String getDoc_ag_guid() {
        return doc_ag_guid;
    }

    public void setDoc_ag_guid(String doc_ag_guid) {
        this.doc_ag_guid = doc_ag_guid;
    }

    public String getDoc_no() {
        return doc_no;
    }

    public void setDoc_no(String doc_no) {
        this.doc_no = doc_no;
    }
}
