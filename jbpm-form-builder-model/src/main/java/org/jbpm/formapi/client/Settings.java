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
package org.jbpm.formapi.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author salaboy
 */
@Entity
@XmlRootElement (name = "settings")
public class Settings implements Serializable {
    
    @Id
    @GeneratedValue()
    private Long id;
    @OneToMany(cascade= CascadeType.ALL, fetch= FetchType.EAGER)
    
    private List<SettingsEntry> entries = new ArrayList<SettingsEntry>();

    private String userId;
    
    public Settings() {
    }

    public Settings(String userId) {
        this.userId = userId;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SettingsEntry> getEntries() {
        return entries;
    }
    
    public SettingsEntry getEntry(String key) {
        for(SettingsEntry entry : entries){
            if(entry.getKey().equals(key)){
                return entry;
            }
        }
        return null;
    }

    public void setEntries(List<SettingsEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(SettingsEntry entry){
        this.entries.add(entry);
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Settings{" + "id=" + id + ", userId=" + userId + ", entries=" + entries +  '}';
    }

    
    
    
    
}
