package com.skwee357.nn.activationfunction;

public class Linear implements ActivationFunction {

    private double bias;

    public Linear(double bias) {
        this.bias = bias;
    }

    @Override
    public double activate(double value) {
        return value + this.bias;
    }
}
