package com.skwee357.nn.activationfunction;

public class Identity implements ActivationFunction {

    @Override
    public double activate(double value) {
        return value;
    }

}
