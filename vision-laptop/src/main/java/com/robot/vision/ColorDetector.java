package com.robot.vision;

import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

public class ColorDetector {

    public Mat trackColor(Mat inputFrame) {
        Mat hsvFrame = new Mat();
        Mat mask = new Mat();
        Mat outputFrame = inputFrame.clone();

        // 1. Convertir de BGR a HSV (más fácil para filtrar colores)
        cvtColor(inputFrame, hsvFrame, COLOR_BGR2HSV);

        // 2. Definir rango de color a detectar (Ejemplo: Rojo/Naranja)
        Scalar lowerBound = new Scalar(0, 120, 70, 0);   // Límite inferior HSV
        Scalar upperBound = new Scalar(10, 255, 255, 0); // Límite superior HSV

        // 3. Crear máscara binaria (blanco lo que coincide con el color, negro lo demás)
        inRange(hsvFrame, new Mat(lowerBound), new Mat(upperBound), mask);

        // 4. Encontrar contornos/formas del objeto
        MatVector contours = new MatVector();
        findContours(mask, contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

        double maxArea = 0;
        Rect bestBox = null;

        // Buscar el contorno más grande (el objeto principal)
        for (long i = 0; i < contours.size(); i++) {
            Mat contour = contours.get(i);
            double area = contourArea(contour);

            if (area > 500 && area > maxArea) { // Filtrar ruido pequeño
                maxArea = area;
                bestBox = boundingRect(contour);
            }
        }

        // 5. Dibujar un rectángulo verde y un punto central sobre el objeto detectado
        if (bestBox != null) {
            rectangle(outputFrame, bestBox, new Scalar(0, 255, 0, 0), 2, LINE_8, 0);

            int centerX = bestBox.x() + bestBox.width() / 2;
            int centerY = bestBox.y() + bestBox.height() / 2;

            // Imprimir coordenadas X, Y en consola o sobre el video
            circle(outputFrame, new Point(centerX, centerY), 5, new Scalar(0, 0, 255, 0), -1, LINE_8, 0);
            putText(outputFrame, "X: " + centerX + " Y: " + centerY, 
                    new Point(bestBox.x(), bestBox.y() - 10), 
                    FONT_HERSHEY_SIMPLEX, 0.6, new Scalar(0, 255, 0, 0), 2, LINE_8, false);
        }

        return outputFrame;
    }
}