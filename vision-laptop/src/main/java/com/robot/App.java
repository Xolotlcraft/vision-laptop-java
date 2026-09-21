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

        ColorDetector detector = new ColorDetector();
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

        CanvasFrame windowOriginal = new CanvasFrame("Webcam - Rastreador de Objetos", CanvasFrame.getDefaultGamma() / 2.2);
        windowOriginal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Mat frame = new Mat();

        while (windowOriginal.isVisible() && cameraService.readFrame(frame)) {
            if (frame.empty()) continue;

            // Procesar el cuadro para rastrear el objeto
            Mat trackedFrame = detector.trackColor(frame);

            // Mostrar frame con el cuadro verde dibujado
            windowOriginal.showImage(converter.convert(trackedFrame));
        }

        cameraService.release();
        converter.close();
        windowOriginal.dispose();
        System.out.println("Programa finalizado.");
    }
}