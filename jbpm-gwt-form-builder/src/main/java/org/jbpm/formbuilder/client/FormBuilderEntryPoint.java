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
package org.jbpm.formbuilder.client;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.drools.guvnor.backend.VFSService;
import org.drools.guvnor.client.editors.fileexplorer.Root;
import org.drools.guvnor.client.mvp.PlaceRequest;
import org.drools.guvnor.vfs.FileSystem;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.Caller;
import org.jbpm.formbuilder.parent.client.FBBaseEntryPoint;

/**
 * Main entry point of the form builder application
 */
@ApplicationScoped
public class FormBuilderEntryPoint extends FBBaseEntryPoint {

    @Inject private Event<Root> event;

    @Inject private Caller<VFSService> vfsService;
    
    /**
     * Creates a FormBuilderView instance
     * Creates a FormBuilderController instance that adds all to the main view
     */
    @Override
    public void loadModule() {
        //start view and controller
//      RootPanel rootPanel = RootPanel.get("formBuilder");
//      FormBuilderView view = new FormBuilderView();
//      new FormBuilderController(rootPanel, view);
    }
    
    @AfterInitialization
    public void startApp() {
        setupGitRepos();
    }
    
    private void setupGitRepos() {
        //It is the responsibility of the "consuming" application to bootstrap the repositories it needs
        //This implementation is likely to change - so be prepared to ask porcelli or manstis for what is "current"
        final String gitURL = "https://github.com/guvnorngtestuser1/guvnorng-playground.git";
        final String userName = "guvnorngtestuser1";
        final String password = "test1234";
        final String fsURI = "jgit:///guvnorng-playground";

        final Map<String, Object> env = new HashMap<String, Object>();
        env.put("username", userName);
        env.put("password", password);
        env.put("giturl", gitURL);

        vfsService.call(new RemoteCallback<FileSystem>() {
            @Override
            public void callback(final FileSystem response) {
                event.fire(new Root(response.getRootDirectories().get(0),
                        new PlaceRequest("RepositoryEditor")
                                .addParameter("path:uri", fsURI)
                                .addParameter("path:name", "guvnorng-playground")));
            }
        }).newFileSystem(fsURI, env);
    }

}
