package de.htwsaar.esch.Codeopolis.DomainModel.Storage;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * The Silo class represents a storage unit for a specific type of grain.
 */
public class Silo implements Serializable,Comparable<Silo> {
    private Comparator<Harvest> comparator = (harvest1, harvest2) -> Float.compare(harvest1.getDurability(), harvest2.getDurability());
    private PriorityQueue<Harvest> stock;
    private final int capacity;
    private int fillLevel;
    private int stockIndex = -1;

    @Override
    public int compareTo(Silo otherSilo) {

        if (this.fillLevel > otherSilo.fillLevel) {
            return 1;
        } else if (this.fillLevel < otherSilo.fillLevel) {
            return -1;
        } else {
            return 0;
        }

    }

    public class Status {
        private final int capacity;
        private final int fillLevel;

        private Status() {
            this.capacity = Silo.this.capacity;
            this.fillLevel = Silo.this.fillLevel;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getFillLevel() {
            return fillLevel;
        }
    }

    /**
     * Constructs a Silo object with the specified initial capacity.
     *
     * @param capacity The initial capacity of the silo.
     */
    public Silo(int capacity) {
        this.capacity = capacity;
        this.stock = new PriorityQueue<>(comparator);
        this.fillLevel = 0;
    }

    /**
     * Copy constructor for the Silo class.
     * Creates a new Silo object as a deep copy of another Silo object.
     * This constructor is used to ensure that each property of the Silo,
     * including mutable objects, is copied and independent of the original object.
     *
     * @param other The Silo object to copy.
     */
    public Silo(Silo other) {
        this.capacity = other.capacity;
        this.fillLevel = other.fillLevel;
        this.stockIndex = other.stockIndex;
        this.stock = new PriorityQueue<>(comparator);
        for (Harvest harvest : other.stock) {
            this.stock.add(harvest.copy());
        }
    }

    /**
     * Stores a harvest in the silo if there is available capacity.
     *
     * @param harvest The harvest to be stored in the silo.
     * @return The amount of grain that could not be stored due to capacity limitations.
     */
    public Harvest store(Harvest harvest) {
        // Check if the grain type matches the existing grain in the silo
        if (fillLevel > 0 && stock.peek().getGrainType() != harvest.getGrainType()) {
            throw new IllegalArgumentException("The grain type of the given Harvest does not match the grain type of the silo");
        }

        // Check if there is enough space in the silo
        if (fillLevel >= capacity) {
            return harvest; // The silo is already full, cannot be stored
        }

        // Check if the entire harvest can be stored
        int remainingCapacity = this.capacity - this.fillLevel;
        if(harvest.getAmount() <= remainingCapacity) {
            this.stockIndex++;
            this.stock.add(harvest);
            this.fillLevel += harvest.getAmount();
            return null;
        }
        else {
            // Split the harvest and store the remaining amount
            Harvest remainingHarvest = harvest.split(remainingCapacity);
            this.stockIndex++;
            this.stock.add(remainingHarvest); // Store the remaining harvest in the current depot
            this.fillLevel += remainingHarvest.getAmount();
            return harvest; // Return the surplus amount
        }
    }


    /**
     * Empties the silo by removing all stored harvests and returning them.
     *
     * @return An array containing all the removed harvests from the silo.
     *         If the silo is empty, an empty array is returned.
     */
    public LinkedList<Harvest> emptySilo() {
        if (this.stock.isEmpty()) {
            return null;
        }
        else {
            LinkedList<Harvest> removedHarvests = new LinkedList<Harvest>();
            this.stock.forEach(removedHarvests::addLast);
            this.stock.clear();
            stockIndex = -1;
            fillLevel = 0;
            return removedHarvests;
        }
    }
    public LinkedList<Harvest> getStockCopy() {
        if (this.stock.isEmpty()) {
            return null;
        }
        else {
            LinkedList<Harvest> copy = new LinkedList<Harvest>();
            this.stock.forEach(copy::addLast);
            return copy;
        }
    }


    /**
     * Takes out a specified amount of grain from the silo.
     *
     * @param amount The amount of grain to be taken out.
     * @return The actual amount of grain taken out from the silo.
     */
    public int takeOut(int amount) {
        int takenAmount = 0;

        for(Iterator<Harvest> harvestIterator = this.stock.iterator(); harvestIterator.hasNext() && amount > 0;) {
            Harvest currentHarvest = harvestIterator.next();
            int taken = currentHarvest.remove(amount);
            amount -= taken;
            takenAmount += taken;

            if(currentHarvest.getAmount() == 0) {
                harvestIterator.remove();
            }
        }

        return takenAmount;
    }

    /**
     * Gets the current fill level of the silo.
     *
     * @return The number of harvests currently stored in the silo.
     */
    public int getFillLevel() {
        return this.stock.stream()
                .map(Harvest::getAmount)
                .reduce(0, (accumulator, currentElement) -> accumulator + currentElement);
    }

    /**
     * Gets the capacity of the silo.
     *
     * @return The maximum number of harvests the silo can store.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the grain type stored in the silo.
     *
     * @return A string representation of the grain type.
     */
    public Game.GrainType getGrainType() {
        // Assuming each silo stores only one type of grain, we can retrieve the grain type from the first stored harvest
        if (fillLevel > 0 && !this.stock.isEmpty()) {
            return stock.peek().getGrainType();
        }
        else {
            return null;
        }
    }

    /**
     * Retrieves the number of harvests currently stored in the silo.
     *
     * @return The number of harvests stored in the silo.
     */
    public int getHarvestCount() {
        return this.stock.size();
    }

    /**
     * Simulates the decay of grain in all harvests stored in the silo over time.
     *
     * @param currentYear The current year used to calculate the decay.
     * @return The total amount of grain that decayed in all harvests in the silo.
     */
    public int decay(int currentYear) {
        return this.stock.stream()
                .map(harvest -> harvest.decay(currentYear))
                .reduce(0, (accumulator, decayAmount) -> accumulator + decayAmount);
    }

    /**
     * Retrieves the status of the silo, including its capacity and fill level.
     *
     * @return A SiloStatus object representing the current state of the silo.
     */
    public Status getStatus() {
        return new Status();
    }
}

