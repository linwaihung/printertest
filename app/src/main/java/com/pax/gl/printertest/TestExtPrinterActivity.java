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
 * 2019-01-18 	         michael.z          	Create
 * ===========================================================================================
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.pax.gl.extprinter.entity.BarcodeLine;
import com.pax.gl.extprinter.entity.EAlign;
import com.pax.gl.extprinter.entity.EAsciiFontSize;
import com.pax.gl.extprinter.entity.EBarCodeType;
import com.pax.gl.extprinter.entity.EChineseFontSize;
import com.pax.gl.extprinter.entity.TextLine;
import com.pax.gl.extprinter.impl.GLExtPrinterDebug;
import com.pax.gl.extprinter.inf.ILine;

import java.util.ArrayList;
import java.util.List;

import static com.pax.gl.extprinter.inf.ITextLine.ITextUnit.TEXT_STYLE_BOLD;
import static com.pax.gl.extprinter.inf.ITextLine.ITextUnit.TEXT_STYLE_NORMAL;
import static com.pax.gl.extprinter.inf.ITextLine.ITextUnit.TEXT_STYLE_UNDERLINE;
import static com.pax.gl.extprinter.inf.ITextLine.ITextUnit.TEXT_STYLE_WHITEBLACKREVERSE;

public class TestExtPrinterActivity extends AppCompatActivity {
    public static Bitmap bitmap = null;
    public static List<ILine> textList = null;
    public static List<ILine> barcodeList = null;

    Button btPrinterButton = null;
    Button wifiPrinterButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        GLExtPrinterDebug.setDebugLevel(GLExtPrinterDebug.EDebugLevel.DEBUG_LEVEL_ALL);

        this.setTitle("ExtPrinterTest_" + this.getVerName(this));

        initData();

        btPrinterButton = (Button) findViewById(R.id.btPrinterTest);
        btPrinterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestExtPrinterActivity.this, TestBtPrinterActivity.class));
            }
        });

        wifiPrinterButton = (Button) findViewById(R.id.wifiPrinterTest);
        wifiPrinterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestExtPrinterActivity.this, TestWifiPrinterActivity.class));
            }
        });

        wifiPrinterButton = (Button) findViewById(R.id.internalPrinterTest);
        wifiPrinterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestExtPrinterActivity.this, TestInternalPrinterActivity.class));
            }
        });


    }

    private void initData(){
        textList = getTextList();
        barcodeList = getBarcodeList();
        bitmap = GenBitemap.genBitmap(TestExtPrinterActivity.this);
    }

    private List<ILine> getTextList(){
        List<ILine> list = new ArrayList<>();
        int lineSpacing = 20;

        for(int i = 0; i < 1; i++) {
            list.add(new TextLine(lineSpacing).addUnit("发票", null, EChineseFontSize.NORMAL, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("商户存根", null , EChineseFontSize.NORMAL, EAlign.LEFT, null));
            list.add(new TextLine(lineSpacing).addUnit("---------------------------------------------------------------------", EAsciiFontSize.NORMAL, null,  EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("商户存根", null, EChineseFontSize.NORMAL, EAlign.LEFT, TEXT_STYLE_WHITEBLACKREVERSE, null));
            list.add(new TextLine(lineSpacing).addUnit("---------------------------------------------------------------------", EAsciiFontSize.NORMAL, null, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("商户存根", null, EChineseFontSize.NORMAL, EAlign.LEFT, TEXT_STYLE_BOLD, null));
            list.add(new TextLine(lineSpacing).addUnit("---------------------------------------------------------------------", EAsciiFontSize.NORMAL, null, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("商户存根", null, null, EAlign.LEFT, TEXT_STYLE_UNDERLINE, null));
            list.add(new TextLine(lineSpacing).addUnit("---------------------------------------------------------------------", EAsciiFontSize.NORMAL, null, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("商户存根", null, EChineseFontSize.NORMAL, EAlign.LEFT, TEXT_STYLE_WHITEBLACKREVERSE | TEXT_STYLE_BOLD, null));
            list.add(new TextLine(lineSpacing).addUnit("---------------------------------------------------------------------", EAsciiFontSize.NORMAL, null, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("商户存根", EAsciiFontSize.NORMAL,null, EAlign.LEFT, TEXT_STYLE_WHITEBLACKREVERSE | TEXT_STYLE_UNDERLINE, null));
            list.add(new TextLine(lineSpacing).addUnit("---------------------------------------------------------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("商户存根", EAsciiFontSize.NORMAL,null, EAlign.LEFT, TEXT_STYLE_BOLD | TEXT_STYLE_UNDERLINE, null));
            list.add(new TextLine(lineSpacing).addUnit("---------------------------------------------------------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("商户存根", EAsciiFontSize.NORMAL,null, EAlign.LEFT, TEXT_STYLE_WHITEBLACKREVERSE | TEXT_STYLE_BOLD | TEXT_STYLE_UNDERLINE, null));
            list.add(new TextLine(lineSpacing).addUnit("---------------------------------------------------------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));

            list.add(new TextLine(lineSpacing).addUnit("商户名称:", EAlign.LEFT, 5).addUnit("百富计算机技术有限公司", EAlign.RIGHT, 5));
            list.add(new TextLine(lineSpacing).addUnit("商户编号: " + "111111111111111", EAsciiFontSize.NORMAL, EChineseFontSize.NORMAL, null));
            list.add(new TextLine(lineSpacing).addUnit("终端编号:", null ,EChineseFontSize.NORMAL, EAlign.LEFT, TEXT_STYLE_UNDERLINE, 2, 2, 2).addUnit("操作员号:12345678909876543210abcdefghijklmnopqrstuvwxyz", EAsciiFontSize.NORMAL, EChineseFontSize.NORMAL, EAlign.CENTER, TEXT_STYLE_NORMAL, 1, 1, 8));
            list.add(new TextLine(lineSpacing).addUnit("22222222", EAsciiFontSize.NORMAL,null, 5).addUnit("1235678901", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 5));
            list.add(new TextLine(lineSpacing).addUnit("卡号：", null, EChineseFontSize.NORMAL, EAlign.LEFT, 4).addUnit("5454545454545454", null, null, EAlign.RIGHT, 6));
            list.add(new TextLine(lineSpacing).addUnit("交易类型：消费，交易类型：消费，交易类型：消费，交易类型：消费", null));
            list.add(new TextLine(lineSpacing).addUnit("流水号:", null, EChineseFontSize.NORMAL, 2).addUnit("批次号:", null, EChineseFontSize.NORMAL, EAlign.RIGHT, 8));
            list.add(new TextLine(lineSpacing).addUnit("123456", null, EChineseFontSize.NORMAL, 4).addUnit("000001", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 6));
            list.add(new TextLine(lineSpacing).addUnit("授权码:1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ", null, EChineseFontSize.NORMAL, EAlign.LEFT, TEXT_STYLE_NORMAL, 1, 1, 5, 6).addUnit("参考号:12345", null, EChineseFontSize.NORMAL, EAlign.RIGHT, TEXT_STYLE_NORMAL, 1, 1, 0, 4));
            list.add(new TextLine(lineSpacing).addUnit("123456", EAsciiFontSize.NORMAL, EChineseFontSize.NORMAL, EAlign.LEFT, TEXT_STYLE_NORMAL, 1, 1, 10, 4).addUnit("012345678912", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, TEXT_STYLE_NORMAL, 6));
            list.add(new TextLine(lineSpacing).addUnit("日期/时间:" + "2016/06/13 12:12:12", EAsciiFontSize.NORMAL,null, null));
            list.add(new TextLine(lineSpacing).addUnit("金额:", 2).addUnit("RMB 1.00", EAlign.RIGHT, TEXT_STYLE_BOLD, 4).addUnit("RMB 1.00", EAlign.RIGHT, TEXT_STYLE_BOLD, 4));
            list.add(new TextLine(lineSpacing).addUnit("备注:", null, EChineseFontSize.NORMAL, null));
            list.add(new TextLine(lineSpacing).addUnit("----------------持卡人签名---------------", EAsciiFontSize.NORMAL, EChineseFontSize.NORMAL, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("-----------------------------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("本人确认已上交易, 同意将其计入本卡账户", null, EChineseFontSize.NORMAL, EAlign.CENTER, TEXT_STYLE_UNDERLINE, null));

            list.add(new TextLine());
            list.add(new TextLine());
            list.add(new TextLine());

            list.add(new TextLine(lineSpacing).addUnit("交易类型", null, EChineseFontSize.NORMAL, EAlign.LEFT, 3).addUnit("笔数", null, EChineseFontSize.NORMAL, EAlign.LEFT, 2).addUnit("金额", null, EChineseFontSize.NORMAL, EAlign.RIGHT, 5));
            list.add(new TextLine(lineSpacing).addUnit("-----------------------------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("消费: ", null, EChineseFontSize.NORMAL, EAlign.LEFT, 3).addUnit("20", EAsciiFontSize.NORMAL,null, EAlign.LEFT, 2).addUnit("123456789012.00", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 5));
            list.add(new TextLine(lineSpacing).addUnit("退货: ", null, EChineseFontSize.NORMAL, EAlign.LEFT, 3).addUnit("40", EAsciiFontSize.NORMAL,null, EAlign.LEFT, 2).addUnit("123.00", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 5));
            list.add(new TextLine(lineSpacing).addUnit("激活: ", null, EChineseFontSize.NORMAL, EAlign.LEFT, 3).addUnit("80", EAsciiFontSize.NORMAL,null, EAlign.LEFT, 2).addUnit("34.00", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 5));
            list.add(new TextLine(lineSpacing).addUnit("预授权完成请求撤销", null, EChineseFontSize.NORMAL, EAlign.LEFT, 6).addUnit("120", EAsciiFontSize.NORMAL,null, EAlign.LEFT, 2).addUnit("80.00", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 2));
            list.add(new TextLine(lineSpacing).addUnit("预授权完成请求", null, EChineseFontSize.NORMAL, EAlign.LEFT, 6).addUnit("120", EAsciiFontSize.NORMAL,null, EAlign.LEFT, 2).addUnit("80.00", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 2));
            list.add(new TextLine(lineSpacing).addUnit("-----------------------------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
            list.add(new TextLine(lineSpacing).addUnit("TEST 1", EAsciiFontSize.NORMAL,null, EAlign.LEFT, 2).addUnit("TEST 2", EAsciiFontSize.NORMAL,null, EAlign.CENTER, 2).addUnit("TEST 3", EAsciiFontSize.NORMAL,null, EAlign.CENTER, 2).addUnit("TEST 4", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 4));
            list.add(new TextLine(lineSpacing).addUnit("TEST 5", EAlign.LEFT, 2).addUnit("TEST 6", EAsciiFontSize.NORMAL,null, EAlign.CENTER, 2).addUnit("TEST 7", EAsciiFontSize.NORMAL,null, EAlign.CENTER, 2).addUnit("TEST 8", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 2).addUnit("TEST 9", EAsciiFontSize.NORMAL,null, EAlign.RIGHT, 2));
            list.add(new TextLine(lineSpacing).addUnit("----------------END----------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, 1));
        }

        return list;
    }

    private List<ILine> getBarcodeList(){
        List<ILine> list = new ArrayList<>();

        list.add(new TextLine().addUnit("----------------UPCA---------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));//第12位为校验值
        list.add(new BarcodeLine("01234567890", EBarCodeType.UPC_A, EAlign.CENTER));
        //list.add(new BarcodeLine("012345678905", EBarCodeType.UPC_A, EAlign.RIGHT, 4, 50));

        list.add(new TextLine().addUnit("----------------JAN13---------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));//第13位为校验值
        list.add(new BarcodeLine("012345678900", EBarCodeType.JAN13, EAlign.RIGHT, 4, 50));

        list.add(new TextLine().addUnit("----------------JAN8---------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));//第8位为校验位
        list.add(new BarcodeLine("0123456", EBarCodeType.JAN8, EAlign.RIGHT, 4, 50));

        list.add(new TextLine().addUnit("----------------ITF---------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
        list.add(new BarcodeLine("012345", EBarCodeType.ITF, EAlign.RIGHT, 4, 50));
        list.add(new BarcodeLine("00", EBarCodeType.ITF, EAlign.CENTER, 4, 100));//可以生成条码,但是无法扫码
        list.add(new BarcodeLine("0011223344", EBarCodeType.ITF, EAlign.LEFT, 4, 100));

        list.add(new TextLine().addUnit("---------------CODE93---------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
        list.add(new BarcodeLine("012345", EBarCodeType.CODE93, EAlign.RIGHT, 4, 50));
        list.add(new BarcodeLine("abc", EBarCodeType.CODE93, EAlign.RIGHT, 4, 50));
        list.add(new BarcodeLine("ABCDE", EBarCodeType.CODE93, EAlign.RIGHT, 4, 50));

        list.add(new TextLine().addUnit("---------------CODE128---------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
        list.add(new BarcodeLine("{BS{A+", EBarCodeType.CODE128, EAlign.RIGHT, 1, 50));
        list.add(new BarcodeLine("{BNo.12345abcde", EBarCodeType.CODE128, EAlign.RIGHT, 1, 50));

        list.add(new TextLine().addUnit("----------------CODE39---------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
        list.add(new BarcodeLine("1", EBarCodeType.CODE39, EAlign.RIGHT, 1, 50));//有问题
        list.add(new BarcodeLine("12", EBarCodeType.CODE39, EAlign.RIGHT, 1, 50));//有问题
        list.add(new BarcodeLine("012345678900", EBarCodeType.CODE39, EAlign.RIGHT, 1, 50));//有问题
        list.add(new BarcodeLine("ABCDEFGHIJKL", EBarCodeType.CODE39, EAlign.RIGHT, 1, 50));

        list.add(new TextLine().addUnit("----------------UPCE---------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
        list.add(new BarcodeLine("042100005264", EBarCodeType.UPC_E, EAlign.LEFT, 2, null));

        list.add(new TextLine().addUnit("---------------CODABAR---------------------", EAsciiFontSize.NORMAL,null, EAlign.CENTER, null));
        list.add(new BarcodeLine("A012345D", EBarCodeType.CODABAR, EAlign.RIGHT, 1, null));//
        list.add(new BarcodeLine("B40156B", EBarCodeType.CODABAR, EAlign.LEFT, 2, null));

        return list;
    }


    private String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

}
