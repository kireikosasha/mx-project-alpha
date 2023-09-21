package mx.project.api;

import lombok.var;

public class math {

    public static double getD(double xA, double xB, double yA, double yB) {
        var Xposer = Math.pow((xA - xB), 2);
        var Yposer = Math.pow((xA - xB), 2);
        var XYPoser = Xposer + Yposer;
        return Math.sqrt(XYPoser);
    }
    public static double scaleVal(double value, double scale) {
        double scale2 = Math.pow(10, scale);
        return Math.ceil(value * scale2) / scale2;
    }
}
