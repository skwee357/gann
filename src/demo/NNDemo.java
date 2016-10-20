package demo;

import com.skwee357.nn.Layer;
import com.skwee357.nn.NeuralNetwork;
import com.skwee357.nn.Neuron;
import com.skwee357.nn.activationfunction.Linear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NNDemo {

    public static void main(String[] args) {
        Random random = new Random();

        NeuralNetwork nn = new NeuralNetwork(2, new Linear(0), 1, new Linear(0));
//        nn.addHiddenLayer(3, new SigmoidFunction());

        //set links between input layer and output layer
        {
            Layer inputLayer = nn.getInputLayer();
            Layer outputLayer = nn.getOutputLayer();

            for(Neuron inputNeuron: inputLayer.getNeurons()) {
                for(Neuron outputNeuron: outputLayer.getNeurons()) {
                    inputNeuron.addLink(outputNeuron, random.nextDouble());
                }
            }
        }

        List<Double> input = new ArrayList<Double>(2);
        input.add(random.nextDouble());
        input.add(random.nextDouble());

        System.out.println("Input: " + input);

        nn.activate(input);

        System.out.println("Output: " + nn.getOutput());

        System.out.println("Weights: " + nn.getAllWeights());

    }

}
