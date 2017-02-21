package obessonov.com.revisor.Documents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import obessonov.com.revisor.R;
import obessonov.com.revisor.DAO;
import obessonov.com.revisor.Documents.DigitalInputActivity;
import obessonov.com.revisor.Documents.DocRow;
import obessonov.com.revisor.Entity;
import obessonov.com.revisor.Utils.Constant;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class ScanActivity extends AppCompatActivity {

    EditText edScanCode;
    ArrayList<DocRow> docRows;
    DocRow docRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        edScanCode = (EditText) findViewById(R.id.edScanCode);
        edScanCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addEntity(edScanCode.getText().toString());
                edScanCode.setText(null);
                return true;
            }
        });

        Intent intent = getIntent();
        docRows = intent.getParcelableArrayListExtra("docRows");

    }

    private void addEntity(String barcode) {
        DAO dao = new DAO(this);
        Entity entity = dao.getEntityByCode(barcode);
        docRow = new DocRow();

        if (entity.getEnt_id() > 0) {
            Intent intent = new Intent(this, DigitalInputActivity.class);
            String inputText = entity.getEnt_name();
            intent.putExtra(Constant.CAPTION_DIGITAL_INPUT, inputText);
            intent.putExtra(Constant.DIGITALINPUT_REQUEST_CODE, Constant.CALL_DIGITALINPUT_FOR_QTY);
            startActivityForResult(intent, Constant.CALL_DIGITALINPUT_FOR_QTY);
        }

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
            }

            if(!edScanCode.isFocused()) {
                edScanCode.requestFocus();
            }
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("docRows",docRows);
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }
}
