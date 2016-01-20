package fr.irit.smac.libs.tooling.messaging;

import java.text.MessageFormat;

/**
 * The Class MessageException.
 */
public class ExceptionMessage {

    /** The Constant UNKNOWN_GROUP_UNSUBSCRIBE. */
    public static final String UNKNOWN_GROUP_UNSUBSCRIBE        = "Trying to unsubscribe an agent from an unknown group : {0}";

    /** The Constant UNKNOWN_GROUP_SUBSCRIBE. */
    public static final String UNKNOWN_GROUP_SUBSCRIBE          = "Trying to subscribe an agent from an unknown group : {0}";

    /** The Constant UNKNOWN_RECEIVER_SEND. */
    public static final String UNKNOWN_RECEIVER_SEND            = "Trying to send a message to an unknown receiver : {0}";

    /** The Constant UNKNOWN_GROUP_SEND. */
    public static final String UNKNOWN_GROUP_SEND               = "Trying to send a message to an unknown group : {0}";

    /** The Constant GET_MESSAGES_DISPOSED_MSG_BOX. */
    public static final String GET_MESSAGES_DISPOSED_MSG_BOX    = "Trying to receive messages from a disposed message box.";

    /** The Constant AGENT_ALREADY_ASSOCIATED_MSG_BOX. */
    public static final String AGENT_ALREADY_ASSOCIATED_MSG_BOX = "The agent {0} is already associated with a msgBox.";

    /**
     * Instantiates a new exception message.
     */
    private ExceptionMessage() {
        
    }
    
    /**
     * Format message.
     *
     * @param messageString
     *            the message string
     * @param arg0
     *            the arg0
     * @return the string
     */
    public static String formatMessage(String messageString, Object arg0) {

        MessageFormat mf = new MessageFormat(messageString);
        Object[] args = new Object[1];
        args[0] = arg0;
        return mf.format(args);
    }

}
