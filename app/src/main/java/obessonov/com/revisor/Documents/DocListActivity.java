package obessonov.com.revisor.Documents;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import obessonov.com.revisor.DAO;
import obessonov.com.revisor.R;
import obessonov.com.revisor.Utils.Constant;

public class DocListActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lvDocList;
    ArrayList<DocumentTitle> docArList;
    Button btnAddDocument;
    ArrayAdapter<DocumentTitle> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnAddDocument = (Button) findViewById(R.id.btnAddDocument);
        btnAddDocument.setOnClickListener(this);

        lvDocList = (ListView) findViewById(R.id.lvDocList);

        updateList(false);
        adapter = new DocListAdapter(this);
        lvDocList.setAdapter(adapter);
        registerForContextMenu(lvDocList);

        lvDocList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDocument(position);
            }
        });


    }



    public class DocListAdapter extends ArrayAdapter<DocumentTitle>{

        public DocListAdapter(Context context) {
            super(context, R.layout.item_doclist ,docArList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DocumentTitle documentTitle = getItem(position);
            String docInfo = "Документ № " + documentTitle.getDoc_id() + " от " + documentTitle.getDoc_date();

            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_doclist,null);
            }

            ((TextView) convertView.findViewById(R.id.tvDocInfo)).setText(docInfo);
            ((TextView) convertView.findViewById(R.id.tvDocState)).setText(documentTitle.getDoc_state());

            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddDocument){
            openDocument(-1);
        }
        /*
        DAO dao = new DAO(this);
        dao.testInsertDocument();
        updateList(true);
        */
    }

    private void updateList(boolean updateNotify) {
        DAO dao = new DAO(this);
        docArList = dao.getDocumentList();

        if(updateNotify){
            adapter.clear();
            adapter.addAll(dao.getDocumentList());
            adapter.notifyDataSetChanged();
        } else {
            docArList = dao.getDocumentList();
        }
    }


    private void openDocument(int pos){
        long id;
        DocumentTitle dt;

        if(pos == -1){
            id = 0;
        }else
        {
          dt = docArList.get(pos);
          id = dt.getDoc_id();
        }

        Intent intent = new Intent(this,Document.class);
        intent.putExtra(Constant.DOC_ID,id);
        startActivity(intent);
    }


}
