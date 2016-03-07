/*
 * #%L
 * avt
 * %%
 * Copyright (C) 2014 - 2016 IRIT - SMAC Team
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package fr.irit.smac.libs.tooling.avt;

/**
 * The Enum EMessageException.
 */
public enum EMessageException {

    /** LowerBound is NaN. */
    LOWER_BOUND_NAN("lowerBound isNaN"),

    /** UpperBound is NaN. */
    UPPER_BOUND_NAN("upperBound isNaN"),

    /** StartValue is NaN. */
    START_VALUE_NAN("startValue isNaN"),

    /** LowerBound is greater than upperBbound. */
    LOWER_BOUND_GT_UPPER_BOUND("lowerBound > upperBound"),

    /** DeltaManagerFactory is null. */
    DELTA_MANAGER_FACTORY_NULL("deltaManagerFactory == null"),

    /** Value is NaN. */
    VALUE_NAN("value isNaN"),

    /** Feedback is null. */
    FEEDBACK_NULL("feedback is null"),

    /** Criticity is NaN. */
    CRITICITY_NAN("criticity isNaN"),

    /** Criticity is lower than 0. */
    CRITICITY_LT_0("criticity < 0"),

    /** Criticity is greater than 1. */
    CRITICITY_GT_1("criticity > 1"),

    /** DeltaMin is NaN. */
    DELTA_MIN_NAN("deltaMin isNaN"),

    /** Range is null. */
    RANGE_NULL("range is null"),

    /** DeltaMin is lower than or equal to 0. */
    DELTA_MIN_LE_0("deltaMin <= 0"),

    /** DeltaMin is lower than 0. */
    DELTA_MIN_LT_0("deltaMin < 0"),

    /** Delta evolution is null. */
    DELTA_EVOLUTION_NULL("deltaEvolution == null"),

    /** IncreaseFactor is lower than or equal to 1. */
    DELTA_EVOLUTION_INCREASE_FACTOR_LE_1("deltaEvolution.getIncreaseFactor() <= 1."),

    /** DeltaMax is NaN. */
    DELTA_MAX_NAN("deltaMax isNaN"),

    /** DeltaMax is lower than deltaMin. */
    DELTA_MAX_LT_DELTA_MIN("deltaMax < deltaMin"),

    /** DeltaMin is greater than deltaMax. */
    DELTA_MIN_GT_DELTA_MAX("deltaMin > this.deltaMax"),

    /** Direction is null. */
    DIRECTION_NULL("direction is null"),

    /** Delta is NaN. */
    DELTA_NAN("delta isNaN"),

    /** DeltaMax is lower than deltaMin. */
    DELTA_MAX_LT_THIS_DELTA_MIN("deltaMax < this.deltaMin"),

    /** GeometricFactor is NaN. */
    GEOMETRIC_FACTOR_BOUND_NAN("geometricFactor isNaN"),

    /** DeltaMin is greater than or equal to range. */
    DELTA_MIN_GE_RANGE("deltaMin => range"),

    /** Range is greather than DOUBLE.MAX_VALUE*2. */
    RANGE_GT_MAX_VALUE_TIME_2("range > Double.MAX_VALUE * 2"),

    /** GeometricStepNumber is wrong. */
    GEOMETRIC_STEP_NUMBER_BOUND("if geometricStepNumber < 1 or geometricStepNumber > this.getNbGeometricSteps()"),

    /** IncreaseFactor delta. */
    INCREASE_FACTOR_DELTA("increaseFactor delta"),

    /** IncreaseFactor is lower than 1. */
    INCREASE_FACTOR_LT_1("increase factor < 1"),

    /** DecreaseFactor is lower than 1. */
    DECREASE_FACTOR_LT_1("decrease factor < 1"),

    /** DecreaseFactor is NaN. */
    DECREASE_FACTOR_NAN("decreaseFactor is NaN"),

    /** IncreaseFactor is NaN. */
    INCREASE_FACTOR_NAN("increaseFactor is NaN"),

    /** DecreaseNoise is lower than or equal to 0. */
    DECREASE_NOISE_LT_ET_0("decreaseNoise <= 0"),

    /** DecreaseNoise is NaN. */
    DECREASE_NOISE_NAN("decreaseNoise is NaN"),

    /** DecreaseFactor minus decreaseNoise is lower than 1. */
    DECREASE_FACTOR_SUB_DECREASE_NOISE_LT_1("decreaseFactor - decreaseNoise < 1"),

    /** dmDecison is null. */
    DM_DECISION("dmDecison == null"),

    /** AvtFactory is null. */
    AVT_FACTORY_NULL("avtFactory is not set"),

    /** StartValue is greater than upperBound. */
    START_VALUE_GT_UPPER_BOUND("startValue > upperBound"),

    /** LowerBound is greater than startValue. */
    LOWER_BOUND_GT_START_VALUE("lowerBound > startValue"),

    /** LowerBound is greather than or equal to upperBound. */
    LOWER_BOUND_GE_UPPER_BOUND("lowerBound >= upperBound"),

    /** DecreaseDelay is lower than 0. */
    DECREASE_DELAY_LT_0("decreaseDelay < 0"),

    /** IncreaseDelay is lower than 0. */
    INCREASE_DELAY_LT_0("increaseDelay < 0"),

    /** Call deterministic(false) before. */
    CALL_DETERMINISTIC_BEFORE(
        "cannot set decreaseNoise if delta decrease is deterministic, call this.deterministic(false) before"),

    /** Call isHardBounds(false) before. */
    CALL_HARD_BOUNDS_BEFORE(
        "cannot set softBoundsMemory when hardBounds are set to true, call this.isHardBounds(false) before"),

    /** Call avtFactory(null) before. */
    CALL_AVT_FACTORY_BEFORE(
        "cannot set hardBound option when an avt factory is already set. Call this.avtFactory(null) before."),

    /** SoftBoundsMemory is lower than 0. */
    SOFT_BOUNDS_MEMORY_LT_0("softBoundsMemory < 0");

    /** The text. */
    private String text;

    /**
     * Instantiates a new e message exception.
     *
     * @param text
     *            the text
     */
    private EMessageException(String text) {
        this.text = text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

}
