package com.skwee357.ga;

public abstract class Selector<G> implements Population.Listener<G> {

    public abstract Individual<G> select();

}
