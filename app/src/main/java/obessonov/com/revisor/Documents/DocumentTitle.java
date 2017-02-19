package obessonov.com.revisor.Documents;


public class DocumentTitle {
    private int doc_id;
    private String doc_date;
    private String doc_state;

    public void setDoc_state(String doc_state) {
        this.doc_state = doc_state;
    }

    public String getDoc_state() {

        return doc_state;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public String getDoc_date() {
        return doc_date;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public void setDoc_date(String doc_date) {
        this.doc_date = doc_date;
    }
}
