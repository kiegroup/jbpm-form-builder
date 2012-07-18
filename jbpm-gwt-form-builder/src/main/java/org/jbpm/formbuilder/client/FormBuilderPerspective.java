/*
 * Copyright 2012 JBoss by Red Hat.
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
package org.jbpm.formbuilder.client;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.drools.guvnor.client.annotations.DefaultPerspective;
import org.drools.guvnor.client.mvp.PlaceManager;
import org.drools.guvnor.client.mvp.PlaceRequest;
import org.drools.guvnor.client.workbench.WorkbenchPanel;
import org.drools.guvnor.client.workbench.perspectives.IPerspectiveProvider;
import org.drools.guvnor.client.workbench.widgets.panels.PanelManager;

/**
 * @author salaboy
 */
@ApplicationScoped
@DefaultPerspective
public class FormBuilderPerspective
    implements
    IPerspectiveProvider {

    @Inject
    PlaceManager                placeManager;

    private static final String NAME = "Default";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void buildWorkbench(final PanelManager panelManager,
                               final WorkbenchPanel root) {
        placeManager.goTo( new PlaceRequest( "FileExplorer" ) );
    }

}
