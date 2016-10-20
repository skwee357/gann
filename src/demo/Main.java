package demo;

import com.skwee357.ga.*;
import com.skwee357.ga.selectors.FitnessProportionate;

import java.util.*;

public class Main {

    public static final Byte MAX_GENE_VALUE = 0x0F;
    public static final Integer MIN_GENES = 3;
    public static final Integer MAX_GENES = 48;
    public static final Integer MASK = 0x0000000F;

    private static HashMap<Integer, Integer> evaluationScores = new HashMap<Integer, Integer>();

    public static void main(String[] args) {
        System.out.println("Hello and welcome to ga calculator");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Please entry an integer number: ");
        Integer target = scanner.nextInt();

        Population.Environment<Byte> environment = new Population.Environment<Byte>(
                new Fitness(target),
                new FitnessProportionate<Byte>(),
                new RandomReproducer(),
                new RandomMutator()
        );

        System.out.print("Please select population amount (> 0): ");
        Integer totalPopulation = scanner.nextInt();
        environment.setPopulationCap(totalPopulation);

        System.out.print("After how many generations, start the Great Flood? (Kill everyone and start a new population. 0 - never): ");
        Integer greatFlood = scanner.nextInt();
        if (greatFlood < 0) greatFlood = 0;

        System.out.print("Please enter the % for crossover to happen [in range 0 - 1, good value is 0.7]: ");
        environment.setCrossoverRate(scanner.nextFloat());

        System.out.print("Please enter the % for mutation to happen [in range 0 - 1, good value is 0.001]: ");
        environment.setMutationRate(scanner.nextFloat());

        Population<Byte> population = new Population<Byte>(environment);
        randomInitialPopulation(population, totalPopulation);

        int maxIterations = 100000, currentIteration = 0;
        while (maxIterations-- > 0) {
            System.out.println();
            System.out.printf("Target Value %d\t\tGeneration %d\t\tTotal Population %d\n", target, population.getGeneration(), population.size());
            Individual<Byte> best = population.getFittestIndividual();

            if (best == null) {
                System.out.println("Everybody died..");
                return;
            }

            System.out.printf("Best in this generation:\n\tfitness: %.3f\n%s\n", best.getFitness(), Main.describeChromosome(best.getChromosome()));

            if (Main.evaluate(best.getChromosome()).equals(target)) {
                System.out.printf("BEST FIT FOUND AFTER %d GENERATIONS\n", population.getGeneration());
                return;
            }

            population.evolve();

            currentIteration++;
            if ((greatFlood != 0) && (currentIteration == greatFlood)) {
                System.out.println("====A great flood has started!====");
                population = new Population<Byte>(environment);
                randomInitialPopulation(population, totalPopulation);
                currentIteration = 0;
            }
        }

        System.out.println("Too many iterations :(");
    }

    private static List<Byte> randomGenome() {
        Random random = new Random();
        List<Byte> genes = new ArrayList<Byte>();
        int total = random.nextInt(MAX_GENES) + MIN_GENES;
        for (int i = 0; i < total; ++i) {
            int gene = random.nextInt(MAX_GENE_VALUE);
            genes.add((byte) gene);
        }
        return genes;
    }

    private static void randomInitialPopulation(Population<Byte> population, int target) {
        while (target-- > 0) {
            population.addChromosome(new Chromosome<Byte>(randomGenome()));
        }
    }

    private static Integer evaluate(Chromosome<Byte> chromosome) {
        Integer hashCode = System.identityHashCode(chromosome);

        if (!evaluationScores.containsKey(hashCode)) {
            Integer accumulator = 0;
            boolean lookForSign = true;

            List<Byte> seq = new ArrayList<Byte>();

            for (Byte b : chromosome.getGenome()) {
                if (lookForSign) {
                    if ((b >= 10) && (b <= 13)) {
                        seq.add(b);
                        lookForSign = !lookForSign;
                    }
                } else {
                    if ((b >= 0) && (b <= 9)) {
                        seq.add(b);
                        lookForSign = !lookForSign;
                    }
                }
            }

            for (int i = 0; i < seq.size() - 1; ++i) {
                if ((seq.get(i) == 13) && (seq.get(i + 1) == 0)) { // division followed by 0
                    seq.set(i, (byte) 10);
                }
            }

            for (int i = 0; i < seq.size() - 1; ++i) {
                switch (seq.get(i)) {
                    case 10:
                        accumulator += seq.get(i + 1);
                        break;
                    case 11:
                        accumulator -= seq.get(i + 1);
                        break;
                    case 12:
                        accumulator *= seq.get(i + 1);
                        break;
                    case 13:
                        accumulator /= seq.get(i + 1);
                        break;
                }
            }

            evaluationScores.put(hashCode, accumulator);
        }

        return evaluationScores.get(hashCode);
    }

    private static class RandomReproducer implements com.skwee357.ga.Reproducer<Byte> {

        private Random random = new Random();

        @Override
        public List<com.skwee357.ga.Chromosome<Byte>> crossover(com.skwee357.ga.Chromosome<Byte> parent1, com.skwee357.ga.Chromosome<Byte> parent2) {
            int cross = this.random.nextInt(Math.min(parent1.getGenome().size(), parent2.getGenome().size()));

            List<Chromosome<Byte>> operations = new ArrayList<Chromosome<Byte>>(2);

            List<Byte> genome = new ArrayList<Byte>(parent2.getGenome().size());
            for (int i = 0; i < cross; ++i) genome.add(i, parent1.getGenome().get(i));
            for (int i = cross; i < parent2.getGenome().size(); ++i) genome.add(i, parent2.getGenome().get(i));
            operations.add(new Chromosome<Byte>(genome));

            genome = new ArrayList<Byte>(parent1.getGenome().size());
            for (int i = 0; i < cross; ++i) genome.add(i, parent2.getGenome().get(i));
            for (int i = cross; i < parent1.getGenome().size(); ++i) genome.add(i, parent1.getGenome().get(i));
            operations.add(new Chromosome<Byte>(genome));

            return operations;
        }
    }

    private static class RandomMutator implements com.skwee357.ga.Mutator<Byte> {

        private Random random = new Random();

        @Override
        public void mutate(Chromosome<Byte> chromosome) {
            int index = this.random.nextInt(chromosome.getGenome().size() - 1);
            int mutated = ~chromosome.getGenome().get(index);
            mutated = mutated & MASK;

            chromosome.getGenome().set(index, (byte) mutated);
        }
    }

    private static String describeChromosome(Chromosome<Byte> chromosome) {
        StringBuilder sb = new StringBuilder();

        boolean lookForSign = true;

        List<Byte> seq = new ArrayList<Byte>();

        sb.append("\tGenome\n");
        sb.append("\t\tRaw:\t\t");

        for (Byte b : chromosome.getGenome()) {
            if ((b >= 10) && (b <= 13)) {
                if (b == 10) sb.append("+");
                else if (b == 11) sb.append("-");
                else if (b == 12) sb.append("*");
                else if (b == 13) sb.append("/");
            } else if ((b >= 0) && (b <= 9)) {
                sb.append(b);
            } else {
                sb.append("na");
            }

            sb.append(" ");
        }

        sb.append("\n\t\tEvaluated:\t");

        for (Byte b : chromosome.getGenome()) {
            if (lookForSign) {
                if ((b >= 10) && (b <= 13)) {
                    seq.add(b);
                    lookForSign = false;
                }
            } else {
                if ((b >= 0) && (b <= 9)) {
                    seq.add(b);
                    lookForSign = true;
                }
            }
        }

        for (int i = 0; i < seq.size() - 1; ++i) {
            if ((seq.get(i) == 13) && (seq.get(i + 1) == 0)) { // division followed by 0
                seq.set(i, (byte) 10);
            }
        }

        for (Byte c : seq) {
            if ((c >= 0) && (c <= 9)) {
                sb.append(c);
            } else if ((c >= 10) && (c <= 13)) {
                if (c == 10) sb.append("+");
                else if (c == 11) sb.append("-");
                else if (c == 12) sb.append("*");
                else if (c == 13) sb.append("/+");
            }
            sb.append(" ");
        }

        sb.append("= ").append(Main.evaluate(chromosome));

        return sb.toString();
    }

//    private static class Chromosome extends com.skwee357.ga.Chromosome<Byte, Genome> {
//
//        public static final Integer MASK = 0x0000000F;
//
//        private Random random = new Random();
//        private Integer evaluation = null;
//
//        public Chromosome() {
//            super(Genome.makeRandom());
//        }
//
//        public Chromosome(Genome genome) {
//            super(genome);
//        }
//
//        public int evaluate() {
//            if (this.evaluation == null) {
//                Integer accumulator = 0;
//                boolean lookForSign = true;
//
//                List<Byte> seq = new ArrayList<Byte>();
//
//                for (Byte b : this.getGenome().getGenes()) {
//                    if (lookForSign) {
//                        if ((b >= 10) && (b <= 13)) {
//                            seq.add(b);
//                            lookForSign = !lookForSign;
//                        }
//                    } else {
//                        if ((b >= 0) && (b <= 9)) {
//                            seq.add(b);
//                            lookForSign = !lookForSign;
//                        }
//                    }
//                }
//
//                for (int i = 0; i < seq.size() - 1; ++i) {
//                    if ((seq.get(i) == 13) && (seq.get(i + 1) == 0)) { // division followed by 0
//                        seq.set(i, (byte) 10);
//                    }
//                }
//
//                for (int i = 0; i < seq.size() - 1; ++i) {
//                    switch (seq.get(i)) {
//                        case 10:
//                            accumulator += seq.get(i + 1);
//                            break;
//                        case 11:
//                            accumulator -= seq.get(i + 1);
//                            break;
//                        case 12:
//                            accumulator *= seq.get(i + 1);
//                            break;
//                        case 13:
//                            accumulator /= seq.get(i + 1);
//                            break;
//                    }
//                }
//
//                this.evaluation = accumulator;
//            }
//
//            return this.evaluation;
//        }
//
//        @Override
//        public String toString() {
//            StringBuilder sb = new StringBuilder();
//
//            boolean lookForSign = true;
//
//            List<Byte> seq = new ArrayList<Byte>();
//
//            sb.append("\tGenome\n");
//            sb.append("\t\tRaw:\t\t");
//
//            for (Byte b : this.getGenome().getGenes()) {
//                if ((b >= 10) && (b <= 13)) {
//                    if (b == 10) sb.append("+");
//                    else if (b == 11) sb.append("-");
//                    else if (b == 12) sb.append("*");
//                    else if (b == 13) sb.append("/");
//                } else if ((b >= 0) && (b <= 9)) {
//                    sb.append(b);
//                } else {
//                    sb.append("na");
//                }
//
//                sb.append(" ");
//            }
//
//            sb.append("\n\t\tEvaluated:\t");
//
//            for (Byte b : this.getGenome().getGenes()) {
//                if (lookForSign) {
//                    if ((b >= 10) && (b <= 13)) {
//                        seq.add(b);
//                        lookForSign = false;
//                    }
//                } else {
//                    if ((b >= 0) && (b <= 9)) {
//                        seq.add(b);
//                        lookForSign = true;
//                    }
//                }
//            }
//
//            for (int i = 0; i < seq.size() - 1; ++i) {
//                if ((seq.get(i) == 13) && (seq.get(i + 1) == 0)) { // division followed by 0
//                    seq.set(i, (byte) 10);
//                }
//            }
//
//            for (Byte c : seq) {
//                if ((c >= 0) && (c <= 9)) {
//                    sb.append(c);
//                } else if ((c >= 10) && (c <= 13)) {
//                    if (c == 10) sb.append("+");
//                    else if (c == 11) sb.append("-");
//                    else if (c == 12) sb.append("*");
//                    else if (c == 13) sb.append("/+");
//                }
//                sb.append(" ");
//            }
//
//            sb.append("= ").append(this.evaluate());
//
//            return sb.toString();
//        }
//
//        public void mutate() {
//            int index = this.random.nextInt(this.getGenome().size() - 1);
//            int mutated = ~this.getGenome().getGene(index);
//            mutated = mutated & MASK;
//
//            this.getGenome().setGene(index, (byte) mutated);
//        }

//    @Override
//    public List<Chromosome> crossover(Chromosome other) {
//        int cross = this.random.nextInt(Math.min(this.genes.size(), other.genes.size()));
//
//        List<Chromosome> operations = new ArrayList<Chromosome>(2);
//
//        List<Byte> genes = new ArrayList<Byte>(other.genes.size());
//        for (int i = 0; i < cross; ++i) genes.add(i, this.genes.get(i));
//        for (int i = cross; i < other.genes.size(); ++i) genes.add(i, other.genes.get(i));
//        operations.add(new Chromosome(genes));
//
//        genes = new ArrayList<Byte>(this.genes.size());
//        for (int i = 0; i < cross; ++i) genes.add(i, other.genes.get(i));
//        for (int i = cross; i < this.genes.size(); ++i) genes.add(i, this.genes.get(i));
//        operations.add(new Chromosome(genes));
//
//        return operations;
//    }
//
//    }

    private static class Fitness implements com.skwee357.ga.FitnessFunction<Byte> {

        private int target;

        public Fitness(int target) {
            this.target = target;
        }

        @Override
        public Double evaluate(Chromosome<Byte> chromosome) {
            Integer v = Main.evaluate(chromosome);
            return 1.0 / Math.abs(this.target - v);
        }

    }

}