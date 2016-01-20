/*
 * #%L
 * agent-scheduling
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
package fr.irit.smac.libs.tooling.scheduling.contrib.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.irit.smac.libs.tooling.scheduling.ISystemControlHandler;

/**
 * TODO: document.
 *
 * @author jorquera
 */
public class SystemControllerPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long           serialVersionUID = -8768099103951586071L;

    /** The system. */
    private final ISystemControlHandler system;

    /** The ms. */
    private int                         ms;

    /** The speed slider. */
    private final SchedulerSlider       speedSlider;
    
    /** The step button. */
    private final JButton               stepButton       = new JButton("step");

    /**
     * Instantiates a new system controller panel.
     *
     * @param system the system
     * @param ms the ms
     */
    public SystemControllerPanel(final ISystemControlHandler system, int ms) {
        super();

        this.system = system;
        this.ms = ms;

        this.setLayout(new FlowLayout());

        speedSlider = new SchedulerSlider();

        this.add(speedSlider);
        this.add(stepButton);

        this.stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (stepButton.isEnabled()) {
                    SystemControllerPanel.this.system.step();
                }
            }
        });
    }

    /**
     * Instantiates a new system controller panel.
     *
     * @param system the system
     */
    public SystemControllerPanel(final ISystemControlHandler system) {
        this(system, 500);
    }

    /**
     * The Class SchedulerSlider.
     */
    private class SchedulerSlider extends JSlider {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -5591439208368228848L;

        /**
         * Instantiates a new scheduler slider.
         */
        public SchedulerSlider() {

            super(SwingConstants.HORIZONTAL, 1, 3, 1);

            Dictionary<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
            labelTable.put(new Integer(1), new JLabel("Pause"));
            labelTable.put(new Integer(2), new JLabel("Slow (" + ms + "msec)"));
            labelTable.put(new Integer(3), new JLabel("Fast "));
            setLabelTable(labelTable);

            setMinorTickSpacing(1);
            setPaintTicks(true);
            setPaintLabels(true);

            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider) e.getSource();
                    if (!source.getValueIsAdjusting()) {
                        int state = source.getValue();
                        switch (state) {
                            case 1:
                                system.pause();
                                stepButton.setEnabled(true);
                                break;
                            case 2:
                                stepButton.setEnabled(false);
                                system.run(ms);
                                break;
                            case 3:
                                stepButton.setEnabled(false);
                                system.run(0);
                            default:
                                break;
                        }
                    }

                }
            });
        }
    }

}
