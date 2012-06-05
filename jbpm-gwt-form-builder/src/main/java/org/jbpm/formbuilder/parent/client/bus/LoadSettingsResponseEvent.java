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
package org.jbpm.formbuilder.parent.client.bus;


import com.google.gwt.event.shared.GwtEvent;
import org.jbpm.model.formapi.client.Settings;

/**
 * Tells whoever asked that the form representation of the current client edition has been loaded
 */
public class LoadSettingsResponseEvent extends
        GwtEvent<LoadSettingsResponseHandler> {

    public static final Type<LoadSettingsResponseHandler> TYPE = new Type<LoadSettingsResponseHandler>();
    private final Settings settings;
    public LoadSettingsResponseEvent(Settings settings) {
        super();
        this.settings = settings;
    }

    @Override
    public Type<LoadSettingsResponseHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LoadSettingsResponseHandler handler) { 
        handler.onEvent(this);
    }

    public Settings getSettings() {
        return settings;
    }

    
}
