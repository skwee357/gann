package com.skwee357.nn;

import com.skwee357.nn.activationfunction.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class Layer {

    private List<Neuron> neurons = new ArrayList<Neuron>();

    public Layer(int neuronCount, ActivationFunction activationFunction) {
        for (int i = 0; i < neuronCount; ++i) this.neurons.add(new Neuron(activationFunction));
    }

    public void activate() {
        for (Neuron n : this.neurons) n.activate();
    }

    public List<Neuron> getNeurons() {
        return this.neurons;
    }

    public Neuron getNeuronByIndex(int index) {
        if (index < 0) throw new IndexOutOfBoundsException("Neuron index is out of bound");
        if (index >= this.neurons.size()) throw new IndexOutOfBoundsException("Neuron index is out of bound");
        return this.neurons.get(index);
    }

}
