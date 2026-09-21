package com.robot.vision;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

public class ColorDetector {

    public Mat convertToGrayscale(Mat inputFrame) {
        Mat grayFrame = new Mat();
        opencv_imgproc.cvtColor(inputFrame, grayFrame, opencv_imgproc.COLOR_BGR2GRAY);
        return grayFrame;
    }
}