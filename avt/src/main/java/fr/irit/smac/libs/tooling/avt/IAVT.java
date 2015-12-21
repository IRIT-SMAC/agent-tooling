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

/**
 * An AVT is able to tune up (or adjust) a double value thanks to feedbacks.
 * 
 * <p>
 * Here is the basic usage of an AVT :
 * </p>
 * <ul>
 * <li>AVT current value is given by the method <code>getValue()</code></li>
 * <li>AVT value can be adjusted by the method
 * <code>adjustValue(feedback)</code> where the feedback informs whether the
 * value must be incremented of decremented</li>
 * </ul>
 * <p>
 * You should use an instance of the <code>AVTBuilder</code> to easily get a
 * well initialized AVT. For example :
 * </p>
 * <p>
 * <code>AVT avt = new AVTBuilder().lowerBound(0.).upperBound(100.).deltaMin(0.1).build()</code>
 * </p>
 * 
 * @see AVTBuilder
 * @author Sylvain Lemouzy
 */
public interface IAVT {

    /**
     * Adjusts the value to the direction of the given feedback.
     * <ul>
     * <li><code>Feedback.GREATER</code>: increments the current value</li>
     * <li><code>Feedback.LOWER<code>: decrements the current value</li>
     *  <li><code>Feedback.GOOD</code>: decreases the criticity of the AVT but
     * doesn't change the value. This feedbacks express that the current value
     * is good and should evolve more slowly at the next adjustment.</li>
     * </ul>
     * 
     * 
     * @param feedback
     */
    public void adjustValue(EFeedback feedback);

    /**
     * Current value determied by the AVT.
     * 
     * @return the current value determied by the AVT
     */
    public double getValue();

    /**
     * Returns the criticity of the AVT.
     * <p>
     * Criticity is a value that express the estimated inaccuracy of the current
     * value. The criticity is a value between 0 and 1 where :
     * </p>
     * <ul>
     * <li>0 stands for the best accuracy</li>
     * <li>1 stands for the worst accuracy</lI>
     * </ul>
     * 
     * @return the criticity of this AVT
     */
    public double getCriticity();

    /**
     * Returns an advanced interface to control the AVT.
     * <p>
     * Pay caution, some of the methods of this interface doesn't guarantee the
     * AVT state consistency. Generally, you should only uses this interface if
     * you have good reasons and you know what you are doing.
     * </p>
     * 
     * @return this AVT instance with an advanced interface
     */
    public IAdvancedAVT getAdvancedAVT();
}
