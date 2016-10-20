package com.skwee357.nn;

import com.skwee357.nn.activationfunction.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    private List<Layer> layers = new ArrayList<Layer>();
    private List<Double> output = new ArrayList<Double>();

    public NeuralNetwork(int inputNeuronCount, ActivationFunction inputActivationFunction, int outputNeuronCount, ActivationFunction outputActivationFunction) {
        this.layers.add(new Layer(inputNeuronCount, inputActivationFunction));
        this.layers.add(new Layer(outputNeuronCount, outputActivationFunction));
    }

    public Layer getInputLayer() {
        return this.layers.get(0);
    }

    public Layer getOutputLayer() {
        return this.layers.get(this.layers.size() - 1);
    }

    public Layer getHiddenLayer(int index) {
        index = index + 1; //first layer is always input layer hence +1
        if (index >= this.layers.size() - 1)
            throw new IndexOutOfBoundsException("Invalid hidden layer index: " + index); //last layer is always output layer hence -1
        return this.layers.get(index);
    }

    public Layer addHiddenLayer(int neuronCount, ActivationFunction activationFunction) {
        Layer layer = new Layer(neuronCount, activationFunction);
        this.layers.add(this.layers.size() - 1, layer);
        return layer;
    }

    public List<Double> getAllWeights() {
        List<Double> weights = new ArrayList<Double>();

        for (Layer layer : this.layers) {
            for (Neuron neuron : layer.getNeurons()) {
                for (Link link : neuron.getLinks()) {
                    weights.add(link.getWeight());
                }
            }
        }

        return weights;
    }

    public void setAllWeights(List<Double> weights) {
        if (weights.size() == 0) return;

        for (Layer layer : this.layers) {
            for (Neuron neuron : layer.getNeurons()) {
                for (Link link : neuron.getLinks()) {
                    link.setWeight(weights.get(0));
                    weights.remove(0);

                    if (weights.size() == 0) return; //@TODO more elegant solution?
                }
            }
        }
    }

    public void activate(List<Double> input) {
        this.output.clear();

        Layer inputLayer = this.layers.get(0);
        List<Neuron> inputLayerNeurons = inputLayer.getNeurons();

        if (input.size() != inputLayerNeurons.size())
            throw new IllegalArgumentException("Number of inputs(" + input.size() + ") mismatches the number of input neurons(" + inputLayerNeurons.size() + ")");

        for (int i = 0; i < input.size(); ++i) {
            inputLayerNeurons.get(i).addSignal(input.get(i));
        }

        for (Layer layer : this.layers) {
            layer.activate();
        }

        Layer outputLayer = this.layers.get(this.layers.size() - 1);
        List<Neuron> outputLayerNeurons = outputLayer.getNeurons();

        for (Neuron n : outputLayerNeurons) {
            this.output.add(n.getActivatedSignal());
        }
    }

    public List<Double> getOutput() {
        return this.output;
    }

}
