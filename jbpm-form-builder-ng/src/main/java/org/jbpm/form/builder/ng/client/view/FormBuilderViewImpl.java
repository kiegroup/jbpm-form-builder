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
package org.jbpm.form.builder.ng.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.uberfire.client.mvp.PlaceManager;

/**
 * Main view. Uses UIBinder to define the correct position of components
 */
@Dependent
public class FormBuilderViewImpl extends Composite implements FormBuilderPresenter.FormBuilderView{

    @Inject
    private UiBinder<Widget, FormBuilderViewImpl> uiBinder;


    @Inject
    private PlaceManager placeManager;
    @Inject
    private FormBuilderPresenter presenter;
    
    @UiField SimplePanel optionsView;
    @UiField ScrollPanel menuView;
    @UiField ScrollPanel editionView;
    @UiField ScrollPanel layoutView;
    @UiField Panel toolBarView;

    
    
    @PostConstruct
    protected final void init() {
        
            
            
            int fullHeight = Window.getClientHeight();
            String height = "" + (fullHeight - 80) + "px";
            String smallerHeight = "" + (fullHeight - 105) + "px";
            //treeView.setHeight("100%");
            menuView.setHeight("100%");
            editionView.setHeight("100%");
            //ioAssociationView.setHeight("100%");
            layoutView.setHeight(smallerHeight);
       
    }

    
}
