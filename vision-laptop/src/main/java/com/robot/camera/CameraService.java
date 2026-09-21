package com.robot.camera;

import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.opencv_core.Mat;

public class CameraService {
    private VideoCapture camera;

    public CameraService(int cameraIndex) {
        this.camera = new VideoCapture(cameraIndex);
    }

    public boolean isOpened() {
        return camera.isOpened();
    }

    public boolean readFrame(Mat frame) {
        return camera.read(frame);
    }

    public void release() {
        if (camera.isOpened()) {
            camera.release();
        }
    }
}