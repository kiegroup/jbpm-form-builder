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
 * Asks for a form representation to the client components
 */
public class LoadSettingsEvent extends
        GwtEvent<LoadSettingsHandler> {

    public static final Type<LoadSettingsHandler> TYPE = new Type<LoadSettingsHandler>();

    
    public LoadSettingsEvent() {
    }
    
    @Override
    public Type<LoadSettingsHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LoadSettingsHandler handler) {
        handler.onEvent(this);
    }

}
