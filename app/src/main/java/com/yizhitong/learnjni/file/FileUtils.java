package com.yizhitong.learnjni.file;

import java.io.File;

/**
 * create by ths on 2020/9/1
 */
public class FileUtils {

    public static boolean exist(String filepath)
    {
        if (filepath == null)
            return false;
        return new File(filepath).exists();
    }

}
