package com.github.pentadrago.fileverifier;

import java.util.EventListener;

/**
 * The MessageListener is used to inform output (gui) classes of new messages.
 *
 * @author Stefan Kloe
 */
public interface MessageListener extends EventListener {

    /**
     * is called when a new message arrives
     *
     * @param message new message
     */
    public void receive(String message);

}
