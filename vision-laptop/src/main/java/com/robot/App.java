package com.robot;

import com.robot.camera.CameraService;
import com.robot.vision.ColorDetector;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        CameraService cameraService = new CameraService(0);

        if (!cameraService.isOpened()) {
            System.out.println("Error: No se pudo acceder a la webcam.");
            return;
        }

        System.out.println("Cámara iniciada correctamente.");

        ColorDetector detector = new ColorDetector();
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

        CanvasFrame windowOriginal = new CanvasFrame("Webcam Original", CanvasFrame.getDefaultGamma() / 2.2);
        CanvasFrame windowProcesada = new CanvasFrame("Webcam Escala de Grises", CanvasFrame.getDefaultGamma() / 2.2);

        windowOriginal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowProcesada.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Mat frame = new Mat();

        while (windowOriginal.isVisible() && cameraService.readFrame(frame)) {
            if (frame.empty()) continue;

            Mat grayFrame = detector.convertToGrayscale(frame);

            windowOriginal.showImage(converter.convert(frame));
            windowProcesada.showImage(converter.convert(grayFrame));
        }

        cameraService.release();
        windowOriginal.dispose();
        windowProcesada.dispose();
        System.out.println("Programa finalizado.");
    }
}