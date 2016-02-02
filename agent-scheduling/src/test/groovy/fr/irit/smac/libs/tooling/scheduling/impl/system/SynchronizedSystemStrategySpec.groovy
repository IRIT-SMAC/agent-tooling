package fr.irit.smac.libs.tooling.scheduling.impl.system

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy

@Unroll
class SynchronizedSystemStrategySpec extends Specification {

    @Shared SynchronizedSystemStrategy synchronizedSystemStrategy

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

    private static class AgentMock2 implements IAgentStrategy {

        @Override
        public void nextStep() {
            throw new ExecutionException()
        }
    }

    private static class AgentMock3 implements IAgentStrategy {

        @Override
        public void nextStep() {
            throw new InterruptedException()
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

    def 'doStep have to run the step of each agent' () {

        given:
        IAgentStrategy agent = new AgentMock(4)
        synchronizedSystemStrategy.addAgent(agent)

        when:
        synchronizedSystemStrategy.doStep()
        boolean done = true
        Set<IAgentStrategy> agents = synchronizedSystemStrategy.agentsCallables.keySet()
        for (AgentMock a : agents) {
            if (!a.done) {
                done = false
            }
        }

        then:
        done == true
    }

//    private SynchronizedSystemStrategy getSystemWithMockLogger(Logger logger, Set<IAgentStrategy> agents) {
//
//        SynchronizedSystemStrategy synchronizedSystemStrategy = new SynchronizedSystemStrategy(agents)
//        Field field = SynchronizedSystemStrategy.class.getDeclaredField("LOGGER")
//        field.setAccessible(true)
//        Field modifiersField = Field.class.getDeclaredField("modifiers")
//        modifiersField.setAccessible(true)
//        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL)
//        field.set(synchronizedSystemStrategy, logger)
//
//        return synchronizedSystemStrategy
//    }
//
//    def 'doStep should catch any ExecutionException'() {
//
//        given:
//        Set<AgentMock2> agents = new HashSet<AgentMock2>()
//        AgentMock2 agent = new AgentMock2()
//        agents.add(agent)
//        Logger logger = Mock(Logger)
//
//        SynchronizedSystemStrategy synchronizedSystemStrategy = getSystemWithMockLogger(logger, agents)
//
//        when:
//        synchronizedSystemStrategy.doStep()
//
//        then:
//        1*logger.log(_,'java.util.concurrent.ExecutionException',_)
//    }
//
//    def 'doStep should catch any InterruptedException'() {
//
//        given:
//        Set<AgentMock3> agents = new HashSet<AgentMock3>()
//        AgentMock3 agent = new AgentMock3()
//        agents.add(agent)
//        Logger logger = Mock(Logger)
//        SynchronizedSystemStrategy synchronizedSystemStrategy = getSystemWithMockLogger(logger, agents)
//
//        when:
//        synchronizedSystemStrategy.doStep()
//        Thread.sleep(2000)
//
//        then:
//        1*logger.log(_,'java.lang.InterruptedException',_)
//    }

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
