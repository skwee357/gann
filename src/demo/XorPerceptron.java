package demo;

import com.skwee357.nn.Layer;
import com.skwee357.nn.NeuralNetwork;
import com.skwee357.nn.activationfunction.Step;

import java.util.ArrayList;
import java.util.List;

public class XorPerceptron {

    public static void main(String[] args) {
        NeuralNetwork network = new NeuralNetwork(2, null, 1, null);
        Layer perceptron = network.addHiddenLayer(3, null);

        perceptron.getNeuronByIndex(0).setActivationFunction(new Step(1));
        perceptron.getNeuronByIndex(1).setActivationFunction(new Step(2));
        perceptron.getNeuronByIndex(2).setActivationFunction(new Step(1));

        Layer input = network.getInputLayer();
        input.getNeuronByIndex(0).addLink(perceptron.getNeuronByIndex(0), 1.0);
        input.getNeuronByIndex(0).addLink(perceptron.getNeuronByIndex(1), 1.0);

        input.getNeuronByIndex(1).addLink(perceptron.getNeuronByIndex(1), 1.0);
        input.getNeuronByIndex(1).addLink(perceptron.getNeuronByIndex(2), 1.0);

        Layer output = network.getOutputLayer();
        perceptron.getNeuronByIndex(0).addLink(output.getNeuronByIndex(0), 1.0);
        perceptron.getNeuronByIndex(1).addLink(output.getNeuronByIndex(0), -2.0);
        perceptron.getNeuronByIndex(2).addLink(output.getNeuronByIndex(0), 1.0);

        List<Double> i = new ArrayList<Double>(2);

        for(int x = 0; x <= 1; ++x) {
            for(int y = 0; y <= 1; ++y) {
                i.clear();
                i.add((double)x);
                i.add((double)y);
                network.activate(i);
                System.out.println("Input: " + i + "\tOutput: " + network.getOutput());
            }
        }
    }

}
