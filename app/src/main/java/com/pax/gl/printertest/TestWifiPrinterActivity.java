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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pax.gl.commhelper.IComm;
import com.pax.gl.commhelper.impl.PaxGLComm;
import com.pax.gl.extprinter.entity.BitmapLine;
import com.pax.gl.extprinter.entity.EAlign;
import com.pax.gl.extprinter.exception.CommException;
import com.pax.gl.extprinter.exception.PrintException;
import com.pax.gl.extprinter.impl.GLExtPrinter;
import com.pax.gl.extprinter.inf.ICommListener;
import com.pax.gl.extprinter.inf.IExtPrinter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestWifiPrinterActivity extends Activity{
    private String TAG = "TestWifiPrinterActivity";
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(" + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
                    "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    private int currentTestCaseIndex = 0;
    private String currentIp = "";
    private int currentPort = -1;

    private IComm comm = null;
    private PaxGLComm glComm = null;
    private IExtPrinter escPos = null;

    private Spinner spTestCases;
    private Button wifiTest;
    private TextView wifi_tvResult;
    private EditText ipText;
    private EditText portTeXt;

    private ProgressDialog progressDialog;

    //网络通道
    private ICommListener wifiListener = new ICommListener() {
        @Override
        public void onSend(byte[] data) throws CommException {
            if(comm != null){
                try {
                    comm.send(data);
                } catch (com.pax.gl.commhelper.exception.CommException e) {
                    e.printStackTrace();
                    throw new CommException(CommException.ERR_SEND);
                }
            }else {
                Log.e(TAG, "comm is null, send error");
            }
        }

        @Override
        public byte[] onRecv(int expLen) throws CommException {
            byte[] data  = new byte[0];
            if(comm != null) {
                try {
                    data = comm.recv(expLen);
                } catch (com.pax.gl.commhelper.exception.CommException e) {
                    e.printStackTrace();
                    throw new CommException(CommException.ERR_RECV);
                }
            }else {
                Log.e(TAG, "comm is null, recv error");
            }
            return data;
        }

        @Override
        public void onReset() {
            if(comm != null) {
                comm.reset();
            }else {
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
        progressDialog = ProgressDialog.show(TestWifiPrinterActivity.this, "", "Testing...");
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
                String ip = ipText.getText().toString();
                String port = portTeXt.getText().toString();

                if(isIPv4Address(ip) && isNumeric(port)){
                    updateResult("ip:" + ip + ", port:" + port);
                }else{
                    updateResult("ip or port is not right");
                    progressDialog.dismiss();
                    break;
                }

                if(connect(ip, Integer.valueOf(port))){
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
                        updateResult("wifi not connected");
                    }
                }else{
                    updateResult("wifi not connected");
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
                        updateResult("wifi not connected");
                    }
                }else{
                    updateResult("wifi not connected");
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
                        updateResult("wifi not connected");
                    }
                }else{
                    updateResult("wifi not connected");
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
                        updateResult("wifi not connected");
                    }
                }else{
                    updateResult("wifi not connected");
                }

                progressDialog.dismiss();
                break;
            }

        }
    }

    public boolean connect(final String host, final int port) {
        Log.d(TAG, ">>>wifi connect");

        if (currentIp.equals("") || comm == null || !isConnected()) {// first connect
            Log.d(TAG, "first connect...");
            comm = glComm.createTcpClient(host, port);
        } else {// has connected
            Log.d(TAG, "has connected...");

            if ((!host.equals(currentIp)) || (currentPort != port)) {// new ip or port
                Log.d(TAG, "connect another ip or port...");

                try {
                    comm.disconnect();
                } catch (com.pax.gl.commhelper.exception.CommException e) {
                    e.printStackTrace();
                }

                currentIp = "";
                currentPort = -1;

                comm = glComm.createTcpClient(host, port);
            }
        }

        try {
            if (!isConnected()) {
                comm.connect();
                currentIp = host;// save
                currentPort = port;//save
            }

            return true;

        } catch (com.pax.gl.commhelper.exception.CommException e) {
            currentIp = "";
            currentPort = -1;
            comm = null;
            return false;
        }
    }

    public boolean isConnected() {
        Log.d(TAG, ">>>isConnected");
        if (comm == null) {
            return false;
        }

        comm.cancelRecv();//GLComm - getConnectStatus is syn

        return (comm.getConnectStatus() == IComm.EConnectStatus.CONNECTED ? true : false);
    }

    public void disconnect() {
        Log.d(TAG, "disconnect");
        Log.d(TAG, "try close device...");

        if (comm != null) {
            try {
                comm.cancelRecv();
                comm.disconnect();
            } catch (com.pax.gl.commhelper.exception.CommException e) {
                e.printStackTrace();
            }
        }

        comm = null;
        currentIp = "";
        currentPort = -1;
    }

    private void printText(){
        try{
            escPos.reset();
            escPos.print(TestExtPrinterActivity.textList);
            escPos.feedPaper(200);
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

    private void printBarcode(){
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

    private void printBitmap(){

        try {
            escPos.reset();
            escPos.print(new BitmapLine(TestExtPrinterActivity.bitmap));
            escPos.print(new BitmapLine(TestExtPrinterActivity.bitmap, EAlign.LEFT));
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

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_main);

        glComm = PaxGLComm.getInstance(TestWifiPrinterActivity.this);
        escPos = GLExtPrinter.createEscPosPrinter(wifiListener, 576);

        ipText = (EditText) findViewById(R.id.ip);
        portTeXt = (EditText) findViewById(R.id.port);

        ipText.setHint("please enter ip");
        portTeXt.setHint("please enter port");

        wifi_tvResult = (TextView) findViewById(R.id.wifi_tvResult);

        spTestCases = (Spinner) findViewById(R.id.wifi_spTestCases);
        List<HashMap<String, String>> testCasesList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < testCases.length; i++) {
            HashMap<String, String> testCase = new HashMap<String, String>();
            testCase.put("testcase", testCases[i]);

            testCasesList.add(testCase);
        }

        spTestCases.setAdapter(new SimpleAdapter(TestWifiPrinterActivity.this, testCasesList, R.layout.testcase_item, new String[] { "testcase" }, new int[] { R.id.tvTestCase }));
        spTestCases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                currentTestCaseIndex = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        wifiTest = (Button) findViewById(R.id.wifiTest);
        wifiTest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doTest();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(comm != null){
            try {
                comm.disconnect();
            } catch (com.pax.gl.commhelper.exception.CommException e) {
                e.printStackTrace();
            }
        }
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


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0: // set
                {
                    String result = message.getData().getString("result");
                    wifi_tvResult.setText(result);
                    break;
                }
                case 1: // update
                {
                    String result = message.getData().getString("result");
                    String s = wifi_tvResult.getText().toString();
                    wifi_tvResult.setText(s + "\n" + result);
                    break;
                }
            }
        }
    };

}
