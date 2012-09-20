package org.jbpm.formbuilder.server;


import org.jbpm.formapi.client.CommonGlobals;
import org.jbpm.formapi.client.Settings;
import org.jbpm.formbuilder.server.file.FileService;
import org.jbpm.formbuilder.server.settings.SettingsService;
import org.jbpm.formbuilder.shared.form.FormDefinitionService;
import org.jbpm.formbuilder.shared.menu.MenuService;
import org.jbpm.formbuilder.shared.task.TaskDefinitionService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class ServiceFactory implements BeanFactoryAware {

    private static final ServiceFactory INSTANCE = new ServiceFactory();
    
    public static ServiceFactory getInstance() {
        return INSTANCE;
    }
    
    private ServiceFactory() {
        
    }
    
    private BeanFactory beanFactory;
    
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    
    public FormDefinitionService getFormDefinitionService() {
        return (FormDefinitionService) getService("FormService");
    }
    
    public TaskDefinitionService getTaskDefinitionService() {
        return (TaskDefinitionService) getService("TaskService");
    }
    
    public FileService getFileService() {
        return (FileService) getService("FileService");
    }
    
    public MenuService getMenuService() {
        return (MenuService) getService("MenuService");
    }
    
    public SettingsService getSettingsService() {
        //Static Service Configured By Spring
        return (SettingsService) this.beanFactory.getBean("SettingsService");
    }
    
    private Object getService(String name) {
        Settings settings = CommonGlobals.getInstance().getSettings();
        String strategy = "guvnor";
        if(settings != null){
            strategy = settings.getEntry("storage").getValue();
        }
        System.out.println("Getting service using strategy ="+strategy);
        return this.beanFactory.getBean( strategy + name);
    }
}
