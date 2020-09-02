package com.yizhitong.learnjni.facedetect;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * create by ths on 2020/9/1
 */
public class FaceDetectBody {

    static{
        try{
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public FaceDetectBody(){

    }

    public void disposFaceDetect(String imagePath,String newFileName,String frontalfacePath){
        Mat src = Imgcodecs.imread(imagePath);
        Imgcodecs.imwrite(newFileName, getFace(src,frontalfacePath));
    }


    public static Mat getFace(Mat src,String frontalfacePath) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }
//        "./resource/haarcascade_frontalface_alt2.xml"
        CascadeClassifier faceDetector = new CascadeClassifier(frontalfacePath);
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

}
