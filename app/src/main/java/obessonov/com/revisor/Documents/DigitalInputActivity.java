package obessonov.com.revisor.Documents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import obessonov.com.revisor.Utils.Constant;
import obessonov.com.revisor.R;

public class DigitalInputActivity extends Activity implements View.OnClickListener{

    private Button btnDigit0;
    private Button btnDigit1;
    private Button btnDigit2;
    private Button btnDigit3;
    private Button btnDigit4;
    private Button btnDigit5;
    private Button btnDigit6;
    private Button btnDigit7;
    private Button btnDigit8;
    private Button btnDigit9;
    private Button btnDigitOk;
    private Button btnDigitDot;
    private Button btnDigitDel;
    private TextView tvDigitResult;
    private TextView tvCaptionDigitalInput;

    StringBuilder sbResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_digital_input);

        btnDigit0     = (Button) findViewById(R.id.btnDigit0);
        btnDigit1     = (Button) findViewById(R.id.btnDigit1);
        btnDigit2     = (Button) findViewById(R.id.btnDigit2);
        btnDigit3     = (Button) findViewById(R.id.btnDigit3);
        btnDigit4     = (Button) findViewById(R.id.btnDigit4);
        btnDigit5     = (Button) findViewById(R.id.btnDigit5);
        btnDigit6     = (Button) findViewById(R.id.btnDigit6);
        btnDigit7     = (Button) findViewById(R.id.btnDigit7);
        btnDigit8     = (Button) findViewById(R.id.btnDigit8);
        btnDigit9     = (Button) findViewById(R.id.btnDigit9);
        btnDigitDot   = (Button) findViewById(R.id.btnDigitDot);
        btnDigitOk    = (Button) findViewById(R.id.btnDigitOk);
        btnDigitDel   = (Button) findViewById(R.id.btnDigitDel);
        tvDigitResult = (TextView) findViewById(R.id.tvDigitResult);
        tvCaptionDigitalInput = (TextView) findViewById(R.id.tvCaptionDigitalInput);

        btnDigit0.setOnClickListener(this);
        btnDigit1.setOnClickListener(this);
        btnDigit2.setOnClickListener(this);
        btnDigit3.setOnClickListener(this);
        btnDigit4.setOnClickListener(this);
        btnDigit5.setOnClickListener(this);
        btnDigit6.setOnClickListener(this);
        btnDigit7.setOnClickListener(this);
        btnDigit8.setOnClickListener(this);
        btnDigit9.setOnClickListener(this);
        btnDigitOk.setOnClickListener(this);
        btnDigitDot.setOnClickListener(this);
        btnDigitDel.setOnClickListener(this);

        sbResult = new StringBuilder();

        Intent intent = getIntent();
        String textCaption = intent.getStringExtra(Constant.CAPTION_DIGITAL_INPUT);
        int requestCode = intent.getIntExtra(Constant.DIGITALINPUT_REQUEST_CODE,0);
        if(requestCode == Constant.CALL_DIGITALINPUT_FOR_QTY) {
            tvDigitResult.setHint(R.string.inputQtyText);
        }
        tvCaptionDigitalInput.setText(textCaption);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDigit0:
                addNumber("0");
                break;
            case R.id.btnDigit1:
                addNumber("1");
                break;
            case R.id.btnDigit2:
                addNumber("2");
                break;
            case R.id.btnDigit3:
                addNumber("3");
                break;
            case R.id.btnDigit4:
                addNumber("4");
                break;
            case R.id.btnDigit5:
                addNumber("5");
                break;
            case R.id.btnDigit6:
                addNumber("6");
                break;
            case R.id.btnDigit7:
                addNumber("7");
                break;
            case R.id.btnDigit8:
                addNumber("8");
                break;
            case R.id.btnDigit9:
                addNumber("9");
                break;
            case R.id.btnDigitDel:
                delNuumber();
                break;
            case R.id.btnDigitDot:
                addNumber(".");
                break;
            case R.id.btnDigitOk:
                sendDataAndExit();
                break;
        }
    }

    private void addNumber(String number) {
        if(number == ".") {
            if (sbResult.indexOf(".") < 0 && sbResult.length() > 0){
                sbResult.append(".");
            }
        } else {
            sbResult.append(number);
        }
        printResul();
    }

    private void delNuumber() {
        sbResult.setLength(Math.max(sbResult.length() - 1, 0));
        printResul();
    }

    public void printResul() {
        tvDigitResult.setText(sbResult.toString());
    }

    public void sendDataAndExit() {
        Intent intent = new Intent();
        intent.putExtra(Constant.RESULT_DIGITAL_INPUT,sbResult.toString());
        setResult(RESULT_OK,intent);
        finish();
    }

}
