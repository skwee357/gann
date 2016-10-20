package com.skwee357.ga;

import java.util.List;

/**
 * @deprecated
 * @param <A>
 */
public interface Genome<A> extends Cloneable {

    public int size();

    public List<A> getGenes();

    public A getGene(int index);

    public void setGene(int index, A gene);

}
