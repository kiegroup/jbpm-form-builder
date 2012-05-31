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
package org.jbpm.formbuilder.server.form;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.jbpm.formapi.shared.api.FormItemRepresentation;
import org.jbpm.formapi.shared.api.FormRepresentation;
import org.jbpm.formapi.shared.form.FormEncodingFactory;
import org.jbpm.formapi.shared.form.FormRepresentationDecoder;
import org.jbpm.formapi.shared.form.FormRepresentationEncoder;
import org.jbpm.formbuilder.server.file.FSFileService;
import org.jbpm.formbuilder.server.file.FileException;
import org.jbpm.formapi.shared.form.AbstractBaseFormDefinitionService;
import org.jbpm.formapi.shared.form.FormDefinitionService;
import org.jbpm.formapi.shared.form.FormServiceException;
import org.jbpm.formapi.shared.task.TaskRef;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author salaboy
 */
public class FSFormDefinitionService extends AbstractBaseFormDefinitionService implements FormDefinitionService, InitializingBean {

    private String baseUrl;
    private FSFileService fileService;
    private String fileSeparator = System.getProperty("file.separator");

    public List<FormRepresentation> getForms(String pkgName) throws FormServiceException {
        List<FormRepresentation> forms = new ArrayList<FormRepresentation>();
        List<String> loadFilesByType;
        try {
            loadFilesByType = fileService.loadFilesByType(pkgName, "formdef");
        } catch (FileException ex) {
            throw new FormServiceException(ex.getMessage(), ex);
        }
        for (String assetId : loadFilesByType) {
            if (isFormName(assetId)) {
                FormRepresentation form = getForm(pkgName, assetId.replace(".formdef", "").replace(baseUrl + fileSeparator + pkgName, ""));
                forms.add(form);
            }
        }
        return forms;
    }

    public Map<String, FormItemRepresentation> getFormItems(String pkgName) throws FormServiceException {
        try {
            Map<String, FormItemRepresentation> items = new HashMap<String, FormItemRepresentation>();
            List<String> loadFilesByType = fileService.loadFilesByType(pkgName, "json");
            for (String assetId : loadFilesByType) {
                if (isItemName(assetId)) {
                    FormItemRepresentation item = getFormItem(pkgName, assetId.replace(".json", ""));
                    items.put(assetId, item);
                }
            }
            return items;
        } catch (Exception ex) {
            throw new FormServiceException(ex.getMessage(), ex);
        }
    }

    public String saveForm(String pkgName, FormRepresentation form) throws FormServiceException {
        String finalUrl = baseUrl + fileSeparator + pkgName + fileSeparator + form.getName() + ".formdef";
        FormRepresentationEncoder encoder = FormEncodingFactory.getEncoder();
        try {
            String encoded = encoder.encode(form);
            File file = new File(finalUrl);
            FileUtils.writeStringToFile(file, encoded);
        } catch (Exception ex) {
            throw new FormServiceException(ex.getMessage(), ex);
        }

        return form.getName();
    }

    public String saveFormItem(String pkgName, String formItemName, FormItemRepresentation formItem) throws FormServiceException {
        StringBuilder builder = new StringBuilder();
        updateItemName(formItemName, builder);
        String finalUrl = baseUrl + fileSeparator + pkgName + fileSeparator + builder.toString() + ".json";

        FormRepresentationEncoder encoder = FormEncodingFactory.getEncoder();
        try {
            FileUtils.writeStringToFile(new File(finalUrl), encoder.encode(formItem));
        } catch (Exception ex) {
            throw new FormServiceException(ex.getMessage(), ex);
        }

        return formItemName;
    }

    public void deleteForm(String pkgName, String formId) throws FormServiceException {
        String deleteUrl = baseUrl + fileSeparator + pkgName + fileSeparator
                + formId + ".formdef";
        FileUtils.deleteQuietly(new File(deleteUrl));
    }

    public void deleteForm(String formUrl) throws FormServiceException {
        
        FileUtils.deleteQuietly(new File(formUrl));
    }

    public void deleteFormItem(String pkgName, String formItemId) throws FormServiceException {
        if (formItemId != null && !"".equals(formItemId)) {
            String deleteUrl = baseUrl + fileSeparator + pkgName + fileSeparator
                    + formItemId + ".json";
            FileUtils.deleteQuietly(new File(deleteUrl));
        }

    }
    public void deleteFormItem(String itemUrl) throws FormServiceException {
            FileUtils.deleteQuietly(new File(itemUrl));

    }

    public FormRepresentation getForm(String pkgName, String formId) throws FormServiceException {
        FormRepresentationDecoder decoder = FormEncodingFactory.getDecoder();
        File file = new File(baseUrl + fileSeparator + pkgName + fileSeparator + formId + ".formdef");
        String json;
        try {
            json = FileUtils.readFileToString(file);
            return decoder.decode(json);
        } catch (Exception ex) {
            throw new FormServiceException(ex.getMessage(), ex);
        }
    }

    public FormRepresentation getFormByUUID(String packageName, String uuid) throws FormServiceException {
        throw new UnsupportedOperationException("Not supported in FS implementation.");
    }

    public FormItemRepresentation getFormItem(String pkgName, String formItemId) throws FormServiceException {
        if (formItemId != null && !"".equals(formItemId)) {
            try {
                FormRepresentationDecoder decoder = FormEncodingFactory.getDecoder();
                String getUrl = baseUrl + fileSeparator + pkgName + "." + formItemId + ".json";
                String json = FileUtils.readFileToString(new File(getUrl));
                return decoder.decodeItem(json);
            } catch (Exception ex) {
                throw new FormServiceException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    public FormRepresentation getAssociatedForm(String pkgName, TaskRef task) throws FormServiceException {
        List<FormRepresentation> forms = getForms(pkgName);
        FormRepresentation retval = null;
        for (FormRepresentation form : forms) {
            if (form.getTaskId() != null && form.getTaskId().equals(task.getTaskId())) {
                retval = form;
                break;
            }
        }
        return retval;
    }

    public void saveTemplate(String packageName, String templateName, String content) throws FormServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void afterPropertiesSet() throws Exception {
        // do nothing
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public FSFileService getFileService() {
        return fileService;
    }

    public void setFileService(FSFileService fileService) {
        this.fileService = fileService;
    }
}
