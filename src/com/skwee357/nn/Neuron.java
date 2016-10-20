package com.skwee357.nn;

import com.skwee357.nn.activationfunction.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    private ActivationFunction activationFunction;
    private double signal = 0;
    private double activatedSignal = 0;
    private List<Link> links = new ArrayList<Link>();

    public Neuron(ActivationFunction activationFunction) {
        this.setActivationFunction(activationFunction);
    }

    public void addSignal(double amount) {
        this.signal += amount;
    }

    public void activate() {
        this.activatedSignal = this.activationFunction.activate(this.signal);
        this.signal = 0;
        for (Link link : this.links) link.transmit();
    }

    public double getActivatedSignal() {
        return this.activatedSignal;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    public void addLink(Neuron receiver, Double weight) {
        this.links.add(new Link(this, receiver, weight));
    }

    public List<Link> getLinks() {
        return this.links;
    }

}
