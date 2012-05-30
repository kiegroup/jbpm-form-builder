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
package org.jbpm.formbuilder.server.settings;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.jbpm.formapi.client.Settings;

/**
 *
 * @author salaboy
 */
public class JTADBSettingsService implements SettingsService {

    
    private EntityManagerFactory emf;
    public JTADBSettingsService() {
        if (emf == null) {
            try {
                InitialContext ctx = new InitialContext();
                // For JBOSS 7.x
                // see: https://docs.jboss.org/author/display/AS71/JPA+Reference+Guide#JPAReferenceGuide-BindingEntityManagerFactorytoJNDI
                emf = (EntityManagerFactory) ctx.lookup("java:jboss/myEntityManagerFactory");
            } catch (NamingException ex) {
                Logger.getLogger(JTADBSettingsService.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }


        }
    }

    public Settings getSettingsByUserId(String userName) {
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("GetSettingsByUser");

        query.setParameter("userId", userName);
        Settings settings = null;
        UserTransaction ut;
        try {
            settings = (Settings) query.getSingleResult();
        } catch (NoResultException ex) {
            try {
                ut = (UserTransaction) new InitialContext().lookup( "java:comp/UserTransaction" );
                ut.begin();
                settings = new Settings(userName);
                em.persist(settings);
                ut.commit();
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
        }
        em.close();
        return settings; 
    }

    public void applySettings(Settings settings, String userName) {
        EntityManager em = emf.createEntityManager();
        try {
            
            UserTransaction ut;
            ut = (UserTransaction) new InitialContext().lookup( "java:comp/UserTransaction" );
            ut.begin();
            em.merge(settings);
            ut.commit();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        em.close();
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    
    
}
