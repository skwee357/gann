package com.skwee357.nn.activationfunction;

public class Step implements ActivationFunction {

    private double threshold;

    public Step(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public double activate(double value) {
        return (value >= this.threshold) ? 1.0 : 0.0;
    }
}
