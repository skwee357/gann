package com.skwee357.ga;

import java.util.*;

public class Population<G> implements Iterable<Individual<G>> {

    public static class Environment<G> {
        private FitnessFunction<G> fitnessFunction;
        private Selector<G> selector;
        private Reproducer<G> reproducer;
        private Mutator<G> mutator;

        private float crossoverRate = .7f;
        private float mutationRate = .001f;

        private int populationCap = 0;

        public Environment(FitnessFunction<G> fitnessFunction, Selector<G> selector, Reproducer<G> reproducer, Mutator<G> mutator) {
            this.fitnessFunction = fitnessFunction;
            this.selector = selector;
            this.reproducer = reproducer;
            this.mutator = mutator;
        }

        public void setPopulationCap(int cap) {
            this.populationCap = cap;
        }

        public void setCrossoverRate(float rate) {
            this.crossoverRate = rate;
        }

        public void setMutationRate(float rate) {
            this.mutationRate = rate;
        }

    }

    interface Listener<G> {
        public void onEvolutionStart(Population<G> population);

        public void onEvolutionEnd(Population<G> population);
    }

    private Random random = new Random();
    private Environment<G> environment;
    private Comparator<Individual<G>> comparator = new Comparator<Individual<G>>() {
        @Override
        public int compare(Individual<G> o1, Individual<G> o2) {
            return o1.getFitness().compareTo(o2.getFitness());
        }
    };
    private List<Individual<G>> population = new ArrayList<Individual<G>>();
    private int generation = 0;
    private List<Listener<G>> listeners = new ArrayList<Listener<G>>();

    public Population(Environment<G> environment) {
        this.environment = environment;
        this.addListener(this.environment.selector);
    }

    public void addListener(Listener<G> listener) {
        this.listeners.add(listener);
    }

    public void evolve() {
        Collections.sort(this.population, this.comparator);

        int currentPopulationSize = this.population.size();

        List<Chromosome<G>> newPopulation = new ArrayList<Chromosome<G>>();

        for (Listener<G> listener : this.listeners) listener.onEvolutionStart(this);

        for (int i = 0; i < currentPopulationSize; ++i) {
            Individual<G> i1 = this.environment.selector.select();
            Individual<G> i2 = this.environment.selector.select();

            if (this.random.nextFloat() <= this.environment.crossoverRate) {
                List<Chromosome<G>> children = this.environment.reproducer.crossover(i1.getChromosome(), i2.getChromosome());
                for (Chromosome<G> child : children) {
                    if (this.random.nextFloat() <= this.environment.mutationRate) this.environment.mutator.mutate(child);
                    newPopulation.add(child);
                }
            }
        }

        if (this.environment.populationCap < newPopulation.size())
            newPopulation = newPopulation.subList(0, this.environment.populationCap);

        this.population = new ArrayList<Individual<G>>();
        for (Chromosome<G> chromosome : newPopulation) this.addChromosome(chromosome);

        this.generation++;

        for (Listener<G> listener : this.listeners) listener.onEvolutionEnd(this);
    }

    public void addChromosome(Chromosome<G> chromosome) {
        Double fitness = this.environment.fitnessFunction.evaluate(chromosome);
        this.addIndividual(new Individual<G>(chromosome, fitness));
    }

    public Individual<G> getFittestIndividual() {
        int index = this.population.size();
        if (index == 0) return null;
        return this.population.get(index - 1);
    }

    public Individual<G> getUnfitIndividual() {
        int index = this.population.size();
        if (index == 0) return null;
        return this.population.get(0);
    }

    public int size() {
        return this.population.size();
    }

    public int getGeneration() {
        return this.generation;
    }

    @Override
    public Iterator<Individual<G>> iterator() {
        return this.population.iterator();
    }

    private void addIndividual(Individual<G> individual) {
        this.population.add(individual);
    }

}
