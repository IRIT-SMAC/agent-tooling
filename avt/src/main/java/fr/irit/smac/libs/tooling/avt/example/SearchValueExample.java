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
package fr.irit.smac.libs.tooling.avt.example;

import fr.irit.smac.libs.tooling.avt.IAVT;
import fr.irit.smac.libs.tooling.avt.AVTBuilder;
import fr.irit.smac.libs.tooling.avt.EFeedback;

public class SearchValueExample {

    private SearchValueExample() {

    }

    public static void main(String[] args) {
        double searchedValue = 9.208;
        double minValue = 0;
        double maxValue = 10;
        double precision = .001;
        int nbCycles = 0;

        IAVT avt = new AVTBuilder().lowerBound(minValue).upperBound(maxValue).deltaMin(precision).deltaDecreaseDelay(1)
            .deltaIncreaseDelay(1).startValue(1.).build();

        while (Math.abs(avt.getValue() - searchedValue) > precision) {
            System.out.println("============================");
            System.out.println("Cycle number " + ++nbCycles);
            System.out.println("DeltaValue = " + avt.getAdvancedAVT().getDeltaManager().getDelta());
            System.out.println("CurrentValue = " + avt.getValue());
            if (avt.getValue() < searchedValue) {
                avt.adjustValue(EFeedback.GREATER);
                System.out.println("-> Increasing value");

            }
            else if (avt.getValue() > searchedValue) {
                avt.adjustValue(EFeedback.LOWER);
                System.out.println("-> Decreasing value");
            }
            else {
                avt.adjustValue(EFeedback.GOOD);
                System.out.println("-> Good value");
            }
            System.out.println("DeltaValue = " + avt.getAdvancedAVT().getDeltaManager().getDelta());
            System.out.println("CurrentValue = " + avt.getValue());
        }
    }
}
