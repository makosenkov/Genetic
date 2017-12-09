package KnapsackSolution;

import KnapsackOther.Fill;
import KnapsackOther.Item;

import java.util.*;

public class GeneticSolution {
    Random random = new Random();
    private Item[] items;
    private int maxLoad;
    private int numberOfGenerations;
    private int sizeOfFirstGeneration = 30;
    private int numberOfSurvivors = 10;
    private int numberOfMutants = 15;

    private GeneticSolution(List<Item> items, int numberOfGenerations, int maxLoad) {
        Item[] items1 = new Item[items.size()];
        items1 = items.toArray(items1);
        this.items = items1;
        this.numberOfGenerations = numberOfGenerations;
        this.maxLoad = maxLoad;
    }

    public static Fill solve(int load, List<Item> items) {
        GeneticSolution solve = new GeneticSolution(items,25, load);
        return solve.fillKnapsackGenetic(load, items);
    }

    public Fill fillKnapsackGenetic(int load, List<Item> items) {
        maxLoad = load;
        return findSolution().convertToFill();
    }

    public Individual findSolution() {
        Generation generation = new Generation();
        for (int i = 0; i < numberOfGenerations; i++)
            generation.evolute();
        return generation.theChosenOne;
    }

    private class Individual {
        boolean[] DNA;
        int fitness;
        int load;

        Individual() {
            boolean[] randomDNA = new boolean[items.length];
            for (int i = 0; i < randomDNA.length; i++) {
                randomDNA[i] = getRandomBoolean();
            }
            DNA = randomDNA;
        }

        Individual(boolean[] DNA, int fitness, int load) {
            this.DNA = DNA;
            this.fitness = fitness;
            this.load = load;
        }

        Fill convertToFill() {
            Set<Item> takenItems = new HashSet<>();
            for (int i = 0; i < items.length; i++)
                if (DNA[i])
                    takenItems.add(items[i]);

            return new Fill(fitness, takenItems);
        }

        Individual cross(Individual otherDNA) {
            boolean crossedDNA[] = new boolean[items.length];
            int fitness = 0;
            int load = 0;
            for (int i = 0; i < crossedDNA.length; i++) {
                if (this.DNA[i] == otherDNA.DNA[i]) {
                    crossedDNA[i] = this.DNA[i];
                    load += items[i].getWeight();
                    fitness += items[i].getCost();
                } else
                    crossedDNA[i] = getRandomBoolean();

            }
            return new Individual(crossedDNA, fitness, load);
        }

        Individual mutate() {
            boolean[] mutant = new boolean[items.length];
            int fitness = 0;
            int load = 0;
            for (int i = 0; i < mutant.length; i++) {
                if (getRandomBoolean()) {
                    if (DNA[i])
                        mutant[i] = false;
                    else {
                        mutant[i] = true;
                        load += items[i].getWeight();
                        fitness += items[i].getCost();
                    }
                }
            }
            return new Individual(mutant, fitness, load);
        }

        private boolean getRandomBoolean() {
            return random.nextInt(500) < 250;
        }
    }

    private class Generation {

        List<Individual> individuals;
        Individual theChosenOne;

        Generation() {
            individuals = new ArrayList<>();
            for (int i = 0; i < sizeOfFirstGeneration; i++)
                individuals.add(new Individual());

            boolean[] DNA = new boolean[items.length];
            for (int i = 0; i < DNA.length; i++) {
                DNA[i] = false;
            }
            theChosenOne = new Individual(DNA, 0, 0);
        }

        private void evolute() {
            for (int i = 0; i < numberOfMutants; i++)
                individuals.add(new Individual());
            individuals = selection();
            individuals.addAll(mutants());
            individuals = selection();
            individuals = crossing();
            chooseTheOnes();
        }

        private void chooseTheOnes() {
            for (Individual individual : individuals)
                if (individual.fitness > theChosenOne.fitness && individual.load < maxLoad)
                    theChosenOne = individual;
        }

        private List<Individual> selection() {
            List<Individual> survivors = new ArrayList<>(numberOfSurvivors);
            for (int i = 0; i < numberOfSurvivors; i++) {
                Individual theBestOne = new Individual(new boolean[items.length], 0, 0);
                int index = 0;
                for (int j = 0; j < individuals.size(); j++) {
                    if (individuals.get(j).fitness > theBestOne.fitness && individuals.get(j).load <= maxLoad) {
                        theBestOne = new Individual(individuals.get(j).DNA, individuals.get(j).fitness, individuals.get(j).load);
                        index = j;
                    }
                }
                survivors.add(theBestOne);
                individuals.remove(index);
            }
            return survivors;
        }

        private List<Individual> crossing() {
            List<Individual> nextGeneration = new ArrayList<>();
            for (int i = 0; i < individuals.size(); i++)
                for (int j = i + 1; j < individuals.size(); j++)
                    nextGeneration.add(individuals.get(i).cross(individuals.get(j)));
            return nextGeneration;
        }

        private List<Individual> mutants() {
            List<Individual> mutants = new ArrayList<>();
            for (Individual individual : individuals) {
                mutants.add(individual.mutate());
                mutants.add(individual.mutate());
            }
            return mutants;
        }
    }
}
