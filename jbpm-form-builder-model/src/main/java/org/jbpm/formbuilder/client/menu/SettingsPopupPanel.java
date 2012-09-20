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
package org.jbpm.formbuilder.client.menu;

import org.jbpm.formbuilder.client.bus.FormDataPopulatedEvent;
import org.jbpm.formbuilder.client.messages.I18NConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.jbpm.formapi.client.CommonGlobals;
import org.jbpm.formapi.client.Settings;
import org.jbpm.formapi.client.SettingsEntry;
import org.jbpm.formbuilder.client.bus.ApplySettingsEvent;
import org.jbpm.formbuilder.client.bus.SettingsPopulatedEvent;

/**
 * form data popup panel. UI to alter form properties (enctype, action, name,
 * etc)
 */
public class SettingsPopupPanel extends PopupPanel {

    private final I18NConstants i18n = CommonGlobals.getInstance().getI18n();
    private final EventBus bus = CommonGlobals.getInstance().getEventBus();
    private final ListBox storage = new ListBox(false);

  

    public SettingsPopupPanel(final String userId, final Settings settings) {
        super(true);
        setStyleName("commandPopupPanel");
        VerticalPanel vPanel = new VerticalPanel();
        vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        Grid grid = new Grid(2, 2);
        
        storage.addItem("guvnor");
        storage.addItem("fs");
        
        SettingsEntry entry = settings.getEntry("storage");
        
        
        if(entry == null || "guvnor".equals(entry.getValue())){
            storage.setSelectedIndex(0);
        }if(entry != null && "fs".equals(entry.getValue())){
            storage.setSelectedIndex(1);
        }


        grid.setWidget(0, 0, new Label(i18n.SettingsLabel()));

        grid.setWidget(1, 0, new Label(i18n.StorageType()));
        grid.setWidget(1, 1, storage);


        vPanel.add(grid);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(new Button(i18n.ConfirmButton(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String storageValue = storage.getItemText(storage.getSelectedIndex());
                System.out.println("Setting storage strategy to: "+storageValue);
                if(settings.getEntry("storage") == null){
                    settings.addEntry(new SettingsEntry("storage", storageValue));
                }else{
                    settings.getEntry("storage").setValue(storageValue);
                }
                bus.fireEvent(new ApplySettingsEvent(userId, settings));
                hide();
            }
        }));
        buttonPanel.add(new Button(i18n.CancelButton(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                System.out.println("Cancel was clicked!!!!!");
                hide();
            }
        }));
        vPanel.add(buttonPanel);
        vPanel.setStyleName("commandContent");
        setWidget(vPanel);
    }

    public void setStorage(String storage) {
        for (int index = 0; index < this.storage.getItemCount(); index++) {
            if (this.storage.getValue(index).equals(storage)) {
                this.storage.setSelectedIndex(index);
                break;
            }

        }
    }

    public String getStorage() {
        return storage.getValue(storage.getSelectedIndex());
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        int left = getPopupLeft();
        int top = getPopupTop();
        int width = getOffsetWidth();
        int height = getOffsetHeight();

        boolean changed = false;

        if (left + width > Window.getClientWidth()) {
            left -= width;
            changed = true;
        }
        if (top + height > Window.getClientHeight()) {
            top -= height;
            changed = true;
        }
        if (changed) {
            setPopupPosition(left, top);
        }
    }
}
