/*
 * #%L
 * avt
 * %%
 * Copyright (C) 2014 - 2015 IRIT - SMAC Team
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

import java.math.BigDecimal;

import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory;
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDEFactory;
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl.DeterministicGDEFactory;
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl.UndeterministicGDEFactory;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecisionFactory;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.DelayedDMDFactory;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.StandardDMDFactory;
import fr.irit.smac.libs.tooling.avt.deltamanager.impl.BoundedDMFactory;
import fr.irit.smac.libs.tooling.avt.deltamanager.impl.StandardDMFactory;

/**
 * The Class AbstractAVTBuilder.
 *
 * @author Sylvain Lemouzy
 * @param <T>
 *            the generic type
 */
public abstract class AbstractAVTBuilder<T extends IAVT> implements IAVTBuilder<T> {

    /** The lower bound. */
    // AVT Parameters
    protected Double                  lowerBound           = Double.NEGATIVE_INFINITY;

    /** The upper bound. */
    protected Double                  upperBound           = Double.POSITIVE_INFINITY;

    /** The start value. */
    protected Double                  startValue           = null;

    /** The is hard bounds. */
    protected boolean                 isHardBounds         = true;

    /** The soft bounds memory. */
    protected int                     softBoundsMemory     = 0;

    /** The avt factory. */
    protected IAVTFactory<T>          avtFactory           = null;

    /** The delta manager factory. */
    // Delta Parameters
    protected IDeltaManagerFactory<?> deltaManagerFactory  = null;

    /** The delta min. */
    protected Double                  deltaMin             = null;

    /** The delta max. */
    protected Double                  deltaMax             = null;

    /** The is bounded delta. */
    protected boolean                 isBoundedDelta       = true;

    /** The is delayed delta. */
    protected boolean                 isDelayedDelta       = false;

    /** The delta increase delay. */
    protected int                     deltaIncreaseDelay   = 1;

    /** The delta decrease delay. */
    protected int                     deltaDecreaseDelay   = 1;

    /** The delta increase factor. */
    protected double                  deltaIncreaseFactor  = 2.;

    /** The delta decrease factor. */
    protected double                  deltaDecreaseFactor  = 3.;

    /** The is deterministic delta. */
    protected boolean                 isDeterministicDelta = true;

    /** The delta decrease noise. */
    protected double                  deltaDecreaseNoise   = 0.5;

    /** The delta random seed. */
    protected Long                    deltaRandomSeed      = null;

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#isHardBounds(boolean)
     */
    @Override
    public AbstractAVTBuilder<T> isHardBounds(boolean isHardBounds) {
        if (this.avtFactory != null) {
            throw new IllegalStateException(EMessageException.CALL_AVT_FACTORY_BEFORE.toString());
        }
        this.isHardBounds = isHardBounds;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#avtFactory(fr.irit.smac.util.avt.
     * AVTFactory)
     */
    @Override
    public AbstractAVTBuilder<T> avtFactory(IAVTFactory<T> avtFactory) {
        this.avtFactory = avtFactory;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#softBoundsMemory(int)
     */
    @Override
    public AbstractAVTBuilder<T> softBoundsMemory(int softBoundsMemory) {
        if (softBoundsMemory < 0) {
            throw new IllegalArgumentException(EMessageException.SOFT_BOUNDS_MEMORY_LT_0.toString());
        }
        if (this.isHardBounds) {
            throw new IllegalStateException(EMessageException.CALL_HARD_BOUNDS_BEFORE.toString());
        }
        this.softBoundsMemory = softBoundsMemory;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#isDelayedDelta(boolean)
     */
    @Override
    public AbstractAVTBuilder<T> isDelayedDelta(boolean isDelayed) {
        this.isDelayedDelta = isDelayed;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#isBoundedDelta(boolean)
     */
    @Override
    public AbstractAVTBuilder<T> isBoundedDelta(boolean isBounded) {
        this.isBoundedDelta = isBounded;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#isDeterministicDelta(boolean)
     */
    @Override
    public AbstractAVTBuilder<T> isDeterministicDelta(boolean isDeterministic) {
        this.isDeterministicDelta = isDeterministic;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#deltaIncreaseFactor(double)
     */
    @Override
    public AbstractAVTBuilder<T> deltaIncreaseFactor(double increaseFactor) {
        if (increaseFactor < 1.) {
            throw new IllegalArgumentException(EMessageException.INCREASE_FACTOR_LT_1.toString());
        }
        this.deltaIncreaseFactor = increaseFactor;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#deltaDecreaseFactor(double)
     */
    @Override
    public AbstractAVTBuilder<T> deltaDecreaseFactor(double decreaseFactor) {
        if (decreaseFactor < 1.) {
            throw new IllegalArgumentException(EMessageException.DECREASE_FACTOR_LT_1.toString());
        }
        this.deltaDecreaseFactor = decreaseFactor;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#deltaDecreaseNoise(double)
     */
    @Override
    public AbstractAVTBuilder<T> deltaDecreaseNoise(double decreaseNoise) {
        if (this.isDeterministicDelta) {
            throw new IllegalStateException(
                EMessageException.CALL_DETERMINISTIC_BEFORE.toString());
        }
        if (decreaseNoise <= 0) {
            throw new IllegalArgumentException(EMessageException.DECREASE_NOISE_LT_ET_0.toString());
        }
        this.deltaDecreaseNoise = decreaseNoise;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#deltaRandomSeed(long)
     */
    @Override
    public AbstractAVTBuilder<T> deltaRandomSeed(long seed) {
        this.isDeterministicDelta = false;
        this.deltaRandomSeed = seed;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#deltaIncreaseDelay(int)
     */
    @Override
    public AbstractAVTBuilder<T> deltaIncreaseDelay(int increaseDelay) {
        if (increaseDelay < 0) {
            throw new IllegalArgumentException(EMessageException.INCREASE_DELAY_LT_0.toString());
        }
        this.isDelayedDelta = true;
        this.deltaIncreaseDelay = increaseDelay;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#deltaDecreaseDelay(int)
     */
    @Override
    public AbstractAVTBuilder<T> deltaDecreaseDelay(int decreaseDelay) {
        if (decreaseDelay < 0) {
            throw new IllegalArgumentException(EMessageException.DECREASE_DELAY_LT_0.toString());
        }
        this.isDelayedDelta = true;
        this.deltaDecreaseDelay = decreaseDelay;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#deltaMin(double)
     */
    @Override
    public AbstractAVTBuilder<T> deltaMin(double deltaMin) {
        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(EMessageException.DELTA_MIN_NAN.toString());
        }
        this.deltaMin = deltaMin;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#deltaMax(double)
     */
    @Override
    public AbstractAVTBuilder<T> deltaMax(double deltaMax) {
        if (Double.isNaN(deltaMax)) {
            throw new IllegalArgumentException(EMessageException.DELTA_MAX_NAN.toString());
        }
        this.deltaMax = deltaMax;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#lowerBound(double)
     */
    @Override
    public AbstractAVTBuilder<T> lowerBound(double lowerBound) {
        if (Double.isNaN(lowerBound)) {
            throw new IllegalArgumentException(EMessageException.LOWER_BOUND_NAN.toString());
        }
        this.lowerBound = lowerBound;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#upperBound(double)
     */
    @Override
    public AbstractAVTBuilder<T> upperBound(double upperBound) {
        if (Double.isNaN(upperBound)) {
            throw new IllegalArgumentException(EMessageException.UPPER_BOUND_NAN.toString());
        }
        this.upperBound = upperBound;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#startValue(double)
     */
    @Override
    public AbstractAVTBuilder<T> startValue(double startValue) {
        if (Double.isNaN(startValue)) {
            throw new IllegalArgumentException(EMessageException.START_VALUE_NAN.toString());
        }
        this.startValue = startValue;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.util.avt.IAVTBuilder#deltaManagerFactory(fr.irit.smac.util.
     * avt.deltamanager.DeltaManagerFactory)
     */
    @Override
    public AbstractAVTBuilder<T> deltaManagerFactory(IDeltaManagerFactory<?> deltaManagerFactory) {
        if (deltaManagerFactory == null) {
            throw new IllegalArgumentException(EMessageException.DELTA_MANAGER_FACTORY_NULL.toString());
        }

        this.deltaManagerFactory = deltaManagerFactory;

        return this;
    }

    /**
     * Gets the portion of range.
     *
     * @param portion
     *            the portion
     * @return the portion of range
     */
    protected double getPortionOfRange(double portion) {

        return this.getRange().multiply(BigDecimal.valueOf(portion)).doubleValue();
    }

    /**
     * Gets the range.
     *
     * @return the range
     */
    protected BigDecimal getRange() {
        if (this.lowerBound > this.upperBound) {
            throw new IllegalStateException(EMessageException.LOWER_BOUND_GT_UPPER_BOUND.toString());
        }

        BigDecimal lb = getBigDecValueOf(this.lowerBound);
        BigDecimal ub = getBigDecValueOf(this.upperBound);

        return ub.subtract(lb);
    }

    /**
     * Gets the big dec value of.
     *
     * @param value
     *            the value
     * @return the big dec value of
     */
    protected static BigDecimal getBigDecValueOf(double value) {

        BigDecimal bdVal;
        if (value <= Double.NEGATIVE_INFINITY) {
            bdVal = BigDecimal.valueOf(-Double.MAX_VALUE);
        }
        else if (value >= Double.POSITIVE_INFINITY) {
            bdVal = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        else {
            bdVal = BigDecimal.valueOf(value);
        }

        return bdVal;
    }

    /**
     * Gets the range middle.
     *
     * @return the range middle
     */
    protected double getRangeMiddle() {
        if (this.lowerBound > this.upperBound) {
            throw new IllegalStateException(EMessageException.LOWER_BOUND_GT_UPPER_BOUND.toString());
        }

        return (this.getRange().divide(BigDecimal.valueOf(2.)).add(getBigDecValueOf(this.lowerBound))).doubleValue();
    }

    /**
     * throws a lot of exceptions, that are described in the doc of the build()
     * function.
     */
    protected void checkStateConsistency() {

        checkLowerBound();
        checkStartValue();
        checkDeltaMin();
        checkAvtFactory();
    }

    /**
     * Check lower bound.
     */
    protected void checkLowerBound() {

        if (this.lowerBound >= this.upperBound) {
            throw new IllegalStateException(EMessageException.LOWER_BOUND_GE_UPPER_BOUND.toString());
        }
    }

    /**
     * Check start value.
     */
    protected void checkStartValue() {

        if (this.startValue != null) {
            if (this.lowerBound > this.startValue) {
                throw new IllegalStateException(EMessageException.LOWER_BOUND_GT_START_VALUE.toString());
            }

            if (this.startValue > this.upperBound) {
                throw new IllegalStateException(EMessageException.START_VALUE_GT_UPPER_BOUND.toString());
            }
        }
    }

    /**
     * Check delta min.
     */
    protected void checkDeltaMin() {

        if (this.deltaMin != null) {
            if (this.deltaMin < 0) {
                throw new IllegalStateException(EMessageException.DELTA_MIN_LT_0.toString());
            }

            if (this.deltaMax != null && this.deltaMin > this.deltaMax) {
                throw new IllegalStateException(EMessageException.DELTA_MIN_GT_DELTA_MAX.toString());
            }
        }
    }

    /**
     * Check avt factory.
     */
    protected void checkAvtFactory() {

        if (this.avtFactory == null) {
            throw new IllegalStateException(EMessageException.AVT_FACTORY_NULL.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#build()
     */
    @Override
    public T build() {
        // check for state consistency
        this.checkStateConsistency();

        // 1. set the start value if not set
        if (this.startValue == null) {
            this.startValue = this.getRangeMiddle();
        }

        // 2. Instanciate DeltaManager
        if (this.deltaManagerFactory == null) {

            // 2.1 instanciation of evolution
            IGeometricDEFactory deltaEvolutionFactory;

            if (this.isDeterministicDelta) {
                deltaEvolutionFactory = new DeterministicGDEFactory(this.deltaIncreaseFactor, this.deltaDecreaseFactor);
            }
            else {
                deltaEvolutionFactory = new UndeterministicGDEFactory(this.deltaIncreaseFactor,
                    this.deltaDecreaseFactor, this.deltaDecreaseNoise, this.deltaRandomSeed);
            }

            // 2.2 instanciate the decision
            IDMDecisionFactory dmDecisionFactory = new StandardDMDFactory();

            if (this.isDelayedDelta) {
                dmDecisionFactory = new DelayedDMDFactory(dmDecisionFactory, this.deltaIncreaseDelay,
                    this.deltaDecreaseDelay);
            }

            // 2.3 instanciate the standard DM
            if (this.deltaMin == null) {
                // initialisation of deltaMin to 1% of the range
                this.deltaMin = this.getPortionOfRange(0.01);
            }

            if (this.deltaMax == null) {
                this.deltaManagerFactory = new StandardDMFactory(deltaEvolutionFactory, dmDecisionFactory,
                    this.deltaMin);
            }
            else {
                this.deltaManagerFactory = new StandardDMFactory(deltaEvolutionFactory, dmDecisionFactory,
                    this.deltaMin, this.deltaMax);
            }

            if (this.isBoundedDelta) {
                this.deltaManagerFactory = new BoundedDMFactory(this.deltaManagerFactory);
            }

        }

        return this.avtFactory.createInstance(this.lowerBound, this.upperBound, this.startValue,
            this.deltaManagerFactory);
    }
}