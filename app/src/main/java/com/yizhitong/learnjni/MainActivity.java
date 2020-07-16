package com.yizhitong.learnjni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


/**
 * 全文cmake list 学习途径
 *
 * 对cmake list 文件的一些用法解释，及常用的方式
 *
 * https://www.jianshu.com/p/39ca6c51bc20
 *
 * https://blog.csdn.net/yongwn/article/details/97618948
 *
 * 对cmake list 文件的一些扩展功能方式的使用
 * https://blog.csdn.net/b2259909/article/details/58591898
 */


//一定要记住 包名里面加下滑线的时候，一定要在下滑线名字前面加1 不然找不到JNI方法
public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // first 我应该做什么,首先第一步我应该去做什么，我既然我去使用了open cv 基于 tess-two的框架,我就必须去做些什么,首先，我先把tess-two的集成完毕把，
        //目前已经把open cv 集成好了,目前要做的就是要去做，集成tess-two。
        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
