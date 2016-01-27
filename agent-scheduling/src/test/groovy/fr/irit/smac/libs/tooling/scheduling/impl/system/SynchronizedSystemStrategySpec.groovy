package fr.irit.smac.libs.tooling.scheduling.impl.system

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit;

import org.assertj.core.util.introspection.FieldUtils

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy

@Unroll
class SynchronizedSystemStrategySpec extends Specification {

    @Shared SynchronizedSystemStrategy synchronizedSystemStrategy

    private class MyAgent implements IAgentStrategy {

        private final int id
        private boolean done = false

        MyAgent(int id) {
            this.id = id
        }

        @Override
        public void nextStep() {
            this.done = true
        }
    }

    def setup() {

        Set<IAgentStrategy> agents = new HashSet<IAgentStrategy>()
        synchronizedSystemStrategy = new SynchronizedSystemStrategy(agents)
    }

    def 'constructor with agents and agentExecuter as parameters' () {

        given:
        Set<IAgentStrategy> agents = new HashSet<IAgentStrategy>()

        for (int i = 0; i < 3; i++) {
            agents.add(Mock(IAgentStrategy))
        }

        expect:
        new SynchronizedSystemStrategy(agents, Executors.newFixedThreadPool(2)) instanceof SynchronizedSystemStrategy
    }

    def 'constructor with agents parameter' () {

        given:
        Set<IAgentStrategy> agents = new HashSet<IAgentStrategy>()

        for (int i = 0; i < 3; i++) {
            agents.add(Mock(IAgentStrategy))
        }

        expect:
        new SynchronizedSystemStrategy(agents) instanceof SynchronizedSystemStrategy
    }

    def 'doStep' () {

        given:
        IAgentStrategy agent = new MyAgent(4)
        synchronizedSystemStrategy.addAgent(agent)

        when:
        synchronizedSystemStrategy.doStep()
        boolean done = true
        Set<IAgentStrategy> agents = synchronizedSystemStrategy.agentsCallables.keySet()
        for (MyAgent a : agents) {
            if (!a.done) {
                done = false
            }
        }

        then:
        done == true
    }

    def 'addAgent with an agent who is not in the system' () {

        given:
        IAgentStrategy agent = Mock(IAgentStrategy)

        when:
        synchronizedSystemStrategy.addAgent(agent)

        then:
        synchronizedSystemStrategy.agents.contains(agent) == true
    }

    def 'addAgent with an agent who already is in the system' () {

        given:
        IAgentStrategy agent = Mock(IAgentStrategy)
        synchronizedSystemStrategy.addAgent(agent)

        when:
        synchronizedSystemStrategy.addAgent(agent)

        then:
        synchronizedSystemStrategy.agents.contains(agent) == true
    }

    def 'removeAgent with an agent who is in the system' () {

        given:
        IAgentStrategy agent = Mock(IAgentStrategy)
        synchronizedSystemStrategy.addAgent(agent)

        when:
        synchronizedSystemStrategy.removeAgent(agent)

        then:
        synchronizedSystemStrategy.agents.contains(agent) == false
    }

    def 'removeAgent with an agent who is not in the system' () {

        given:
        IAgentStrategy agent = Mock(IAgentStrategy)

        when:
        synchronizedSystemStrategy.removeAgent(agent)

        then:
        synchronizedSystemStrategy.agents.contains(agent) == false
    }

    def 'removeAgents'() {

        given:
        Set<IAgentStrategy> agents = new HashSet<IAgentStrategy>()
        SynchronizedSystemStrategy synchronizedSystemStrategy2 = new SynchronizedSystemStrategy(agents)
        agents.add(Mock(IAgentStrategy))
        agents.add(Mock(IAgentStrategy))
        agents.add(Mock(IAgentStrategy))

        when:
        synchronizedSystemStrategy.removeAgents(agents)

        then:
        synchronizedSystemStrategy2.agents.size() == 0
    }

    def 'shutdown'() {

        when:
        synchronizedSystemStrategy.shutdown()

        then:
        synchronizedSystemStrategy.agentExecutor.awaitTermination(5L, TimeUnit.MINUTES)
        synchronizedSystemStrategy.systemExecutor.awaitTermination(5L, TimeUnit.MINUTES)
        synchronizedSystemStrategy.agentExecutor.terminated == true
        synchronizedSystemStrategy.systemExecutor.terminated == true
    }
}
