package com.pax.gl.printertest;
/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2018-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 2019-01-18  	         michael.z          	Create
 * ===========================================================================================
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pax.gl.commhelper.IComm;
import com.pax.gl.commhelper.exception.CommException;
import com.pax.gl.commhelper.impl.PaxGLComm;
import com.pax.gl.extprinter.entity.BitmapLine;
import com.pax.gl.extprinter.entity.EAlign;
import com.pax.gl.extprinter.exception.PrintException;
import com.pax.gl.extprinter.impl.GLExtPrinter;
import com.pax.gl.extprinter.inf.ICommListener;
import com.pax.gl.extprinter.inf.IExtPrinter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestBtPrinterActivity extends Activity{
    public static String selectedDevice = "";
    public static String currentBtMac = "";
    private String tempMac = "";

    private String TAG = "TestBtPrinterActivity";
    private int currentTestCaseIndex = 0;

    private IComm comm = null;
    private PaxGLComm glComm = null;
    private IExtPrinter escPos = null;

    private Spinner spTestCases;

    private Button btTest;
    private Button btScanButton;

    private TextView spBtText;
    private TextView tvResult;

    private ProgressDialog progressDialog;

    //蓝牙通道
    private ICommListener btlistener = new ICommListener() {

        @Override
        public void onSend(byte[] data) throws com.pax.gl.extprinter.exception.CommException {
            if(comm != null) {
                try {
                    comm.send(data);
                } catch (com.pax.gl.commhelper.exception.CommException e) {
                    e.printStackTrace();
                    throw new com.pax.gl.extprinter.exception.CommException(com.pax.gl.extprinter.exception.CommException.ERR_SEND);
                }
            }else{
                Log.e(TAG, "comm is null, send error");
            }
        }

        @Override
        public byte[] onRecv(int expLen) throws com.pax.gl.extprinter.exception.CommException {
            byte[] data = new byte[0];
            if(comm != null) {
                try {
                    data = comm.recv(1);
                } catch (com.pax.gl.commhelper.exception.CommException e) {
                    e.printStackTrace();
                    throw new com.pax.gl.extprinter.exception.CommException(com.pax.gl.extprinter.exception.CommException.ERR_RECV);
                }

                return data;
            }else{
                Log.e(TAG, "comm is null, recv error");
                return new byte[0];
            }
        }

        @Override
        public void onReset() {
            if(comm != null) {
                comm.reset();
            }else{
                Log.e(TAG, "comm is null, reset error");
            }
        }
    };

    private static final String[] testCases = {
            "0-Connect",
            "1-DisConnect",
            "2-print text",
            "3-print barcode",
            "4-print bitmap",
            "5-get status",
    };

    private void doTest() {

        setResult("Testing " + testCases[currentTestCaseIndex]);
        progressDialog = ProgressDialog.show(TestBtPrinterActivity.this, "", "Testing...");
        progressDialog.setCancelable(false);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                doExecute();
            }

        });

        thread.start();
    }

    public void doExecute(){
        switch (currentTestCaseIndex) {

            case 0:// connect
            {
                if(connect(currentBtMac)){
                    updateResult("connect success!");
                }else{
                    updateResult("connect fail!");
                }

                progressDialog.dismiss();
                break;
            }

            case 1:// disconnect
            {
                disconnect();
                progressDialog.dismiss();
                break;
            }

            case 2:// print text
            {
                if(comm != null){
                    if(comm.getConnectStatus() == IComm.EConnectStatus.CONNECTED){
                        printText();
                    }else{
                        updateResult("bt not connected");
                    }
                }else{
                    updateResult("bt not connected");
                }

                progressDialog.dismiss();
                break;
            }

            case 3:// print barcode
            {
                if(comm != null){
                    if(comm.getConnectStatus() == IComm.EConnectStatus.CONNECTED){
                        printBarcode();
                    }else{
                        updateResult("bt not connected");
                    }
                }else{
                    updateResult("bt not connected");
                }

                progressDialog.dismiss();
                break;
            }

            case 4:// print bitmap
            {
                if(comm != null){
                    if(comm.getConnectStatus() == IComm.EConnectStatus.CONNECTED){
                        printBitmap();
                    }else{
                        updateResult("bt not connected");
                    }
                }else{
                    updateResult("bt not connected");
                }

                progressDialog.dismiss();
                break;
            }

            case 5:// get status
            {
                if(comm != null){
                    if(comm.getConnectStatus() == IComm.EConnectStatus.CONNECTED){
                        try {
                            int status = escPos.getStatus();
                            updateResult("printer status: " + status);
                        } catch (com.pax.gl.extprinter.exception.CommException e) {
                            e.printStackTrace();
                            updateResult("get status error");
                        }
                    }else{
                        updateResult("bt not connected");
                    }
                }else{
                    updateResult("bt not connected");
                }

                progressDialog.dismiss();
                break;
            }

        }
    }

    private void testCaseSelected(int index) {
        currentTestCaseIndex = index;
    }

    private void setResult(String result) {
        Message msg = new Message();
        msg.what = 0;
        Bundle data = new Bundle();
        data.putString("result", result);
        msg.setData(data);
        handler.sendMessage(msg);
    }

    private void updateResult(String result) {
        Message msg = new Message();
        msg.what = 1;
        Bundle data = new Bundle();
        data.putString("result", result);
        msg.setData(data);
        handler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_main);

        glComm = PaxGLComm.getInstance(TestBtPrinterActivity.this);
        escPos = GLExtPrinter.createEscPosPrinter(btlistener, 383);

        spBtText = (TextView) findViewById(R.id.spBtText);
        tvResult = (TextView) findViewById(R.id.bt_tvResult);

        btScanButton = (Button) findViewById(R.id.btScanBtn);
        btScanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestBtPrinterActivity.this, BluetoothDeviceListActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        spTestCases = (Spinner) findViewById(R.id.bt_spTestCases);
        List<HashMap<String, String>> testCasesList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < testCases.length; i++) {
            HashMap<String, String> testCase = new HashMap<String, String>();
            testCase.put("testcase", testCases[i]);

            testCasesList.add(testCase);
        }

        spTestCases.setAdapter(new SimpleAdapter(TestBtPrinterActivity.this, testCasesList, R.layout.testcase_item, new String[] { "testcase" }, new int[] { R.id.tvTestCase }));
        spTestCases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                testCaseSelected(arg2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        btTest = (Button) findViewById(R.id.btTest);
        btTest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doTest();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        spBtText.setText(selectedDevice);
        if(selectedDevice.length() >= 17){
            currentBtMac = selectedDevice.substring(selectedDevice.length() - 17);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(comm != null){
            try {
                comm.disconnect();
            } catch (CommException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean connect(String bluetoothId) {
        Log.d(TAG, ">>>bt connect");

        if (tempMac.equals("") || !isConnected()) {
            Log.d(TAG, "first connect...");
            comm =  glComm.createBt(bluetoothId);
        } else {// has connected
            Log.d(TAG, "has connected...");

            if (!bluetoothId.equals(tempMac)) {
                Log.d(TAG, "connect another device...");

                try {
                    comm.disconnect();
                } catch (CommException e) {
                    e.printStackTrace();
                }

                tempMac = "";
                comm =  glComm.createBt(bluetoothId);
            }
        }

        try {
            comm.connect();
            tempMac = bluetoothId;// save
            return true;
        } catch (CommException e) {
            return false;
        }
    }

    public void disconnect() {

        Log.d(TAG, "disconnect");
        Log.d(TAG, "try close device...");

        if (comm != null) {
            try {
                comm.cancelRecv();
                comm.disconnect();
            } catch (CommException e) {
                e.printStackTrace();
            }
        }

        comm = null;
        tempMac = "";
    }


    public boolean isConnected() {
        if (comm == null) {
            return false;
        }

        comm.cancelRecv();

        return (comm.getConnectStatus() == IComm.EConnectStatus.CONNECTED ? true : false);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0: // set
                {
                    String result = message.getData().getString("result");
                    tvResult.setText(result);
                    break;
                }
                case 1: // update
                {
                    String result = message.getData().getString("result");
                    String s = tvResult.getText().toString();
                    tvResult.setText(s + "\n" + result);
                    break;
                }
            }
        }
    };

    private void printText(){
        try{
            escPos.reset();
            escPos.print(TestExtPrinterActivity.textList);
            escPos.feedPaper(100);
            escPos.cutPaper(0);
            updateResult("print text success");
        } catch (com.pax.gl.extprinter.exception.CommException e) {
            updateResult("print text error, " + e.getMessage());
            e.printStackTrace();
        } catch (PrintException e) {
            updateResult("print text error, " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printBarcode(){
        try {
            escPos.reset();
            escPos.print(TestExtPrinterActivity.barcodeList);
            escPos.feedPaper(255);
            escPos.cutPaper(0);
            updateResult("print barcode success");
        } catch (com.pax.gl.extprinter.exception.CommException e) {
            e.printStackTrace();
            updateResult("print barcode error, " + e.getMessage());
        } catch (PrintException e) {
            e.printStackTrace();
            updateResult("print barcode error, " + e.getMessage());
        }
    }

    public void printBitmap(){
        Bitmap bitmap = GenBitemap.genBitmap(TestBtPrinterActivity.this);
        try {
            escPos.reset();
            escPos.print(new BitmapLine(bitmap));
            escPos.print(new BitmapLine(bitmap, EAlign.LEFT));
            escPos.feedPaper(255);
            escPos.cutPaper(1);
            updateResult("print image success");
        } catch (com.pax.gl.extprinter.exception.CommException e) {
            updateResult("print image error, " + e.getMessage());
            e.printStackTrace();
        } catch (PrintException e) {
            updateResult("print image error, " + e.getMessage());
            e.printStackTrace();
        }
    }
}
