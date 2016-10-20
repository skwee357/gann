package com.skwee357.nn.activationfunction;

public class Sigmoid implements ActivationFunction {

    @Override
    public double activate(double value) {
        return 1.0 / (1.0 + Math.exp(-value));
    }

}
