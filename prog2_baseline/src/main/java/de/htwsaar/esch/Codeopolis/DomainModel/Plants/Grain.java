package de.htwsaar.esch.Codeopolis.DomainModel.Plants;
import java.util.Random;

import java.util.Random;

/**
 * The `Grain` class represents an abstract grain plant with common attributes and behaviors.
 * Subclasses of `Grain` define specific types of grain plants.
 */
public abstract class Grain {

    public enum Pests {FritFly, DeliaFly, BarleyGoutFly}

    ;

    public enum Diseases {PowderyMildew, LeafDrought, Fusarium}

    private float basicYieldRatio; // The basic yield ratio of the grain.

    private boolean harvested = false; // Indicates if the grain has been harvested.

    private int acresPlanted; // Number of acres where the grain is planted.

    /*
     * yieldRatio is protected and therefore visible in the whole package.
     * Ideally, it would only be visible in the subclasses only.
     * However, this is achieved by having a separate package for grain and all its subclasses.
     *
     * As an alternative, there could also be a private variable in each subclass of Grain.
     * Grain could then define an abstract method "public float getYieldRatio", which is also implemented in the subclasses.
     * However, this would mean a redundant implementation.
     * The getYieldRatio method would be needed since yieldRatio is required in the subclasses as well as in Grain's harvest method.
     */

    /**
     * The yield ratio represents the ratio of yield obtained from the planted grain.
     * It is affected by the quality of soil conditions and the grain's basic yield ratio.
     */
    protected float yieldRatio = 0f;

    /**
     * Gets the current yield ratio of the grain.
     *
     * @return The current yield ratio.
     */
    public float getYieldRatio() {
        return yieldRatio;
    }

    /**
     * The crop failure due to bad conditions represents the percentage of crop failure
     * caused by adverse environmental conditions, such as drought or pests.
     */
    private float cropFailureDueToBadConditions;

    /**
     * Constructs a `Grain` object with the specified basic yield ratio and crop failure due to bad conditions.
     *
     * @param basicYieldRatio               The basic yield ratio of the grain.
     * @param cropFailureDueToBadConditions The crop failure due to bad conditions.
     */
    public Grain(float basicYieldRatio, float cropFailureDueToBadConditions) {
        this.basicYieldRatio = basicYieldRatio;
        this.cropFailureDueToBadConditions = cropFailureDueToBadConditions;
        this.acresPlanted = 0;
    }

    /**
     * Plants the grain on a specified number of acres.
     *
     * @param acres The number of acres to plant the grain.
     * @return True if the planting was successful, false otherwise.
     */
    public boolean plant(int acres) {
        if (acresPlanted > 0) // Cannot double-plant a grain.
            return false;
        if (acres < 0) return false;
        acresPlanted = acres;
        harvested = false;
        return true;
    }

    /**
     * Simulates the growth of the grain based on environmental conditions.
     *
     * @param conditions The environmental conditions affecting the grain's growth.
     */
    public void grow(Conditions conditions) {
        this.yieldRatio = conditions.getSoilConditions() * basicYieldRatio;
    }

    /**
     * Handles drought conditions for the grain.
     */
    public abstract void drought();

    /**
     * Handles pest infestation conditions for the grain.
     *
     * @param pest       The type of pest infestation.
     * @param conditions The environmental conditions affecting the pest infestation.
     */
    public abstract void pestInfestation(Pests pest, Conditions conditions);

    /**
     * Handles disease outbreaks for the grain.
     *
     * @param disease    The type of disease outbreak.
     * @param conditions The environmental conditions affecting the disease outbreak.
     */
    public abstract void diseaseOutbreak(Diseases disease, Conditions conditions);

    /**
     * Harvests the grain, returning the crop yield.
     *
     * @return The crop yield obtained from harvesting the grain.
     */
    public int harvest() {
        if (harvested) return 0;
        int crop = (int) (Math.round(this.acresPlanted * this.yieldRatio));
        harvested = true;
        acresPlanted = 0;
        return crop;
    }

    public static class Conditions {


        private float soilConditions;
        private float averageTemperatureSummer;
        private float averageTemperatureWinter;

        private boolean drought;


        private boolean fusarium;

        private boolean leafDrought;

        private boolean powderyMildew;

        private boolean barleyGoOutFly;

        private boolean deliaFly;
        private boolean fritFly;


        /**
         * Constructs a new `Conditions` object with specified values for soil conditions and temperatures.
         *
         * @param soilConditions           The soil conditions affecting plant growth (a float value between 0.0 and 1.0).
         * @param averageTemperatureSummer The average summer temperature (in degrees Celsius).
         * @param averageTemperatureWinter The average winter temperature (in degrees Celsius).
         */
        public Conditions(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter, boolean drought, boolean fusarium, boolean leafDrought, boolean powderyMildew, boolean barleyGoOutFly, boolean deliaFly, boolean fritFly) {
            this.soilConditions = soilConditions;
            this.averageTemperatureSummer = averageTemperatureSummer;
            this.averageTemperatureWinter = averageTemperatureWinter;
            this.drought = drought;
            this.fusarium = fusarium;
            this.leafDrought = leafDrought;
            this.powderyMildew = powderyMildew;
            this.barleyGoOutFly = barleyGoOutFly;
            this.deliaFly = deliaFly;
            this.fritFly = fritFly;
        }

        /**
         * Gets the soil conditions affecting plant growth.
         *
         * @return The soil conditions (a float value between 0.0 and 1.0).
         */
        public float getSoilConditions() {
            return soilConditions;
        }

        /**
         * Gets the average summer temperature.
         *
         * @return The average summer temperature (in degrees Celsius).
         */
        public float getAverageTemperatureSummer() {
            return averageTemperatureSummer;
        }

        /**
         * Gets the average winter temperature.
         *
         * @return The average winter temperature (in degrees Celsius).
         */
        public float getAverageTemperatureWinter() {
            return averageTemperatureWinter;
        }

        /**
         * Factory method to create a new Conditions object with random values for all fields.
         *
         * @return A new Conditions object with random values.
         */
        public static de.htwsaar.esch.Codeopolis.DomainModel.Plants.Grain.Conditions generateRandomConditions() {
            Random random = new Random();
            float soilConditions = random.nextFloat(); // generates a random float value between 0.0 (inclusive) and 1.0 (exclusive)
            float averageTemperatureSummer = random.nextFloat() * 30.0f; // generates a random float value between 0.0 (inclusive) and 30.0 (exclusive)
            float averageTemperatureWinter = random.nextFloat() * 20.0f - 10.0f; // generates a random float value between -10.0 (inclusive) and 10.0 (exclusive)

            boolean drought = random.nextFloat() > 0.8 ? true : false;
            boolean fusarium = random.nextFloat() > 0.8 ? true : false;
            boolean leafDrought = random.nextFloat() > 0.8 ? true : false;
            boolean powderyMildew = random.nextFloat() > 0.8 ? true : false;
            boolean barleyGoutFly = random.nextFloat() > 0.8 ? true : false;
            boolean deliaFly = random.nextFloat() > 0.8 ? true : false;
            boolean fritFly = random.nextFloat() > 0.8 ? true : false;


            return new de.htwsaar.esch.Codeopolis.DomainModel.Plants.Grain.Conditions(soilConditions, averageTemperatureSummer, averageTemperatureWinter, drought, fusarium, leafDrought, powderyMildew, barleyGoutFly, deliaFly, fritFly);

        }

        public boolean isDrought() {
            return drought;
        }

        public boolean isFusarium() {
            return fusarium;
        }

        public boolean isLeafDrought() {
            return leafDrought;
        }

        public boolean isPowderyMildew() {
            return powderyMildew;
        }

        public boolean isBarleyGoOutFly() {
            return barleyGoOutFly;
        }

        public boolean isDeliaFly() {
            return deliaFly;
        }

        public boolean isFritFly() {
            return fritFly;
        }


    }


    /**
     * Retrieves the basic yield ratio of the grain.
     *
     * @return The basic yield ratio of the grain.
     */
    protected float getBasicYieldRatio() {
        return basicYieldRatio;
    }

    /**
     * Retrieves the crop failure rate due to bad conditions.
     *
     * @return The crop failure rate due to bad conditions.
     */

	protected float getCropFailureDueToBadConditions() {
		return cropFailureDueToBadConditions;
	}

	public static class Conditions {

		private float soilConditions;
		private float averageTemperatureSummer;
		private float averageTemperatureWinter;

		private boolean drought;


		private boolean fusarium;

		private boolean leafDrought;

		private  boolean powderyMildew;

		private  boolean barleyGoOutFly;

		private  boolean deliaFly;
		private boolean fritFly;


		/**
		 * Constructs a new `Conditions` object with specified values for soil conditions and temperatures.
		 *
		 * @param soilConditions         The soil conditions affecting plant growth (a float value between 0.0 and 1.0).
		 * @param averageTemperatureSummer The average summer temperature (in degrees Celsius).
		 * @param averageTemperatureWinter The average winter temperature (in degrees Celsius).
		 */
		public Conditions(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter, boolean drought, boolean fusarium, boolean leafDrought, boolean powderyMildew, boolean barleyGoOutFly, boolean deliaFly, boolean fritFly) {
			this.soilConditions = soilConditions;
			this.averageTemperatureSummer = averageTemperatureSummer;
			this.averageTemperatureWinter = averageTemperatureWinter;
			this.drought = drought;
			this.fusarium = fusarium;
			this.leafDrought = leafDrought;
			this.powderyMildew = powderyMildew;
			this.barleyGoOutFly = barleyGoOutFly;
			this.deliaFly = deliaFly;
			this.fritFly = fritFly;
		}

		/**
		 * Gets the soil conditions affecting plant growth.
		 *
		 * @return The soil conditions (a float value between 0.0 and 1.0).
		 */
		public float getSoilConditions() {
			return soilConditions;
		}

		/**
		 * Gets the average summer temperature.
		 *
		 * @return The average summer temperature (in degrees Celsius).
		 */
		public float getAverageTemperatureSummer() {
			return averageTemperatureSummer;
		}

		/**
		 * Gets the average winter temperature.
		 *
		 * @return The average winter temperature (in degrees Celsius).
		 */
		public float getAverageTemperatureWinter() {
			return averageTemperatureWinter;
		}

		/**
		 * Factory method to create a new Conditions object with random values for all fields.
		 *
		 * @return A new Conditions object with random values.
		 */
		public static Conditions generateRandomConditions() {
			Random random = new Random();
			float soilConditions = random.nextFloat(); // generates a random float value between 0.0 (inclusive) and 1.0 (exclusive)
			float averageTemperatureSummer = random.nextFloat() * 30.0f; // generates a random float value between 0.0 (inclusive) and 30.0 (exclusive)
			float averageTemperatureWinter = random.nextFloat() * 20.0f - 10.0f; // generates a random float value between -10.0 (inclusive) and 10.0 (exclusive)

			boolean drought = random.nextFloat() > 0.8 ? true : false;
			boolean fusarium = random.nextFloat() > 0.8 ? true : false;
			boolean leafDrought = random.nextFloat() > 0.8 ? true : false;
			boolean powderyMildew = random.nextFloat() > 0.8 ? true : false;
			boolean barleyGoutFly = random.nextFloat() > 0.8 ? true : false;
			boolean deliaFly = random.nextFloat() > 0.8 ? true : false;
			boolean fritFly = random.nextFloat() > 0.8 ? true : false;


			return new Conditions(soilConditions,averageTemperatureSummer,averageTemperatureWinter,drought,fusarium,
					leafDrought,powderyMildew,barleyGoutFly,deliaFly,fritFly);

		}

		public boolean isDrought() {
			return drought;
		}

		public boolean isFusarium() {
			return fusarium;
		}

		public boolean isLeafDrought() {
			return leafDrought;
		}

		public boolean isPowderyMildew() {
			return powderyMildew;
		}

		public boolean isBarleyGoOutFly() {
			return barleyGoOutFly;
		}

		public boolean isDeliaFly() {
			return deliaFly;
		}

		public boolean isFritFly() {
			return fritFly;
		}
	}

    protected float getCropFailureDueToBadConditions() {
        return cropFailureDueToBadConditions;
    }


}