package hr.fer.zemris.vhdllab.platform.util;

import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.validation.support.CompositeRichValidator;
import org.springframework.binding.validation.support.HibernateRulesValidator;
import org.springframework.binding.validation.support.RulesValidator;
import org.springframework.richclient.form.FormModelHelper;

public abstract class FormModelUtils {

    public static ValidatingFormModel createFormModel(Object formObject) {
        ValidatingFormModel formModel = FormModelHelper
                .createFormModel(formObject);

        RulesValidator rulesSourceValidator = new RulesValidator(formModel);
        HibernateRulesValidator hibernateValidator = new HibernateRulesValidator(
                formModel, formObject.getClass());
        CompositeRichValidator compositeValidator = new CompositeRichValidator(
                hibernateValidator, rulesSourceValidator);

        formModel.setValidator(compositeValidator);
        return formModel;
    }

}
