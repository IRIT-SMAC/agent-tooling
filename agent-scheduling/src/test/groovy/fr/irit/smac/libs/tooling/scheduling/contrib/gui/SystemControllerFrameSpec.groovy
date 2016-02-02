package fr.irit.smac.libs.tooling.scheduling.contrib.gui

import static org.junit.Assert.*

import java.awt.WaitDispatchSupport
import java.util.concurrent.TimeUnit

import org.assertj.swing.edt.GuiActionRunner
import org.assertj.swing.edt.GuiQuery
import org.assertj.swing.fixture.FrameFixture
import org.assertj.swing.fixture.JButtonFixture
import org.assertj.swing.fixture.JSliderFixture
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase
import org.junit.Test

import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy
import fr.irit.smac.libs.tooling.scheduling.impl.system.SynchronizedSystemStrategy

public class SystemControllerFrameSpec extends AssertJSwingJUnitTestCase{

    private FrameFixture window
    private SystemControllerPanel panel
    private JSliderFixture sliderFixture
    private JButtonFixture stepButtonFixture

    private class AgentMock implements IAgentStrategy {

        private final int id
        private boolean done = false

        AgentMock(int id) {
            this.id = id
        }

        @Override
        public void nextStep() {
            this.done = true
        }
    }

    @Override
    protected void onSetUp() {

        Set<IAgentStrategy> agents = new HashSet<AgentMock>()
        agents.add(new AgentMock(1))

        SystemControllerFrame frame = GuiActionRunner.execute(new GuiQuery<SystemControllerFrame>() {
                            protected SystemControllerFrame executeInEDT() {
                                return new SystemControllerFrame(new SynchronizedSystemStrategy(agents))
                            }
                        })
        panel = frame.getContentPane().getComponent(0)
        sliderFixture = new JSliderFixture(robot(), panel.speedSlider)
        stepButtonFixture = new JButtonFixture(robot(), panel.stepButton)
    }

    @Test
    public void testButtonAndSlider() {

        stepButtonFixture.click()
        Thread.sleep(5000)
        Set<IAgentStrategy> agents = panel.system.agents
        AgentMock agent = agents.iterator().next()
        assertTrue(agent.done)

        sliderFixture.slideTo(2)
        assertFalse(panel.stepButton.enabled)

        sliderFixture.slideTo(1)
        assertTrue(panel.stepButton.enabled)

        sliderFixture.slideTo(3)
        assertFalse(panel.stepButton.enabled)

        // To test the case where source.getValueIsAjusting is false
        sliderFixture.click()
    }
}
