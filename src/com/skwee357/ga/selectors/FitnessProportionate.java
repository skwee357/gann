package com.skwee357.ga.selectors;

import com.skwee357.ga.*;

import java.util.Random;

public class FitnessProportionate<G> extends Selector<G> {

    private Random random = new Random();
    private Population<G> population;
    private Double range = 0.0;

    @Override
    public Individual<G> select() {
        double candidate = this.random.nextDouble() * this.range;

        for (Individual<G> individual : this.population) {
            if (candidate <= individual.getFitness()) return individual;
        }

        return this.population.getFittestIndividual();
    }

    @Override
    public void onEvolutionStart(Population<G> population) {
        this.population = population;

        Individual<G> best = this.population.getFittestIndividual();
        Individual<G> worst = this.population.getUnfitIndividual();
        Double bestFitness = (best != null) ? best.getFitness() : 0.0;
        Double worstFitness = (worst != null) ? worst.getFitness() : 0.0;

        this.range = (bestFitness - worstFitness) + worstFitness;
    }

    @Override
    public void onEvolutionEnd(Population<G> population) {

    }
}
