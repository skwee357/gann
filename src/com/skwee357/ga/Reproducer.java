package com.skwee357.ga;

import java.util.List;

public interface Reproducer<G> {

    List<Chromosome<G>> crossover(Chromosome<G> parent1, Chromosome<G> parent2);

}
