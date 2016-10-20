package com.skwee357.ga;

public interface FitnessFunction<G> {

    Double evaluate(Chromosome<G> chromosome);

}
