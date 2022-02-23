package frc.robot.util;

public class Balls {
    private String label;
    private Box box;
    private double confidence;

    public String getLabel() {
        return label;
    }

    public Box getBox() {
        return box;
    }

    public double getConfidence() {
        return confidence;
    }

    public double getSurfaceArea() {
        double width = getBox().getXmax() - getBox().getXmin();
        double length = getBox().getYmax() - getBox().getYmin();
        return width * length; 
    }

}

