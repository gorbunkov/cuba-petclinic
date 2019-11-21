package com.haulmont.sample.petclinic.web.processform.visit;

import com.haulmont.addon.bproc.web.processform.Outcome;
import com.haulmont.addon.bproc.web.processform.ProcessForm;
import com.haulmont.addon.bproc.web.processform.ProcessFormContext;
import com.haulmont.addon.bproc.web.processform.ProcessVariable;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.sample.petclinic.entity.visit.Visit;

import javax.inject.Inject;

@UiController("petclinic_VisitProcessForm")
@UiDescriptor("visit-process-form.xml")
@ProcessForm(
        outcomes = {
                @Outcome(id = VisitProcessForm.COMPLETED_OUTCOME),
                @Outcome(id = VisitProcessForm.PATIENT_DID_NOT_COME_OUTCOME)
        }
)
public class VisitProcessForm extends Screen {

    static final String COMPLETED_OUTCOME = "completed";
    static final String PATIENT_DID_NOT_COME_OUTCOME = "patientDidNotCome";

    @Inject
    @ProcessVariable
    private TextArea<String> ownersComment;

    @Inject
    @ProcessVariable
    private PickerField<Visit> visit;

    @Inject
    @ProcessVariable
    private TextArea<String> recommendations;

    @Inject
    private ProcessFormContext processFormContext;

    @Subscribe("visitCompletedBtn")
    public void onVisitCompletedBtnClick(Button.ClickEvent event) {
        processFormContext.taskCompletion()
                .withOutcome(COMPLETED_OUTCOME)
                .saveInjectedProcessVariables()
                .complete();
        closeWithDefaultAction();
    }

    @Subscribe("patientDidNotComeBtn")
    public void onPatientDidNotComeBtnClick(Button.ClickEvent event) {
        processFormContext.taskCompletion()
                .withOutcome(PATIENT_DID_NOT_COME_OUTCOME)
                .addProcessVariable("recommendations", recommendations.getValue())
                .complete();
        closeWithDefaultAction();
    }
}