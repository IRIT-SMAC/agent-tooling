package fr.irit.smac.libs.tooling.scheduling.contrib.twosteps

import static org.junit.Assert.*

import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

import org.codehaus.groovy.transform.tailrec.VariableReplacedListener.*

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class TwoStepsSystemStrategyTest extends Specification {

    @Shared TwoStepsSystemStrategy twoStepsSystemStrategy

    private class Hook implements Runnable {

        private final String message
        public boolean done = false

        public Hook(String message) {
            super()
            this.message = message
        }

        @Override
        public void run() {
            //            System.out.println("\n-- Hook: " + message)
            done = true
        }

        @Override
        public String toString() {
            return "Hook[" + message + "]"
        }
    }

    private static class AgentMock implements ITwoStepsAgent {

        private int step = 0
        private int perceive = -1
        private int decideAndAct = -1

        @Override
        public void perceive() {
            //            System.out.println("Agent " + this + " is perceiving " + step)
            perceive = step
            step++
        }

        @Override
        public void decideAndAct() {
            //            System.out.println("Agent " + this + " is acting " + step++)
            decideAndAct = step
            step++
        }
    }

    def setup() {

        Set<ITwoStepsAgent> agents = new HashSet<ITwoStepsAgent>()
        twoStepsSystemStrategy = new TwoStepsSystemStrategy(agents)
    }

    def 'constructor with agents as parameter'() {

        given:
        Set<ITwoStepsAgent> agents = new HashSet<ITwoStepsAgent>()
        for (int i = 0; i < 3; i++) {
            agents.add(Mock(ITwoStepsAgent))
        }

        expect:
        new TwoStepsSystemStrategy(agents) instanceof TwoStepsSystemStrategy
    }

    def 'constructor with agents and agentExecutor as parameters' () {

        given:
        Set<ITwoStepsAgent> agents = new HashSet<ITwoStepsAgent>()
        for (int i = 0; i < 3; i++) {
            agents.add(Mock(ITwoStepsAgent))
        }

        expect:
        new TwoStepsSystemStrategy(agents, Executors.newFixedThreadPool(2)) instanceof TwoStepsSystemStrategy
    }

    def 'addPendingAgents'() {

        given:
        Set<ITwoStepsAgent> agents = new HashSet<ITwoStepsAgent>()
        agents.add(Mock(ITwoStepsAgent))
        agents.add(Mock(ITwoStepsAgent))
        TwoStepsSystemStrategy twoStepsSystemStrategy2 = new TwoStepsSystemStrategy(agents)

        when:
        twoStepsSystemStrategy2.addPendingAgents()

        then:
        twoStepsSystemStrategy2.internalSystemStrategy.agents.size() == 2
    }

    def 'removePendingAgents'() {

        given:
        Set<ITwoStepsAgent> agents = new HashSet<ITwoStepsAgent>()
        ITwoStepsAgent agent1 = Mock(ITwoStepsAgent)
        ITwoStepsAgent agent2 = Mock(ITwoStepsAgent)
        agents.add(agent1)
        agents.add(agent2)
        TwoStepsSystemStrategy twoStepsSystemStrategy2 = new TwoStepsSystemStrategy(agents)
        twoStepsSystemStrategy2.removeAgent(agent1)
        twoStepsSystemStrategy2.removeAgent(agent2)
        twoStepsSystemStrategy2.addPendingAgents()

        when:
        twoStepsSystemStrategy2.removePendingAgents()

        then:
        twoStepsSystemStrategy2.internalSystemStrategy.agents.size() == 0
    }

    def 'postStep with postStepHook'() {

        given:
        Hook hook = new Hook("hook")
        Hook hook2 = new Hook("hook2")
        twoStepsSystemStrategy.addPostStepHook(hook)
        twoStepsSystemStrategy.addPostStepHook(hook2)

        when:
        twoStepsSystemStrategy.postStep()

        then:
        hook.done == true
        hook2.done == true
    }

    def 'preStep with preStepHook'() {

        given:
        Hook hook = new Hook("hook")
        Hook hook2 = new Hook("hook2")
        twoStepsSystemStrategy.addPreStepHook(hook)
        twoStepsSystemStrategy.addPreStepHook(hook2)

        when:
        twoStepsSystemStrategy.preStep()

        then:
        hook.done == true
        hook2.done == true
    }

    def 'getExecutorService should return the executorService'() {

        expect:
        twoStepsSystemStrategy.getExecutorService() == twoStepsSystemStrategy.executorService
    }

    def 'setExecutorService should set the executorService'() {

        given:
        Executor executor = Executors.newFixedThreadPool(2)

        when:
        twoStepsSystemStrategy.setExecutorService(executor)

        then:
        twoStepsSystemStrategy.executorService == executor
    }

    def 'addAgent with an agent who is in the system'() {

        given:
        ITwoStepsAgent agent = Mock(ITwoStepsAgent)

        when:
        twoStepsSystemStrategy.addAgent(agent)

        then:
        twoStepsSystemStrategy.pendingAddedAgents.contains(agent) == true
    }

    def 'addAgent with an agent who is not in the system'() {

        given:
        ITwoStepsAgent agent = Mock(ITwoStepsAgent)
        twoStepsSystemStrategy.addAgent(agent)

        when:
        twoStepsSystemStrategy.addAgent(agent)

        then:
        twoStepsSystemStrategy.pendingAddedAgents.contains(agent) == true
    }

    def 'removeAgent with an agent who is in the system'() {

        given:
        ITwoStepsAgent agent = Mock(ITwoStepsAgent)
        twoStepsSystemStrategy.addAgent(agent)

        when:
        twoStepsSystemStrategy.removeAgent(agent)

        then:
        twoStepsSystemStrategy.pendingRemovedAgents.contains(agent) == true
    }

    def 'removeAgent with an agent who is not in the system'() {

        given:
        ITwoStepsAgent agent = Mock(ITwoStepsAgent)

        when:
        twoStepsSystemStrategy.removeAgent(agent)

        then:
        twoStepsSystemStrategy.pendingRemovedAgents.contains(agent) == false
    }

    def 'shutdown'() {

        when:
        twoStepsSystemStrategy.shutdown()

        then:
        twoStepsSystemStrategy.agentExecutor.awaitTermination(5L, TimeUnit.MINUTES)
        twoStepsSystemStrategy.systemExecutor.awaitTermination(5L, TimeUnit.MINUTES)
        twoStepsSystemStrategy.agentExecutor.terminated == true
        twoStepsSystemStrategy.systemExecutor.terminated == true
    }

    def 'doStep have to run the steps of each agent. First, the step perceive is run. Then, the step decideAndAct is run.'() {

        given:
        Set<AgentMock> agents = new HashSet<AgentMock>()
        AgentMock agent = new AgentMock()
        agents.add(agent)
        TwoStepsSystemStrategy twoStepsSystemStrategy2 = new TwoStepsSystemStrategy(agents)

        when:
        twoStepsSystemStrategy2.doStep()

        then:
        boolean ok = true
        for (Iterator iterator = twoStepsSystemStrategy2.agents.iterator(); iterator.hasNext();) {
            AgentMock a = (AgentMock) iterator.next()
            ok = (a.perceive == a.step - 2) && (a.decideAndAct == a.step - 1)
        }
        ok == true
    }
}
