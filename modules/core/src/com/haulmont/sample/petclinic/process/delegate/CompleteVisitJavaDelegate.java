package com.haulmont.sample.petclinic.process.delegate;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompleteVisitJavaDelegate implements JavaDelegate {

    private Expression emailTemplateName;

    private static final Logger log = LoggerFactory.getLogger(CompleteVisitJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        Visit visit = (Visit) execution.getVariable("visit");
        String recommendations = (String) execution.getVariable("recommendations");

        DataManager dataManager = AppBeans.get(DataManager.class);
        visit = dataManager.reload(visit, "visit-full");
        visit.setRecommendations(recommendations);
        visit.setStatus("Completed");
        dataManager.commit(visit);

        String ownersEmail = visit.getPet().getOwner().getEmail();
        String emailTemplateNameValue = (String) emailTemplateName.getValue(execution);
        sendEmail(ownersEmail, emailTemplateNameValue);
    }

    private void sendEmail(String address, String templateName) {
        log.info("Let's imagine that we sent an email to {} using the template {}", address, templateName);
    }
}
