package org.jbpm.formbuilder.parent.client.bus.ui;

import com.google.gwt.event.shared.EventHandler;

public interface UserIsLoggedOutHandler extends EventHandler {

    void onEvent(UserIsLoggedOutEvent event);
}
