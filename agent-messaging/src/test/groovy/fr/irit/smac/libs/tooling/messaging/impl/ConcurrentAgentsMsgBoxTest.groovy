package fr.irit.smac.libs.tooling.messaging.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.messaging.AgentMessaging
import fr.irit.smac.libs.tooling.messaging.IMsgBox
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgContainer

@Unroll
class ConcurrentAgentsMsgBoxTest extends Specification{

	@Shared AgentMsgBox agent1MsgBox
	@Shared AgentMsgBox agent2MsgBox
	@Shared DefaultMsgService defaultMsgService
	@Shared String message1 = "hello", agent1 = "agent1"
	@Shared String message2 = "hello", agent2 = "agent2"
	@Shared int THREAD_JOIN_TIMEOUT = 800;

	def setup() {

		defaultMsgService = new DefaultMsgService()
		agent1MsgBox = defaultMsgService.getMsgBox(agent1)
		agent2MsgBox = defaultMsgService.getMsgBox(agent2)
	}


	def 'dispose should remove concurrently the agentMsgBox from its directory'() {

		when:
		agent1MsgBox.dispose()
		// To completely test dispose() the method must be call with two different thread
		Runnable run1 = new Runnable(){
			@Override
			public void run() {
				agent2MsgBox.dispose();
			}
		};

		Thread thr1 = new Thread(run1);
		thr1.start();
		thr1.join(THREAD_JOIN_TIMEOUT); //dispose() failure could be block, so a timeout should be specified
		
		then:
		!agent1MsgBox.directory.agentDirectory.contains(agent1MsgBox)
		!agent2MsgBox.directory.agentDirectory.contains(agent2MsgBox)
	}

}
