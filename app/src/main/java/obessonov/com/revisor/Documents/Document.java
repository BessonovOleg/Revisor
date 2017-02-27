package obessonov.com.revisor.Documents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import obessonov.com.revisor.DAO;
import obessonov.com.revisor.Entity;
import obessonov.com.revisor.R;
import obessonov.com.revisor.Utils.Constant;

public class Document extends Activity {

    private String   docCaption;
    private TextView lbDocCaption;
    private Spinner  spinnerWarehouse;
    private Button   btnHandInput;
    private Button   btnScan;
    private Integer  docID;

    private ArrayList<WareHouse> whList;
    private ArrayList<DocRow> docRows;

    private String   whGuid;
    private String   docDate;
    private ListView lvDocRows;

    private DocRow docRow;

    private ArrayAdapter<DocRow> dlAdapter;

    private static int selected_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        docRows = new ArrayList<DocRow>();

        Intent intent = getIntent();
        docID = intent.getIntExtra("DOCID",0);
        loadDocument();

        lvDocRows    = (ListView) findViewById(R.id.lvDocRows);
        lbDocCaption = (TextView) findViewById(R.id.lbDocCaption);

        btnScan      = (Button)   findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ScanActivity.class);
                intent.putParcelableArrayListExtra("docRows",docRows);
                startActivityForResult(intent,Constant.CALL_SCAN_ACTIVITY);
            }
        });

        btnHandInput = (Button)   findViewById(R.id.btnHandInput);
        btnHandInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handAddEntity();
            }
        });

        initSpinnerWareHouse();


        dlAdapter = new DocLineAdapter(this);
        lvDocRows.setAdapter(dlAdapter);
        registerForContextMenu(lvDocRows);
    }

    public void initSpinnerWareHouse(){
        DAO dao = new DAO(this);
        whList = dao.getWareHouses();
        spinnerWarehouse = (Spinner) findViewById(R.id.spinnerWarehouse);

        //Convert ArrayList to Array
        String[] namesArr = new String[whList.size()];
        for (int i = 0; i < whList.size(); i++) {
            namesArr[i] = whList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,namesArr);
        spinnerWarehouse.setAdapter(adapter);
    }

    private void addEntity(String barcode) {
        DAO dao = new DAO(this);
        docRow = new DocRow();
        Entity entity = dao.getEntityByCode(barcode);

        if (entity.getEnt_id() > 0) {
            Intent intent = new Intent(this, DigitalInputActivity.class);
            String inputText = entity.getEnt_name();
            intent.putExtra(Constant.CAPTION_DIGITAL_INPUT, inputText);
            intent.putExtra(Constant.DIGITALINPUT_REQUEST_CODE, Constant.CALL_DIGITALINPUT_FOR_QTY);
            docRow.setEntity(entity.getEnt_id(),getApplicationContext());
            startActivityForResult(intent, Constant.CALL_DIGITALINPUT_FOR_QTY);
        }
    }

    private void handAddEntity(){
        Intent intent = new Intent(this,DigitalInputActivity.class);
        String inputText = getResources().getString(R.string.inputBarcodeText);
        intent.putExtra(Constant.CAPTION_DIGITAL_INPUT,inputText);
        intent.putExtra(Constant.DIGITALINPUT_REQUEST_CODE,Constant.CALL_DIGITALINPUT_FOR_BARCODE);
        startActivityForResult(intent,Constant.CALL_DIGITALINPUT_FOR_BARCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data == null) {return;}

        if(requestCode == Constant.CALL_DIGITALINPUT_FOR_BARCODE) {
            if(resultCode == RESULT_OK) {
                String code = data.getStringExtra(Constant.RESULT_DIGITAL_INPUT);
                addEntity(code);
            }
        }

        if(requestCode == Constant.CALL_DIGITALINPUT_FOR_QTY) {
            Double qty = 0.0;
            String restxt = data.getStringExtra(Constant.RESULT_DIGITAL_INPUT);
                try{
                   Double resDbl = Double.parseDouble(restxt);
                   qty = resDbl;
                }catch (Exception e) {
                   qty = 0.0;
                }

                if(qty > 0.0) {
                    docRow.setRowNo(docRows.size()+1);
                    docRow.setQty(qty);
                    docRows.add(docRow);
                    dlAdapter.notifyDataSetChanged();
                }
        }

        if(requestCode == Constant.CALL_SCAN_ACTIVITY){
            if(resultCode == RESULT_OK) {
                DocRow tmpDocRow;
                    ArrayList<DocRow> tmpdocRows = data.getParcelableArrayListExtra("docRows");

                    for (DocRow d:tmpdocRows){
                        tmpDocRow = new DocRow();
                        tmpDocRow.setEntID(d.getEntID());
                        tmpDocRow.setRowNo(docRows.size()+1);
                        tmpDocRow.setQty(d.getQty());
                        tmpDocRow.setEntity(d.getEntID(),getApplicationContext());
                        docRows.add(tmpDocRow);
                    }
                dlAdapter.notifyDataSetChanged();
            }
        }

    }


    private class DocLineAdapter extends ArrayAdapter<DocRow> {
        public DocLineAdapter(Context context) {
            super(context,android.R.layout.simple_list_item_2 ,docRows);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DocRow dr = getItem(position);
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2,null);
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(dr.getEntity().getEnt_name());
            ((TextView) convertView.findViewById(android.R.id.text2)).setText(dr.getQty().toString());
            return convertView;
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,Constant.DOC_MENU_CHANGE_ROW,0,R.string.cnx_menu_change);
        menu.add(0,Constant.DOC_MENU_DELETE_ROW,0,R.string.cnx_menu_delete);
        //super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Удалить строку
        if(item.getItemId() == Constant.DOC_MENU_DELETE_ROW){
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            deleteRow(acmi.position);
            return true;
        }

        return super.onContextItemSelected(item);
    }


    public void deleteRow(int pos){
        selected_position = pos;
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(R.string.dialog_caption);
        dlg.setMessage(R.string.dialog_text);
        dlg.setPositiveButton(R.string.dialog_btn_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // удаляем из коллекции, используя позицию пункта в списке
                if(selected_position >= 0){
                    docRows.remove(selected_position);
                    // уведомляем, что данные изменились
                    dlAdapter.notifyDataSetChanged();
                }
            }
        });
        dlg.setNegativeButton(R.string.dialog_btn_no,null);
        dlg.setCancelable(true);
        dlg.show();
    }



    private void loadDocument(){
        if (docID == 0) return;
        //Загрузка шапки
        DAO dao = new DAO(this);


    }


}

