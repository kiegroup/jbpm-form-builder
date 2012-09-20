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
package org.jbpm.formbuilder.client.bus;

import com.google.gwt.event.shared.GwtEvent; 

/**
 * Notifies that the properties of the form have been populated
 */
public class SettingsPopulatedEvent extends GwtEvent<SettingsPopulatedHandler> {

    public static final Type<SettingsPopulatedHandler> TYPE = new Type<SettingsPopulatedHandler>();

    
    private final String storage;
    
    
    public SettingsPopulatedEvent(String storage) {
        
        this.storage = storage;
    }
    
    @Override
    public Type<SettingsPopulatedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SettingsPopulatedHandler handler) {
        handler.onEvent(this);
    }

   

    public String getStorage() {
        return storage;
    }

   
}
