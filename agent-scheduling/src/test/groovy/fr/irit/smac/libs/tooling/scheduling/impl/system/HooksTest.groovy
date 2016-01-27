package fr.irit.smac.libs.tooling.scheduling.impl.system

import java.util.concurrent.Executors

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy

@Unroll
class HooksTest extends Specification {

    @Shared SynchronizedSystemStrategy synchronizedSystemStrategy

    def setup() {
        Set<IAgentStrategy> agents = new HashSet<IAgentStrategy>()

        for (int i = 0; i < 3; i++) {
            agents.add(Mock(IAgentStrategy))
        }

        synchronizedSystemStrategy = new SynchronizedSystemStrategy(
                        agents, Executors.newFixedThreadPool(2))
    }

    def 'addPreStepHook'() {

        when:
        synchronizedSystemStrategy.addPreStepHook(Mock(Runnable))

        then:
        synchronizedSystemStrategy.preStepHooks.size() == 1
    }

    def 'addPostStepHook'() {

        when:
        synchronizedSystemStrategy.addPostStepHook(Mock(Runnable))

        then:
        synchronizedSystemStrategy.postStepHooks.size() == 1
    }

    def 'removePreStepHook with an existing hook'() {

        given:
        Runnable hook = Mock(Runnable)
        synchronizedSystemStrategy.addPreStepHook(hook)

        when:
        synchronizedSystemStrategy.removePreStepHook(hook)

        then:
        synchronizedSystemStrategy.preStepHooks.size() == 0
    }

    def 'removePreStepHook without an existing hook'() {

        given:
        Runnable hook = Mock(Runnable)

        when:
        synchronizedSystemStrategy.removePreStepHook(hook)

        then:
        synchronizedSystemStrategy.preStepHooks.size() == 0
    }

    def 'removePostStepHook with an existing hook'() {

        given:
        Runnable hook = Mock(Runnable)
        synchronizedSystemStrategy.addPostStepHook(hook)

        when:
        synchronizedSystemStrategy.removePostStepHook(hook)

        then:
        synchronizedSystemStrategy.postStepHooks.size() == 0
    }

    def 'removePostStepHook without an existing hook'() {

        given:
        Runnable hook = Mock(Runnable)

        when:
        synchronizedSystemStrategy.removePostStepHook(hook)

        then:
        synchronizedSystemStrategy.postStepHooks.size() == 0
    }

    def 'getPreStepHooks'() {

        when:
        Set <Runnable> preStepHooks = synchronizedSystemStrategy.getPreStepHooks()

        then:
        preStepHooks != null
    }

    def 'getPostStepHooks'() {

        when:
        Set<Runnable> postStepHooks = synchronizedSystemStrategy.getPostStepHooks()

        then:
        postStepHooks != null
    }
}
