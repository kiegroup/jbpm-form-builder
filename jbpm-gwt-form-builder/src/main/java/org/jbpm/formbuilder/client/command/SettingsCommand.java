/*
 * Copyright 2011 JBoss Inc 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formbuilder.client.command;

import org.jbpm.formapi.client.CommonGlobals;
import org.jbpm.formapi.client.FormBuilderException;
import org.jbpm.formapi.client.bus.ui.NotificationEvent;
import org.jbpm.formapi.client.bus.ui.NotificationEvent.Level;
import org.jbpm.formapi.shared.api.FormRepresentation;
import org.jbpm.formbuilder.client.FormBuilderGlobals;
import org.jbpm.formbuilder.client.FormBuilderService;
import org.jbpm.formbuilder.client.bus.GetFormRepresentationEvent;
import org.jbpm.formbuilder.client.bus.GetFormRepresentationResponseEvent;
import org.jbpm.formbuilder.client.bus.GetFormRepresentationResponseHandler;
import org.jbpm.formbuilder.client.menu.FormDataPopupPanel;
import org.jbpm.formbuilder.client.messages.I18NConstants;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtent.reflection.client.Reflectable;
import org.jbpm.formapi.client.Settings;
import org.jbpm.formbuilder.client.bus.*;
import org.jbpm.formbuilder.client.menu.SettingsPopupPanel;

/**
 * Handles the action of saving a form on the server
 */
@Reflectable
public class SettingsCommand implements BaseCommand {

    private static final String SAVE_TYPE = SettingsCommand.class.getName();
    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();
    private final EventBus bus = CommonGlobals.getInstance().getEventBus();
    private final FormBuilderService service = FormBuilderGlobals.getInstance().getService();

    public SettingsCommand() {
        super();
        bus.addHandler(LoadSettingsResponseEvent.TYPE, new LoadSettingsResponseHandler() {

            public void onEvent(LoadSettingsResponseEvent event) {
                System.out.println("Settings response = "+event.getSettings());
                showSettingsPanel(event.getUserId(), event.getSettings());
            }
        });
    }

    private void showSettingsPanel(String userId, Settings settings) {

        final SettingsPopupPanel panel = new SettingsPopupPanel(userId, settings);
        panel.setModal(true);
        panel.setPopupPosition(
                RootPanel.getBodyElement().getClientWidth() / 2 - 150,
                RootPanel.getBodyElement().getClientHeight() / 2 - 150);
        panel.show();
        panel.addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                applySettings(panel);
            }
        });

    }

    public void applySettings(SettingsPopupPanel panel) {
        System.out.println(">>>> Applying Settings");
    }

    @Override
    public void execute() {
        bus.fireEvent(new LoadSettingsEvent());
    }
    private MenuItem item = null;

    @Override
    public void setItem(MenuItem item) {
        this.item = item;
        item.setEnabled(true);
    }

    @Override
    public void setEmbeded(String profile) {
        if (item != null) {
            //When embedded on guvnor, saving is done through guvnor
            if (profile != null && "guvnor".equals(profile)) {
                item.getParentMenu().removeItem(item);
            }
        }
    }
}
