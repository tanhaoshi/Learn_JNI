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

        }
    }

    public FaceDetectBody(){

    }

    public void disposFaceDetect(String imagePath,String newFileName){
        Mat src = Imgcodecs.imread(imagePath);
        Imgcodecs.imwrite(newFileName, getFace(src));
    }

    public static Mat getUpperBody(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_mcs_upperbody.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getLefteye(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_mcs_lefteye.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getRighteye(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_mcs_righteye.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getLeftear(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_mcs_leftear.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getRightear(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_mcs_rightear.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getMouth(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_mcs_mouth.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getNose(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_mcs_nose.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getSmile(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_smile.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getLowerBody(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_lowerbody.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getFullBody(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_fullbody.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getFace(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_frontalface_alt2.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public static Mat getProfileFace(Mat src) {
        Mat result = src.clone();
        if (src.cols() > 1000 || src.rows() > 1000) {
            Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
        }

        CascadeClassifier faceDetector = new CascadeClassifier("./resource/haarcascade_profileface.xml");
        MatOfRect objDetections = new MatOfRect();
        faceDetector.detectMultiScale(result, objDetections);
        for (Rect rect : objDetections.toArray()) {
            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
        return result;
    }

}
