package fr.irit.smac.libs.tooling.messaging

import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ExceptionMessageTest extends Specification{

    def 'constructor should be private - this test is just here for the coverage'() {

        when:
        Constructor constructor = ExceptionMessage.class.getDeclaredConstructor()

        then:
        Modifier.isPrivate(constructor.getModifiers()) == true

        and:
        constructor.setAccessible(true)
        constructor.newInstance()
    }
}
