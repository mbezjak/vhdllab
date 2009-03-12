package hr.fer.zemris.vhdllab.entity.validation;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.entity.ProjectInfo;
import hr.fer.zemris.vhdllab.entity.ProjectType;

import org.hibernate.validator.Validator;

public class UserIdNullabilityConstraintValidator implements
        Validator<UserIdNullabilityConstraint> {

    @Override
    public void initialize(UserIdNullabilityConstraint parameters) {
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof Project) {
            Project project = (Project) value;
            ProjectType type = project.getType();
            String userId = project.getUserId();
            return type.equals(ProjectType.USER) ? userId != null
                    : userId == null;
        } else if (value instanceof ProjectInfo) {
            return true;
        }
        return false;
    }

}
