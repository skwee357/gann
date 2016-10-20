package com.skwee357.nn;

public class Link {

    private Neuron sender;
    private Neuron receiver;
    private Double weight;

    public Link(Neuron sender, Neuron receiver, Double weight) {
        this.sender = sender;
        this.receiver = receiver;
        this.weight = weight;
    }

    public void transmit() {
        this.receiver.addSignal(this.sender.getActivatedSignal() * this.weight);
    }

    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
