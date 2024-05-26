package de.htwsaar.esch.Codeopolis.DomainModel.Storage;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.GameConfig;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.Harvest;

import java.text.DecimalFormat;
import java.util.NoSuchElementException;

public class Depot {
    private LinkedList<Silo> silos;

    /**
     * Constructs a Depot object with the specified number of silos and capacity per silo.
     *
     * @param numberOfSilos   The number of silos in the depot.
     * @param capacityPerSilo The capacity per silo.
     */
    public Depot(int numberOfSilos, int capacityPerSilo) {
        this.silos = new LinkedList<>();
        for (int i = 0; i < numberOfSilos; i++) {
            this.silos.addLast(new Silo(capacityPerSilo));
        }
    }

    /**
     * Constructs a Depot object with the specified array of silos.
     * Each silo in the array is deeply copied to ensure that the Depot has its own separate instances.
     *
     * @param silosList The array of Silo objects to be copied into the depot.
     */
    public Depot(LinkedList<Silo> silosList) {   // probably used when a Game is loaded
        this.silos = new LinkedList<>();
        if (silosList == null) {
        } else {
            LinkedList<Silo>.LinkedIterator<Silo> iter = silosList.makeIterator();
            while (iter.hasNext()) {
                this.silos.addLast(iter.next());
            }
        }
    }

    /**
     * Retrieves the current fill level of the depot for a specific grain type.
     *
     * @param grainType The grain type for which to retrieve the fill level.
     * @return The total amount of grain stored in the depot for the specified grain type.
     */
    public int getFillLevel(Game.GrainType grainType) {
        int totalFillLevel = 0;
        Iterator iterator = new DepotIterator(grainType);

        // Add the fill level as long as there is another silo with the correct GrainType
        while (iterator.hasNext()) {
            Silo.Status currStatus = iterator.next();
            totalFillLevel += currStatus.getFillLevel();
        }
        return totalFillLevel;
    }

    /**
     * Creates and returns a copy of the silos array.
     * This method creates a new array and populates it with copies of the Silo objects,
     * ensuring that modifications to the returned array do not affect the original silos.
     *
     * @return A copy of the silos array.
     */
    public LinkedList<Silo> getSilos() {
        // Create a new array of Silo with the same length as the original

        LinkedList<Silo> silosCopy = new LinkedList<>();

        LinkedList<Silo>.LinkedIterator<Silo> iter = silosCopy.makeIterator();

        while (iter.hasNext()) {
            Silo currSilo = iter.next();
            silosCopy.addLast(currSilo);
        }

        return silosCopy;
    }

    /**
     * Gets the total amount of bushels (grain) stored in the depot.
     *
     * @return The total amount of bushels stored in the depot.
     */
    public int getTotalFillLevel() {
        int totalBushels = 0;

        LinkedList<Silo>.LinkedIterator<Silo> iter = silos.makeIterator();

        while (iter.hasNext()) {
            Silo currSilo = iter.next();
            totalBushels += currSilo.getFillLevel();
        }


        return totalBushels;
    }

    /**
     * Retrieves the capacity of the depot for a specific grain type.
     * TEsting
     *
     * @param grainType The grain type for which to retrieve the capacity.
     * @return The total capacity of the depot for the specified grain type.
     */
    public int getCapacity(Game.GrainType grainType) {
        int totalCapacity = 0;
        Iterator iterator = new DepotIterator(grainType);


        // Add the capacity as long as there is another silo with the correct GrainType
        while (iterator.hasNext()) {
            Silo.Status currStatus = iterator.next();
            totalCapacity += currStatus.getCapacity();
        }

        return totalCapacity;
    }

    /**
     * Stores a harvest in the depot.
     *
     * @param harvest The harvest to be stored in the depot.
     * @return True if the harvest was successfully stored, false otherwise.
     */
    public boolean store(Harvest harvest) {

        LinkedList<Silo>.LinkedIterator<Silo> iter = silos.makeIterator();

        while (iter.hasNext()) {
            Silo currSilo = iter.next();
            if (currSilo.getGrainType() == harvest.getGrainType() || currSilo.getFillLevel() == 0) {
                harvest = currSilo.store(harvest);
                if (harvest == null) {
                    return true;
                }
            }
        }


        defragment();

        LinkedList<Silo>.LinkedIterator<Silo> iter2 = silos.makeIterator();

        while (iter2.hasNext()) {


            Silo currSilo = iter2.next();
            if (currSilo.getGrainType() == harvest.getGrainType() || currSilo.getFillLevel() == 0) {
                harvest = currSilo.store(harvest);
                if (harvest == null) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Takes out a specified amount of grain from the depot for a specific grain type.
     *
     * @param amount    The amount of grain to be taken out.
     * @param grainType The grain type for which to take out the grain.
     * @return The actual amount of grain taken out from the depot.
     */
    public int takeOut(int amount, Game.GrainType grainType) {
        int takenAmount = 0;

        LinkedList<Silo>.LinkedIterator<Silo> iter = silos.makeIterator();

        while (iter.hasNext() && amount > 0) {
            Silo currSilo = iter.next();
            if (currSilo.getGrainType() == grainType) {
                int taken = currSilo.takeOut(amount);
                amount -= taken;
                takenAmount += taken;
            }
        }

        return takenAmount;
    }

    /**
     * Takes out the specified amount of grain from the silo, distributing it evenly among the stored bushels.
     * If the specified amount exceeds the total amount of grain in the silo, all grain is removed and returned.
     * If the specified amount is less than the total amount of grain, the grain is taken out evenly from each bushel,
     * with any remaining grain distributed among the bushels in a round-robin fashion.
     *
     * @param amount The amount of grain to be taken out from the silo.
     * @return The actual amount of grain taken out from the silo.
     */
    public int takeOut(int amount) {
        if (amount >= this.getTotalFillLevel()) {
            int totalAmountOfBushels = this.getTotalFillLevel();

            LinkedList<Silo>.LinkedIterator<Silo> iter = silos.makeIterator();

            while (iter.hasNext()) {
                Silo currSilo = iter.next();
                currSilo.emptySilo();
            }

            return totalAmountOfBushels;
        }


        int partion = amount / this.silos.size();   // length
        int remainder = amount % this.silos.size(); // length

        LinkedList<Silo>.LinkedIterator<Silo> iter2 = silos.makeIterator();

        while (iter2.hasNext()) {
            Silo currSilo = iter2.next();
            if (currSilo.getFillLevel() < partion) {
                remainder += partion - currSilo.getFillLevel();
                currSilo.emptySilo();
            } else {
                currSilo.takeOut(partion);
            }
        }


        int j = 0;
        while (remainder > 0) {
            if (this.silos.get(j).getFillLevel() > 0) {
                this.silos.get(j).takeOut(1);
                remainder--;
            }
            j = (j + 1) % Game.GrainType.values().length;
        }
        return amount;
    }

    /**
     * Expands the depot by adding more silos with the specified capacity per silo.
     *
     * @param numberOfSilos   The number of silos to add.
     * @param capacityPerSilo The capacity per silo.
     */
    public void expand(int numberOfSilos, int capacityPerSilo) {
        for (int i = 0; i < numberOfSilos; i++) {
            silos.addLast(new Silo(capacityPerSilo));
        }
        this.takeOut((int) (numberOfSilos * GameConfig.DEPOT_EXPANSION_COST)); //#Issue42
    }

    /**
     * Performs defragmentation on the depot to redistribute grain across silos.
     */
    public void defragment() {
        LinkedList<Harvest> allHarvests = new LinkedList<>();

        LinkedList<Silo>.LinkedIterator<Silo> iter = silos.makeIterator();
        while (iter.hasNext()) {
            Silo silo = iter.next();
            LinkedList<Harvest> siloHarvests = silo.emptySilo();
            if(siloHarvests != null) {

                LinkedList<Harvest>.LinkedIterator<Harvest> harvestIter = siloHarvests.makeIterator();
                while (harvestIter.hasNext()) {
                    allHarvests.addLast(harvestIter.next());
                }
            }
        }
        }


    /**
     * Retrieves the total count of harvests across all silos.
     *
     * @return The total count of harvests stored in all silos combined.
     */
    private int getTotalHarvestCount() {
        int totalCount = 0;
        LinkedList<Silo>.LinkedIterator<Silo> iter = silos.makeIterator();
        while (iter.hasNext()){
            totalCount += iter.next().getHarvestCount();
        }
        return totalCount;
    }


    /**
     * Simulates the decay of grain in the depot over time.
     *
     * @return The total amount of grain that decayed in the depot.
     */
    public int decay(int currentYear) {
        int totalDecayedAmount = 0;
        LinkedList<Silo>.LinkedIterator<Silo> iter = silos.makeIterator();
        while (iter.hasNext()){
            totalDecayedAmount += iter.next().decay(currentYear);
        }
        return totalDecayedAmount;
    }


    /**
     * Checks if the depot is fully occupied with grain.
     *
     * @return {@code true} if the total fill level of all silos equals or exceeds the total capacity of the storage system, {@code false} otherwise.
     */
    public boolean full() {
        if (this.getTotalFillLevel() >= this.totalCapacity()) return true;
        return false;
    }

    /**
     * Calculates the total capacity of the depot by summing the capacities of all silos.
     *
     * @return The total capacity of the storage system.
     */
    public int totalCapacity() {
        int totalCapacity = 0;
        LinkedList<Silo>.LinkedIterator<Silo> iter = silos.makeIterator();
        while (iter.hasNext()){
            totalCapacity += iter.next().getCapacity();
        }
        return totalCapacity;
    }

    /**
     * Retrieves the total amount of grain categorized by grain type.
     *
     * @return An array containing the total amount of grain for each grain type, indexed by the grain type constants defined in the {@code GameConfig} class.
     */
    public int[] getBushelsCategorizedByGrainType() {
        int[] result = new int[Game.GrainType.values().length];
        for (Game.GrainType grainType : Game.GrainType.values()) {
            result[grainType.ordinal()] = getFillLevel(grainType);
        }
        return result;
    }


    /**
     * Returns a string representation of the depot, including information about each silo's grain type, fill level, capacity, and absolute amount of grain.
     *
     * @return A string containing information about the depot, including each silo's grain type, fill level, capacity, and absolute amount of grain.
     */
    @Override
    public String toString() {


        // creating the local class
        class DepotVisualizer {

            private StringBuilder builder = new StringBuilder();
            private DecimalFormat df = new DecimalFormat("0.00");

            private int index = 0;

            // adding information to a silo
            public void appendSiloInfo(Silo silo) {

                builder.append("Silo ").append(index + 1).append(": ");

                String grainName = (silo.getGrainType() != null) ? silo.getGrainType().toString() : "EMPTY";
                builder.append(grainName).append("\n");

                int fillLevel = silo.getFillLevel();
                int capacity = silo.getCapacity();

                double fillPercentage = (double) fillLevel / capacity * 100;
                int fillBarLength = 20;

                int filledBars = (int) (fillPercentage / 100 * fillBarLength);
                int emptyBars = fillBarLength - filledBars;

                builder.append("Amount of Grain: ").append(fillLevel).append(" units\n");
                builder.append("|");

                for (int j = 0; j < filledBars; j++) {
                    builder.append("=");
                }

                for (int j = 0; j < emptyBars; j++) {
                    builder.append("-");
                }

                builder.append("| ").append(df.format(fillPercentage)).append("% filled\n");
                builder.append("Capacity: ").append(capacity).append(" units\n\n");

                index++;
            }

            // make a visual representation of the Depot
            public String visualize() {
                //simply return a string representation of the builder
                return builder.toString();
            }
        }

        silos.sort();
        //From exercise sheet 3
        DepotVisualizer result = new DepotVisualizer();

        //iteratively append information to silo
        for (int i = 0; i < silos.size(); i++) {

            result.appendSiloInfo(silos.get(i));
        }


        // Rückgabe der String-Repräsentation des Depots
        return result.visualize();
    }


    public interface Iterator {
        /**
         * Checks if there are further objects available for iteration.
         *
         * @return {@code true} if more objects are available; {@code false} otherwise.
         */
        boolean hasNext();

        /**
         * Returns the next {@link Silo.Status} object in the iteration.
         * This method should only be called if {@code hasNext()} returns {@code true}.
         *
         * @return The next {@link Silo.Status} object.
         * @throws NoSuchElementException if no more elements are available.
         */
        Silo.Status next();
    }

    private class DepotIterator implements Iterator {

        private Game.GrainType iteratorGrainType;

        //TODO: does it make sense to set it to -1 ?
        private int currentIndex = -1;
        private int temp;

        private DepotIterator(Game.GrainType grainTypeToIterate) {
            iteratorGrainType = grainTypeToIterate;
        }

        @Override
        public boolean hasNext() {

            for (int i = currentIndex + 1; i < silos.size(); i++) {
                if (silos.get(i).getGrainType() == iteratorGrainType || silos.get(i).getFillLevel() == 0) {
                    temp = i;
                    return true;
                }
            }
            return false;
        }

        @Override
        public Silo.Status next() {
            // if the next Silo is available and has the same GrainType
            //TODO: do we really need the if case?
            if (hasNext()) {
                //TODO: return new Silo status
                currentIndex = temp;
                return silos.get(currentIndex).getStatus();
            } else {
                throw new NoSuchElementException("No next element there!");
            }
        }
    }

    // public method to create an Iterator Object outside the Depot class,
    public Iterator createIterator(Game.GrainType grainType) {
        return new DepotIterator(grainType);
    }


}
