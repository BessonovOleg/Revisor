package obessonov.com.revisor.Documents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import obessonov.com.revisor.DAO;
import obessonov.com.revisor.Entity;
import obessonov.com.revisor.R;
import obessonov.com.revisor.Utils.Constant;

public class Document extends Activity {

    private String   docCaption;
    private TextView lbDocCaption;
    private Spinner  spinnerWarehouse;
    private EditText edScanInput;
    private Button   btnHandInput;
    private Button   btnScan;

    private ArrayList<WareHouse> whList;
    private ArrayList<DocRow> docRows;

    private String   whGuid;
    private String   docDate;
    private ListView lvDocRows;

    private DocRow docRow;

    private Button btn55;
    private ArrayAdapter<DocRow> dlAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        docRows = new ArrayList<DocRow>();

        lvDocRows    = (ListView) findViewById(R.id.lvDocRows);
        lbDocCaption = (TextView) findViewById(R.id.lbDocCaption);
        edScanInput  = (EditText) findViewById(R.id.edScanInput);

        btnHandInput = (Button)   findViewById(R.id.btnHandInput);
        btnScan      = (Button)   findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                intent.putParcelableArrayListExtra("docRows",docRows);
                startActivityForResult(intent,Constant.CALL_SCAN_ACTIVITY);
            }
        });



        edScanInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addEntity(edScanInput.getText().toString());
                edScanInput.setText("");
                return true;
            }
        });

        btnHandInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handAddEntity();
            }
        });
        initSpinnerWareHouse();

        dlAdapter = new DocLineAdapter(this);
        //dlAdapter.setNotifyOnChange(true);
        lvDocRows.setAdapter(dlAdapter);

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
            docRow.setEntity(entity);
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

                if(!edScanInput.isFocused()) {
                    edScanInput.requestFocus();
                }
        }

        if(requestCode == Constant.CALL_SCAN_ACTIVITY){
            if(resultCode == RESULT_OK) {
                Log.d("REVISOR_LOG","update adapter");
                Log.d("REVISOR_LOG","Size before update = " + String.valueOf(docRows.size()));
                docRows = data.getParcelableArrayListExtra("docRows");
                Log.d("REVISOR_LOG","Size after update = " + String.valueOf(docRows.size()));
                dlAdapter.notifyDataSetChanged();
            }
        }

    }

    public void recalc() {
        lbDocCaption.setText(docCaption);
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
            //return super.getView(position, convertView, parent);
        }
    }




}
