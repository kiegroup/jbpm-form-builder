package org.jbpm.formbuilder.client;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.model.formapi.client.CommonGlobals;
import org.jbpm.model.formapi.client.FormBuilderException;
import org.jbpm.model.formapi.client.bus.ui.NotificationEvent;
import org.jbpm.model.formapi.client.bus.ui.NotificationEvent.Level;
import org.jbpm.model.formbuilder.client.messages.I18NConstants;

import com.google.gwt.event.shared.EventBus;

public class RoleUtils {

    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();
    private final EventBus bus = CommonGlobals.getInstance().getEventBus();
    private final List<String> roles = new ArrayList<String>();
    
    private static final RoleUtils INSTANCE = new RoleUtils();
    
    public static RoleUtils getInstance() {
        return INSTANCE;
    }
    
    private RoleUtils() {
        reload();
    }

    public void reload() {
        try {
            roles.clear();
            FormBuilderService server = FormBuilderGlobals.getInstance().getService();
            if (server != null) {
                server.getCurrentRoles(new FormBuilderService.RolesResponseHandler() {
                    @Override
                    public void onResponse(List<String> responseRoles) {
                        roles.addAll(responseRoles);
                    }
                });
            }
        } catch (FormBuilderException e) {
            bus.fireEvent(new NotificationEvent(Level.ERROR, i18n.RolesNotRead(), e));
        }
    }
    
    public boolean hasDesignPrivileges() {
        return roles.contains("admin") || roles.contains("webdesigner");
    }
    
    public boolean hasOnlyUserPrivileges() {
        return roles.contains("functionalanalyst") && !hasDesignPrivileges();
    }

    

}
