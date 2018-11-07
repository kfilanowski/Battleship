package common;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an abstract message source &ndash; a subject in the observer pattern.
 * This class should be extended by classes that want to deliver messages to some set of interested
 * parties.  These &quot;interested parties&quot; are decoupled from the implementation of this
 * class &ndash; those classes must implement the <code>MessageListener</code> interface.
 *
 * <b> The two concepts have been decoupled from one another:
 * Meaning that classes that are the source of message must implement the MessageSource interface 
 * classes that want to be notified must implment MessageListener. </b>
 *
 * @author Dr. William Kreahling
 * @version November 2017
 */
public abstract class MessageSource {
    /** Observers registered to receive notifications about this subject. */
    private List<MessageListener> messageListeners;

    /**
     * Constructs a new <code>MessageSource</code> with no registered observers.
     */
    public MessageSource() {
        this.messageListeners = new ArrayList<MessageListener>();
    }

    /**
     * Adds a new observer to this subject.
     *
     * @param  listener The new message listener; a new &quot;observer&quot;.
     */
    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    /**
     * Removes the specified observer from this subject.
     *
     * @param listener The listener to remove.
     */
    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    /**
     * Notifies <b>all</b> registered observers that this message source will generate no new
     * messages.
     */
    protected void closeMessageSource() {
        /* 
         * Here we need to iterate over a *copy* of our messageListeners list.  The reason is
         * because if the listener's 'sourceClosed' method removes that listener from this subject,
         * we'd get a ConcurrentModificationException if we were iterating over the original list.
         */
        for (MessageListener listener : new ArrayList<MessageListener>(messageListeners)) {
            try {
                listener.sourceClosed(this);
            } catch (RuntimeException ex) {
                /*
                 * We're doing this on a best-effort basis.  If something goes wrong, we don't want
                 * to stop.  Here, we simply dump the stack and continue.
                 */
                ex.printStackTrace();
            }
        }
        messageListeners.clear();
    }

    /**
     * Notifies <b>all</b> registered listeners that a new message has been received.
     *
     * @param message The message this subject received.
     */
    protected void notifyReceipt(String message) {
        for (MessageListener listener : new ArrayList<MessageListener>(messageListeners)) {
            /* 
             * We wrap this in a try/catch block so that just in case one of our observers screws
             * up, we don't want to stop notifying other observers.
             */
            try {
                listener.messageReceived(message, this);
            } catch (RuntimeException ex) {
                /* 
                 * We're doing this on a best-effort basis.  If something goes wrong, we don't want
                 * to stop.  Here, we simply dump the stack and continue.
                 */
                ex.printStackTrace();
            }
        }
    }
}
