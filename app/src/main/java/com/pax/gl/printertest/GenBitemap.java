package com.pax.gl.printertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.pax.gl.page.IPage;
import com.pax.gl.page.IPage.ILine.IUnit;
import com.pax.gl.page.IPage.EAlign;
import com.pax.gl.page.PaxGLPage;

public class GenBitemap {

    public  static Bitmap genBitmap(Context context) {
        {

            PaxGLPage iPaxGLPage;
            final int FONT_BIG = 28;
            final int FONT_NORMAL = 20;
            final int FONT_BIGEST = 40;

            iPaxGLPage = PaxGLPage.getInstance(context);
            IPage page = iPaxGLPage.createPage();
            //page.setTypeFace("/cache/data/public/neptune/Fangsong.ttf");
            //         page.setTypefaceObj(Typeface.createFromAsset(context.getAssets(), "Fangsong.ttf"));
            IUnit unit = page.createUnit();
            unit.setAlign(EAlign.CENTER);
            unit.setText("GLiPaxGlPage");
            page.addLine().addUnit().addUnit(unit).addUnit(page.createUnit().setText("Test").setAlign(EAlign.RIGHT));
            page.addLine().addUnit("商户存根", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_BOLD);
            page.addLine().addUnit("商户存根", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_UNDERLINE);
            page.addLine().addUnit("商户存根", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_BOLD | IUnit.TEXT_STYLE_UNDERLINE);
            page.addLine().addUnit("商户存根", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);
            page.addLine().addUnit("商户存根", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_ITALIC);
            page.addLine()
                    .addUnit("商户存根", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_BOLD | IUnit.TEXT_STYLE_UNDERLINE | IUnit.TEXT_STYLE_ITALIC, 1);
            page.addLine().addUnit("TEXT_STYLE_ITALIC", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_ITALIC);
            page.addLine()
                    .addUnit("TEXT_STYLE_ITALIC", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_BOLD | IUnit.TEXT_STYLE_UNDERLINE | IUnit.TEXT_STYLE_ITALIC, 1);
            page.addLine().addUnit("商户存根", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL, 1);
            page.addLine().addUnit("-----------------------------------------", FONT_NORMAL);
            page.addLine().addUnit("商户名称: " + "百富计算机技术", FONT_NORMAL);
            page.addLine().addUnit("商户编号: " + "111111111111111", FONT_NORMAL);

            page.addLine().addUnit("终端编号:", 40,EAlign.LEFT, IUnit.TEXT_STYLE_ITALIC, 1.0f).addUnit("操作员号:", 10, EAlign.RIGHT);
            page.addLine().addUnit("22222222", FONT_NORMAL).addUnit("01", FONT_NORMAL, EAlign.RIGHT);

            page.addLine().addUnit("卡号：", FONT_NORMAL);
            page.addLine().addUnit("5454545454545454", FONT_BIG);

            page.addLine().addUnit("交易类型: " + "消费", FONT_BIG);

            page.addLine().addUnit("流水号:", FONT_NORMAL).addUnit("批次号:", FONT_NORMAL, EAlign.RIGHT);
            page.addLine().addUnit("123456", FONT_NORMAL).addUnit("000001", FONT_NORMAL, EAlign.RIGHT);

            page.addLine().addUnit("授权码:", FONT_NORMAL, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL, 1)
                    .addUnit("参考号:", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL, 1);
            page.addLine().addUnit("987654", FONT_BIGEST, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL, 1)
                    .addUnit("012345678912", FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);

            page.addLine().addUnit("日期/时间:" + "2016/06/13 12:12:12", FONT_NORMAL);
            page.addLine().addUnit("金额:", FONT_BIG);
            page.addLine().addUnit("RMB 1.00", FONT_BIG, EAlign.RIGHT, IUnit.TEXT_STYLE_BOLD);

            page.addLine().addUnit("备注:", FONT_NORMAL);
//            page.addLine().addUnit("----------------持卡人签名---------------", FONT_NORMAL);
//            page.addLine().addUnit(getImageFromAssetsFile("pt.bmp"));
//            page.addLine().addUnit("-----------------------------------------", FONT_NORMAL);
            page.addLine()
                    .addUnit("本人确认已上交易, 同意将其计入本卡账户\n\n\n\n\n", FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_UNDERLINE);

            page.addLine().addUnit("交易类型", FONT_NORMAL, EAlign.LEFT).addUnit("笔数", FONT_NORMAL, EAlign.LEFT)
                    .addUnit("金额", FONT_NORMAL, EAlign.RIGHT);
            page.addLine().addUnit("-----------------------------------------", FONT_NORMAL);
            page.addLine().addUnit("消费", FONT_NORMAL, EAlign.LEFT).addUnit("20", FONT_NORMAL, EAlign.LEFT)
                    .addUnit("12345678901234.00", FONT_NORMAL, EAlign.RIGHT);
            page.addLine().addUnit("退货", FONT_NORMAL, EAlign.LEFT).addUnit("40", FONT_NORMAL, EAlign.LEFT)
                    .addUnit("123.00", FONT_NORMAL, EAlign.RIGHT);
            page.addLine().addUnit("激活", FONT_NORMAL, EAlign.LEFT).addUnit("80", FONT_NORMAL, EAlign.LEFT).addUnit();
            page.addLine().addUnit("预授权完成请求撤销", FONT_NORMAL, EAlign.RIGHT).addUnit("120", FONT_NORMAL, EAlign.LEFT)
                    .addUnit("80.00", FONT_NORMAL, EAlign.RIGHT);
            page.addLine().addUnit("预授权完成请求", FONT_NORMAL, EAlign.CENTER).addUnit("120", FONT_NORMAL, EAlign.LEFT)
                    .addUnit("80.00", FONT_NORMAL, EAlign.RIGHT);
            page.addLine().addUnit("--------------------------------------\n\n", FONT_NORMAL);

            page.addLine().addUnit("TEST 1", FONT_NORMAL, EAlign.LEFT).addUnit("TEST 2", FONT_NORMAL, EAlign.CENTER)
                    .addUnit("TEST 3", FONT_NORMAL, EAlign.CENTER).addUnit("TEST 4", FONT_NORMAL, EAlign.RIGHT);

            page.addLine().addUnit("TEST 5", FONT_NORMAL, EAlign.LEFT).addUnit("TEST 6", FONT_NORMAL, EAlign.CENTER)
                    .addUnit("TEST 7", FONT_NORMAL, EAlign.CENTER).addUnit("TEST 8", FONT_NORMAL, EAlign.RIGHT)
                    .addUnit("TEST 9", FONT_NORMAL, EAlign.RIGHT);
            page.addLine().addUnit("\n\n\n\n", FONT_NORMAL);

            page.addLine().addUnit("金额:", 25).addUnit("500.00jg", 25,EAlign.RIGHT);
            page.addLine().addUnit("金额:", 25).addUnit("500.00jg", 25, 1.0f, 1.0f,EAlign.RIGHT,1.0f);
            page.addLine().addUnit("金额:", 25).addUnit("500.00jg", 25, 1.0f, 0.5f,EAlign.RIGHT,1.0f);
            page.addLine().addUnit("金额:", 25).addUnit("500.00jg", 25, 0.5f, 1.0f,EAlign.RIGHT,1.0f);
            page.addLine().addUnit("金额:", 25).addUnit("500.00jg", 25, 0.5f, 0.5f,EAlign.RIGHT,1.0f);
            page.addLine().addUnit("金额:", 25).addUnit("500.00jg", 25, 1.0f, 1.5f,EAlign.RIGHT,1.0f);
            page.addLine().addUnit("金额:", 25).addUnit("500.00jg", 25, 1.5f, 1.5f,EAlign.RIGHT,1.0f);
            page.addLine().addUnit("金额:", 25).addUnit("500.00jg", 25, 1.0f, 2.0f,EAlign.RIGHT,1.0f);
            page.addLine().addUnit("金额:", 25,0.2f).addUnit("500.00jg", 25, 2.0f, 1.0f,EAlign.RIGHT,0.8f);
            page.addLine().addUnit("金额:", 25,0.2f).addUnit("500.00jg", 25, 2.0f, 2.0f,EAlign.RIGHT,0.8f);
            page.addLine().addUnit("金额:", 25).addUnit("500.00jg", 25, 1.0f, 3.0f,EAlign.RIGHT,1.0f);
            page.addLine().addUnit("金额:", 25,0.2f).addUnit("500.00jg", 25,3.0f,2.0f,EAlign.RIGHT,0.8f);

            page.addLine().addUnit("流水号:123456", 25);

            page.addLine().addUnit("5jمرحباً", 25*5);
            page.addLine().addUnit("5jمرحباً", 25*4);
            page.addLine().addUnit("5jمرحباً", 25*3);
            page.addLine().addUnit("5jمرحباً", 25*2);
            page.addLine().addUnit("5jمرحباً", 25*1);

            page.addLine().addUnit("50.01", 25*5);
            page.addLine().addUnit("50.01", 25*4);
            page.addLine().addUnit("50.01", 25*3);
            page.addLine().addUnit("50.01", 25*2);
            page.addLine().addUnit("50.01", 25*1);

            int width = 384;
            return iPaxGLPage.pageToBitmap(page, width);

        }
    }
}
