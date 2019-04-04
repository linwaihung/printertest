package com.pax.gl.printertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.pax.dal.IPrinter;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pax.dal.IDAL;

public class TestInternalPrinterActivity extends AppCompatActivity {

    private NeptuneLiteUser nlu = null;
    private IDAL dal = null;

    private IPrinter a920printer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_internal_printer);

        Bitmap alipay_pic = BitmapFactory.decodeResource(getResources(), R.drawable.alipay_logo);


        nlu = NeptuneLiteUser.getInstance();
        try {
            dal = nlu.getDal(TestInternalPrinterActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        a920printer = dal.getPrinter();
        try {
            a920printer.init();
            a920printer.printStr("EFT Solutions test 1\n",null);
            a920printer.printStr("EFT Solutions test 2\n",null);
            a920printer.printStr("EFT Solutions test 3\n",null);
            a920printer.printStr("EFT Solutions test 4\n",null);
            a920printer.printBitmap(alipay_pic);
            a920printer.start();
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
}
