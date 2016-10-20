package com.skwee357.ga;

import java.util.List;

public class Chromosome<G> {

    private List<G> genome;

    public Chromosome(List<G> genome) {
        this.genome = genome;
    }

    public List<G> getGenome() {
        return this.genome;
    }

}
