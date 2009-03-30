package hr.fer.zemris.vhdllab.platform.ui.wizard.source;

import hr.fer.zemris.vhdllab.service.ci.Port;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.springframework.binding.validation.support.HibernateRulesMessageInterpolator;
import org.springframework.richclient.application.support.ApplicationServicesAccessor;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.table.BeanTableModel;
import org.springframework.richclient.wizard.WizardPage;

public class PortValidationReporter extends ApplicationServicesAccessor
        implements TableModelListener {

    private final BeanTableModel model;
    private final WizardPage page;
    private final ClassValidator<Port> validator;

    public PortValidationReporter(BeanTableModel model, WizardPage page) {
        Validate.notNull(model, "Bean table model can't be null");
        Validate.notNull(page, "Wizard page can't be null");
        this.model = model;
        this.page = page;
        this.validator = new ClassValidator<Port>(Port.class,
                new HibernateRulesMessageInterpolator());
        model.addTableModelListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void tableChanged(TableModelEvent e) {
        reportValidationError(null, -1);
        String duplicate = checkForDuplicates(model.getRows());
        if (duplicate != null) {
            String message = getMessage("duplicate.port.name",
                    new String[] { duplicate });
            page.setMessage(new DefaultMessage(message, Severity.ERROR));
            page.setEnabled(false);
        } else {
            for (int i = 0; i < model.getRowCount(); i++) {
                Port port = (Port) model.getRow(i);
                InvalidValue[] invalidValues = validator.getInvalidValues(port);
                if (invalidValues.length > 0) {
                    reportValidationError(invalidValues[0], i);
                    break;
                }
            }
        }
    }

    private String checkForDuplicates(List<Port> ports) {
        Set<String> uniquePortNames = new HashSet<String>(ports.size());
        String duplicate = null;
        for (Port port : ports) {
            if (StringUtils.isBlank(port.getName())) {
                continue;
            }
            String name = port.getName().toLowerCase();
            if (uniquePortNames.contains(name)) {
                duplicate = port.getName();
                break;
            }
            uniquePortNames.add(name);
        }
        return duplicate;
    }

    private void reportValidationError(InvalidValue invalidValue, int row) {
        Message message = null;
        if (invalidValue != null) {
            StringBuilder sb = new StringBuilder(50);
            String simpleName = invalidValue.getBean().getClass()
                    .getSimpleName().toLowerCase();
            String propertyName = invalidValue.getPropertyName();
            String columnNameKey = simpleName + "." + propertyName;
            sb.append(getMessage("row")).append(" ").append(row + 1);
            sb.append(": ").append(getMessage(columnNameKey)).append(" ");
            sb.append(invalidValue.getMessage());

            message = new DefaultMessage(sb.toString(), Severity.ERROR);
        }
        page.setMessage(message);
        page.setEnabled(message == null);
    }
}
