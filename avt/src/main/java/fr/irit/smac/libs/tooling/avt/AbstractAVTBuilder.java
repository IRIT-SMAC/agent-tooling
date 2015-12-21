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
 * Yeah !
 * 
 * @author Sylvain Lemouzy
 */
public abstract class AbstractAVTBuilder<T extends IAVT> implements IAVTBuilder<T> {

    // AVT Parameters
    protected Double                  lowerBound           = Double.NEGATIVE_INFINITY;
    protected Double                  upperBound           = Double.POSITIVE_INFINITY;
    protected Double                  startValue           = null;
    protected boolean                 isHardBounds         = true;
    protected int                     softBoundsMemory     = 0;
    protected IAVTFactory<T>          avtFactory           = null;

    // Delta Parameters
    protected IDeltaManagerFactory<?> deltaManagerFactory  = null;

    protected Double                  deltaMin             = null;
    protected Double                  deltaMax             = null;

    protected boolean                 isBoundedDelta       = true;

    protected boolean                 isDelayedDelta       = false;
    protected int                     deltaIncreaseDelay   = 1;
    protected int                     deltaDecreaseDelay   = 1;

    protected double                  deltaIncreaseFactor  = 2.;
    protected double                  deltaDecreaseFactor  = 3.;
    protected boolean                 isDeterministicDelta = true;
    protected double                  deltaDecreaseNoise   = 0.5;
    protected Long                    deltaRandomSeed      = null;

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.util.avt.IAVTBuilder#isHardBounds(boolean)
     */
    @Override
    public AbstractAVTBuilder<T> isHardBounds(boolean isHardBounds) {
        if (this.avtFactory != null) {
            throw new IllegalStateException(
                "cannot set hardBound option when an avt factory is already set. Call this.avtFactory(null) before.");
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
            throw new IllegalArgumentException("softBoundsMemory < 0");
        }
        if (this.isHardBounds) {
            throw new IllegalStateException(
                "cannot set softBoundsMemory when hardBounds are set to true, call this.isHardBounds(false) before");
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
            throw new IllegalArgumentException("Increase factor < 1.");
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
            throw new IllegalArgumentException("Decrease factor < 1.");
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
                "cannot set decreaseNoise if delta decrease is deterministic, call this.deterministic(false) before");
        }
        if (decreaseNoise <= 0) {
            throw new IllegalArgumentException("DecreaseNoise <= 0");
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
            throw new IllegalArgumentException("increaseDelay < 0");
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
            throw new IllegalArgumentException("decreaseDelay < 0");
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
            throw new IllegalArgumentException("deltaMin isNaN");
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
            throw new IllegalArgumentException("deltaMax isNaN");
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
            throw new IllegalArgumentException("lowerBound isNaN");
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
            throw new IllegalArgumentException("upperBound isNaN");
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
            throw new IllegalArgumentException("startValue isNaN");
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
            throw new IllegalArgumentException("deltaManage == null");
        }

        this.deltaManagerFactory = deltaManagerFactory;

        return this;
    }

    protected double getPortionOfRange(double portion) {

        return this.getRange().multiply(BigDecimal.valueOf(portion)).doubleValue();
    }

    protected BigDecimal getRange() {
        if (this.lowerBound > this.upperBound) {
            throw new IllegalStateException("lowerBound greater than upperBound");
        }

        BigDecimal lb = getBigDecValueOf(this.lowerBound);
        BigDecimal ub = getBigDecValueOf(this.upperBound);

        return ub.subtract(lb);
    }

    protected static BigDecimal getBigDecValueOf(double value) {

        BigDecimal bdVal;
        if (value == Double.NEGATIVE_INFINITY) {
            bdVal = BigDecimal.valueOf(-Double.MAX_VALUE);
        }
        else if (value == Double.POSITIVE_INFINITY) {
            bdVal = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        else {
            bdVal = BigDecimal.valueOf(value);
        }

        return bdVal;
    }

    protected double getRangeMiddle() {
        if (this.lowerBound > this.upperBound) {
            throw new IllegalStateException("lowerBound greater than upperBound");
        }

        return (this.getRange().divide(BigDecimal.valueOf(2.)).add(getBigDecValueOf(this.lowerBound))).doubleValue();
    }

    /**
     * throws a lot of exceptions, that are described in the doc of the build()
     * function
     */
    protected void checkStateConsistency() {
        if (this.lowerBound >= this.upperBound) {
            throw new IllegalStateException("lowerBound >= upperBound");
        }

        if (this.startValue != null) {
            if (this.lowerBound > this.startValue) {
                throw new IllegalStateException("lowerBound > startValue");
            }

            if (this.startValue > this.upperBound) {
                throw new IllegalStateException("startValue > upperBound");
            }
        }

        if (this.deltaMin != null) {
            if (this.deltaMin < 0) {
                throw new IllegalStateException("deltaMin < 0");
            }

            if (this.deltaMax != null && this.deltaMin > this.deltaMax) {
                throw new IllegalStateException("deltaMin > deltaMax");
            }
        }

        if (this.avtFactory == null) {
            throw new IllegalStateException("avtFactory is not set");
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
            IGeometricDEFactory deltaEvolutionFactory = null;

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