package com.iskandar.android.simplecalc;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView numView;
    double currentResult;
    double previousResult;
    int opCode; // 0:none(start), 1:+, 2:-, 3:X, 4:/  ... etc //

    boolean noError;
    boolean clearWelcomeTextStatus;
    boolean aboutMsgStatus;
    int imgClickCounter;

    Dialog eegg;
    int c11;
    TextView eeggView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting pointers block // initialization //
        numView =  findViewById(R.id.txtInput);
        currentResult=0;
        previousResult=0;
        opCode=0;
        noError=true;
        clearWelcomeTextStatus=false;
        aboutMsgStatus=false;
        imgClickCounter=0;
        eegg = new Dialog(this,R.style.Theme_AppCompat_Light_DialogWhenLarge);
        c11=0;
        eeggView = findViewById(R.id.txtEEgg);
        /////////////////////////////////////////////
        initializeEEggDialog();

    }

    private void initializeEEggDialog() {
        eegg.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        eegg.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        eegg.setCanceledOnTouchOutside(false);
    }

    //////////// initial methods block //////////////////////////
    private void initializeEEgg() { c11=0; eeggView.setText(getString(R.string.eeggStart));}
    private void setTxtColor()
    {
        numView.setTextColor(Color.rgb(0,255,0));
    }
    private void clearWelcome()
    {
        if(!clearWelcomeTextStatus)
        {
            clear();
            clearWelcomeTextStatus=true;
        }
    }
    private void clearError()
    {
        if(!noError)
        {
            clear();
            noError=true;
        }
    }
    private void clearAbout()
    {
        if(aboutMsgStatus)
        {
            clear();
            aboutMsgStatus=false;
        }
    }
    private void imgClickCounterReset()
    {
        imgClickCounter=0;
    }
    private void initialClear()
    {
        clearWelcome();
        clearError();
        clearAbout();
        imgClickCounterReset();
        setTxtColor();
        initializeEEgg();
    }
    ///////////////////////////////////////////////////////////

    /////////// click & calculate methods blocks /////////////
    public void onClickNum(View v){
        initialClear();

        switch (v.getId())
        {
            case R.id.btn0: insertNum(0); break;
            case R.id.btn1: insertNum(1); break;
            case R.id.btn2: insertNum(2); break;
            case R.id.btn3: insertNum(3); break;
            case R.id.btn4: insertNum(4); break;
            case R.id.btn5: insertNum(5); break;
            case R.id.btn6: insertNum(6); break;
            case R.id.btn7: insertNum(7); break;
            case R.id.btn8: insertNum(8); break;
            case R.id.btn9: insertNum(9); break;
        }
    }
    private void insertNum(int num)
    {
        StringBuilder tmp = new StringBuilder(numView.getText());
        StringBuilder result=new StringBuilder(""+(tmp.toString().equals("")?num:tmp.append(num)));
        numView.setText(result);
    }


    public void onClickDot(View v) { initialClear(); insertDot(); }
    private void insertDot()
    {
        StringBuilder currentNumTxt = new StringBuilder(numView.getText());
        if(currentNumTxt.indexOf(".")==-1) // if there is NO DOT already //
        {
            numView.setText(currentNumTxt.append("."));
        }
    }


    public void onClickClear(View v) { initialClear(); clear(); }
    private void clear() { numView.setText(""); }


    public void onClickOp(View v)
    {
        initialClear();

        currentResult=getCurrentResult();
        switch (v.getId())
        {
            case R.id.btnPlus: opCode=1; break;
            case R.id.btnMinus: opCode=2; break;
            case R.id.btnMultiply: opCode=3; break;
            case R.id.btnDivide: opCode=4; break;
        }
        clear();
    }

    private double getCurrentResult()
    {
        StringBuilder currentNumTxt = new StringBuilder(numView.getText());
        return currentNumTxt.toString().equals("")?0:Double.parseDouble(new String(currentNumTxt));
    }


    public void onClickResult(View v)
    {
        initialClear();

        previousResult=currentResult;
        currentResult=getCurrentResult();
        switch(opCode)
        {
            case 1: calcAdd(); break;
            case 2: calcSubtract(); break;
            case 3: calcMultiply(); break;
            case 4: calcDivide(); break;
            default: showError(1);
        }
        if(noError) showResult();
    }

    private void showResult() {
        numView.setText(new StringBuilder(""+currentResult));
    }

    private void calcDivide() {
        if(currentResult==0)
        {
            showError(0);
            return;
        }
        double tmp = currentResult;
        currentResult=previousResult/currentResult;
        previousResult=tmp;
    }

    private void calcMultiply() {
        double tmp = currentResult;
        currentResult=previousResult*currentResult;
        previousResult=tmp;
    }

    private void calcSubtract() {
        double tmp = currentResult;
        currentResult=previousResult-currentResult;
        previousResult=tmp;
    }

    private void calcAdd() {
        double tmp = currentResult;
        currentResult=previousResult+currentResult;
        previousResult=tmp;
    }

    ///////////////////////////////////////////////////////////

    /////////////// ERROR methods block //////////////////////

    private void showError(int errorCode) {
        noError=false;
        String errorStr="";
        switch(errorCode)
        {
            case 0: errorStr="DIVISION by ZERO"; break;
            case 1: errorStr="INVALID or NOT Entered operator!"; break;
            //case 2: break;
            default: showError();
        }
        numView.setText(new StringBuilder(errorStr));
    }

    private void showError() {
        noError=false;
        numView.setText(new StringBuilder("UNKNOWN ERROR"));
    }

    ///////////////////////////////////////////////////////////

    ///////////// EXTRA stuff block ///////////////////////////

    public void onClickAbout(View v)
    {
        initialClear();
        aboutMsgStatus=true;
        numView.setTextColor(Color.WHITE);
        numView.setText(getString(R.string.about));
    }

    public void onClickBottom(View v) {

        imgClickCounter+=1;

        switch (imgClickCounter)
        {
            case 1: Toast.makeText(this, "press on me!", Toast.LENGTH_SHORT).show();
                break;
            case 2: Toast.makeText(this, "press one more time to exit", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "Good-bye!", Toast.LENGTH_LONG).show();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }
    }

    public void onClickEEgg(View v)
    {
        if(aboutMsgStatus)
        {
            if(check11()) {
                eegg.show();
            }
        }
    }

    private boolean check11() {
        String tmpBinary = Integer.toBinaryString(++c11);
        eeggView.setText(new StringBuilder(tmpBinary));
        if(c11==11) Toast.makeText(this, ""+1011, Toast.LENGTH_LONG).show();
        return (c11 == 11);
    }

    public void dismissListener(View v)
    {
        initializeEEgg();
        eegg.dismiss();
    }
    ///////////////////////////////////////////////////////////

}
