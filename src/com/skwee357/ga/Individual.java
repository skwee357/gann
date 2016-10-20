package com.skwee357.ga;

public class Individual<G> {

    private Chromosome<G> chromosome;
    private Double fitness;

    public Individual(Chromosome<G> chromosome, Double fitness) {
        this.chromosome = chromosome;
        this.fitness = fitness;
    }

    public Chromosome<G> getChromosome() {
        return chromosome;
    }

    public Double getFitness() {
        return fitness;
    }

}
