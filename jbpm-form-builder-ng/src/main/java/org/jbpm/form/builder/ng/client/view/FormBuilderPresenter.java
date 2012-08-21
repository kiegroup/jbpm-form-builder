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


import org.jbpm.model.formapi.client.CommonGlobals;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.IsWidget;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.jbpm.form.builder.ng.client.command.DisposeDropController;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

@Dependent
@WorkbenchScreen(identifier = "Form Builder")
public class FormBuilderPresenter {

    @Inject
    private FormBuilderView view;
    
//    @Inject
//    private FormBuilderService service;
    
    public interface FormBuilderView
            extends
            IsWidget {
        
    }
    
    
   @PostConstruct
    public void init() {
        
       
        
//        PickupDragController dragController = new PickupDragController((AbsolutePanel)view, true);
//        dragController.registerDropController(new DisposeDropController((AbsolutePanel)view));
//        CommonGlobals.getInstance().registerDragController(dragController);

        
//        
//        bus.addHandler(RepresentationFactoryPopulatedEvent.TYPE, new RepresentationFactoryPopulatedHandler() {
//            @Override
//            public void onEvent(RepresentationFactoryPopulatedEvent event) {
//                try {
//                    service.getMenuItems();
//                    service.getMenuOptions();
//                } catch (FormBuilderException e) {
//                    //implementation never throws this
//                }
//                List<GwtEvent<?>> events = setDataPanel(rootPanel);
//                
//                //events are fired deferred since they might need that ui components are already attached
//                fireEvents(events);
//            }
//        });
//       
//        populateRepresentationFactory(service);
    }

    
    
   

    @WorkbenchPartTitle
    public String getTitle() {
        return "Form Builder";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return view;
    }
    
  
    
   

   
    
    
    
    
}
