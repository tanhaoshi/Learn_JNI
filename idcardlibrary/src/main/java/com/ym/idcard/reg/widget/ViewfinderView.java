package com.ym.idcard.reg.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * create by ths on 2020/8/29
 */
public class ViewfinderView extends View {
    private int f;
    private int g;
    private Paint h;
    private Context i;
    private int j;
    private int k;
    private float l;
    private float m;
    private float n;
    private float o;
    private int p = 0;
    private float q = 0.0F;
    private float r = 0.0F;
    private float s = 0.0F;
    private int t = 12;
    private int u = 60;
    private int v;
    private int w;
    boolean a = false;
    boolean b = false;
    boolean c = false;
    boolean d = false;
    boolean e = false;

    public ViewfinderView(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.i = var1;
    }

    public ViewfinderView(Context var1) {
        super(var1);
        this.i = var1;
    }

    public ViewfinderView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.i = var1;
    }

    @SuppressLint("WrongConstant")
    public void a(int var1, int var2) {
        this.v = var1;
        this.w = var2;
        WindowManager var4 = (WindowManager)this.i.getSystemService("window");
        Display var5 = var4.getDefaultDisplay();
        this.f = var5.getWidth();
        this.g = var5.getHeight();
        Log.d("tag", "-1-------->>" + this.f);
        this.s = 42;
        this.q = (float)((double)(this.f - var1) / 2.0D);
        this.r = (float)((double)(this.g - var2) / 2.0D);
        this.j = this.f / 2;
        this.k = this.g / 2;
        float var6 = (float)this.g - this.s * 2.0F;
        float var7 = var6 * 1.58F;
        float var8 = 10.0F;
        Log.d("ocr", var7 + "<<--k---高度----g--1---->>" + var6);

        while(var7 > (float)var1) {
            --var8;
            var7 *= var8 / 10.0F;
            var6 *= var8 / 10.0F;
        }

        Log.d("ocr", var7 + "<<--k---高度----g--2---->>" + var6);
        this.l = (float)((double)this.j - (double)var7 / 2.0D);
        this.m = (float)((double)this.j + (double)var7 / 2.0D);
        this.n = (float)((double)this.k - (double)var6 / 2.0D);
        this.o = (float)((double)this.k + (double)var6 / 2.0D);
        int var9 = var5.getWidth();
        int var10 = var5.getHeight();
        int var11 = this.v;
        int var12 = this.w;
        double var17 = 0.0D;
        double var19 = 0.0D;
        double var21 = 0.0D;
        double var13;
        double var15;
        if (var11 * var10 < var9 * var12) {
            var15 = (double)var10;
            var13 = (double)var11 / (double)var12 * var15;
        } else {
            var13 = (double)var9;
            var15 = var13 * ((double)var12 / (double)var11);
        }

        if (var13 / var15 >= 1.0D) {
            var17 = 4.0D * var15 / 3.0D;
        } else {
            var17 = var13;
            var19 = 3.0D * var13 / 4.0D;
        }

        var21 = var17 / 480.0D * 420.0D;
        this.h = new Paint();
        this.t = (int)var21 / 28;
        this.t = 4;
        this.h.setStrokeWidth((float)this.t);
        this.u = (int)var21 / 6;
    }

    public Rect getFinder() {
        return new Rect((int)(this.l - this.q), (int)(this.n - this.r), (int)(this.m + this.q), (int)(this.o + this.r));
    }

    public void setLineRect(int var1) {
        this.p = var1;
        this.invalidate();
    }

    public void draw(Canvas var1) {
        super.draw(var1);
        if (this.h != null) {
            this.h.setColor(-16711936);
            var1.drawLine(this.l - (float)(this.t / 2), this.n, this.l + (float)this.u, this.n, this.h);
            var1.drawLine(this.l, this.n - (float)(this.t / 2), this.l, this.n + (float)this.u, this.h);
            var1.drawLine(this.m, this.n - (float)(this.t / 2), this.m, this.n + (float)this.u, this.h);
            var1.drawLine(this.m + (float)(this.t / 2), this.n, this.m - (float)this.u, this.n, this.h);
            var1.drawLine(this.l, this.o + (float)(this.t / 2), this.l, this.o - (float)this.u, this.h);
            var1.drawLine(this.l - (float)(this.t / 2), this.o, this.l + (float)this.u, this.o, this.h);
            var1.drawLine(this.m + (float)(this.t / 2), this.o, this.m - (float)this.u, this.o, this.h);
            var1.drawLine(this.m, this.o + (float)(this.t / 2), this.m, this.o - (float)this.u, this.h);
            switch(this.p) {
                case 0:
                default:
                    break;
                case 1:
                    var1.drawLine(this.l, this.n, this.l, this.o, this.h);
                    break;
                case 2:
                    var1.drawLine(this.m, this.n, this.m, this.o, this.h);
                    break;
                case 3:
                    var1.drawLine(this.l, this.n, this.l, this.o, this.h);
                    var1.drawLine(this.m, this.n, this.m, this.o, this.h);
                    break;
                case 4:
                    var1.drawLine(this.l, this.n, this.m, this.n, this.h);
                    break;
                case 5:
                    var1.drawLine(this.l, this.n, this.l, this.o, this.h);
                    var1.drawLine(this.l, this.n, this.m, this.n, this.h);
                    break;
                case 6:
                    var1.drawLine(this.m, this.n, this.m, this.o, this.h);
                    var1.drawLine(this.l, this.n, this.m, this.n, this.h);
                    break;
                case 7:
                    var1.drawLine(this.l, this.n, this.l, this.o, this.h);
                    var1.drawLine(this.m, this.n, this.m, this.o, this.h);
                    var1.drawLine(this.l, this.n, this.m, this.n, this.h);
                    break;
                case 8:
                    var1.drawLine(this.l, this.o, this.m, this.o, this.h);
                    break;
                case 9:
                    var1.drawLine(this.l, this.n, this.l, this.o, this.h);
                    var1.drawLine(this.l, this.o, this.m, this.o, this.h);
                    break;
                case 10:
                    var1.drawLine(this.m, this.n, this.m, this.o, this.h);
                    var1.drawLine(this.l, this.o, this.m, this.o, this.h);
                    break;
                case 11:
                    var1.drawLine(this.l, this.n, this.l, this.o, this.h);
                    var1.drawLine(this.m, this.n, this.m, this.o, this.h);
                    var1.drawLine(this.l, this.o, this.m, this.o, this.h);
                    break;
                case 12:
                    var1.drawLine(this.l, this.n, this.m, this.n, this.h);
                    var1.drawLine(this.l, this.o, this.m, this.o, this.h);
                    break;
                case 13:
                    var1.drawLine(this.l, this.n, this.m, this.n, this.h);
                    var1.drawLine(this.l, this.o, this.m, this.o, this.h);
                    var1.drawLine(this.l, this.n, this.l, this.o, this.h);
                    break;
                case 14:
                    var1.drawLine(this.l, this.n, this.m, this.n, this.h);
                    var1.drawLine(this.l, this.o, this.m, this.o, this.h);
                    var1.drawLine(this.m, this.n, this.m, this.o, this.h);
                    break;
                case 15:
                    var1.drawLine(this.l, this.n, this.l, this.o, this.h);
                    var1.drawLine(this.m, this.n, this.m, this.o, this.h);
                    var1.drawLine(this.l, this.n, this.m, this.n, this.h);
                    var1.drawLine(this.l, this.o, this.m, this.o, this.h);
            }

            this.h.setColor(-16777216);
            this.h.setAlpha(100);
            var1.drawRect(0.0F, 0.0F, (float)this.f, this.n - (float)(this.t / 2), this.h);
            var1.drawRect(0.0F, this.n - (float)(this.t / 2), this.l - (float)(this.t / 2), this.o + (float)(this.t / 2), this.h);
            var1.drawRect(0.0F, this.o + (float)(this.t / 2), (float)this.f, (float)this.g, this.h);
            var1.drawRect(this.m + (float)(this.t / 2), this.n - (float)(this.t / 2), (float)this.f, this.o + (float)(this.t / 2), this.h);
        }
    }
}
