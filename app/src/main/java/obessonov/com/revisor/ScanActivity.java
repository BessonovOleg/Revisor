package obessonov.com.revisor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import obessonov.com.revisor.Documents.DigitalInputActivity;
import obessonov.com.revisor.Documents.DocRow;
import obessonov.com.revisor.Utils.Constant;

public class ScanActivity extends AppCompatActivity {

    EditText edScanCode;
    ArrayList<DocRow> docRows;

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

        docRows = getIntent().getParcelableArrayListExtra("docRows");
    }

    private void addEntity(String barcode) {
        DAO dao = new DAO(this);
        Entity entity = dao.getEntityByCode(barcode);

        if (entity.getEnt_id() > 0) {
            Intent intent = new Intent(this, DigitalInputActivity.class);
            String inputText = entity.getEnt_name();
            intent.putExtra(Constant.CAPTION_DIGITAL_INPUT, inputText);
            intent.putExtra(Constant.DIGITALINPUT_REQUEST_CODE, Constant.CALL_DIGITALINPUT_FOR_QTY);
            startActivityForResult(intent, Constant.CALL_DIGITALINPUT_FOR_QTY);
        }

    }


}
