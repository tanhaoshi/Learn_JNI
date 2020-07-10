package com.ym.idcard.reg;

import android.os.Handler;

/**
 * create by ths on 2020/7/9
 */
public class NativeOcr {

    private static final String LIB = "IDCardengine";

    private static final String LIB_R10 = "IDCardengine_r10";

    private static Handler mHandler;

    private static int mProgress = 0;

    private static boolean mCancel = false;

    public NativeOcr() {}

    public NativeOcr(Handler paramHandler) {
        mHandler = paramHandler;
    }

    public native int freeImage(long paramLong, long[] paramArrayOflong);

    public native int closeOCR(long[] paramArrayOflong);

    public native long GetCardNum(byte[] paramArrayOfbyte, int paramInt);

    public native long GetTrnImageThread();

    public native int GetCardNumRectThread(int[] paramArrayOfint);

    public native int GetCharInfoThread(int[] paramArrayOfint, int paramInt);

    public native int GetCardBinInfo(long paramLong, byte[] paramArrayOfbyte, int paramInt);

    public native int LicenseStr(byte[] paramArrayOfbyte);

    public native int RecYuvImg(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, int[] paramArrayOfint, byte[] paramArrayOfbyte2);

    public native int GetResult(byte[] paramArrayOfbyte, int paramInt);

//    private static int GetState(int paramInt) {
//        if (a.a()) {
//            switch (paramInt) {
//                case 1:
//                    mHandler.sendEmptyMessage(b.a(b.b));
//                    break;
//            }
//        } else {
//            mHandler.sendEmptyMessage(b.a(b.a));
//        }
//        return 1234;
//    }

    public void finalize() {}

    public static int getProgress() {
        return mProgress;
    }

    public static int Progress(int paramInt1, int paramInt2) {
        if (paramInt2 != 0) {
            mProgress += paramInt1;
        } else {
            mProgress = paramInt1;
        }
        return mProgress;
    }

    public native int startBCR(long[] paramArrayOflong, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt, byte[] paramArrayOfbyte3);

    public native int startOCR(long[] paramArrayOflong, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2);

    public native int startBCRBeiJing(long[] paramArrayOflong, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt, byte[] paramArrayOfbyte3);

    public native int closeBCR(long[] paramArrayOflong);

    public native int doImageBCR(long paramLong1, long paramLong2, long[] paramArrayOflong);

    public native int doLineOCR(long paramLong1, long paramLong2, long[] paramArrayOflong, byte[] paramArrayOfbyte, int paramInt);

    public native int imageChecking(long paramLong1, long paramLong2, int paramInt);

    public native int checkingCopyID(long paramLong);

    public native void freeBField(long paramLong1, long paramLong2, int paramInt);

    public native void setProgressFunc(long paramLong, boolean paramBoolean);

    public native long loadImageMem(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3);

    public native int getFieldId(long paramLong);

    public native int getFieldText(long paramLong, byte[] paramArrayOfbyte, int paramInt);

    public native long getNextField(long paramLong);

    public native int getFieldRect(long paramLong, int[] paramArrayOfint);

    public native int codeConvert(long paramLong, byte[] paramArrayOfbyte, int paramInt);

    public native int setoption(long paramLong, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);

    public native int getLastErr(long paramLong, byte[] paramArrayOfbyte, int paramInt);

    public native long DupImage(long paramLong, int[] paramArrayOfint);

    public native long getheadImgRect(long paramLong, int[] paramArrayOfint);

    public native long SaveImage(long paramLong, byte[] paramArrayOfbyte);

    public native int GetCardType(long paramLong1, long paramLong2);

    public native long GetHeadInfo(int[] paramArrayOfint);

    public native int getheadImg(long paramLong1, long paramLong2, byte[] paramArrayOfbyte);

    public native int ClearAll(int paramInt);

    public native int startBCR(long[] paramArrayOflong, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt);

    public native long getYData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);

    public native byte CheckCardEdgeLine(long paramLong1, long paramLong2, int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public native byte[] BImageToImagebyte(long paramLong);

    public native int BImageToImagebyte(long paramLong, byte[] paramArrayOfbyte);

    public native long YuvToRgb(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3);

    public native long FreeRgb(long paramLong);

    public native long SetSwitch(long paramLong, int paramInt1, int paramInt2);

    static {
        try {
            System.loadLibrary("IDCardengine");
        } catch (Exception exception) {}
    }
}
