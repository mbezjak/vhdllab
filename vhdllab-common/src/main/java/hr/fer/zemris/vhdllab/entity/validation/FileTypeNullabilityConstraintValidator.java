package hr.fer.zemris.vhdllab.entity.validation;

import hr.fer.zemris.vhdllab.entity.ClientLog;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileInfo;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.ProjectType;

import org.hibernate.validator.Validator;

public class FileTypeNullabilityConstraintValidator implements
        Validator<FileTypeNullabilityConstraint> {

    @Override
    public void initialize(FileTypeNullabilityConstraint parameters) {
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof File) {
            File file = (File) value;
            FileType fileType = file.getType();
            ProjectType projectType = file.getProject().getType();
            return projectType.equals(ProjectType.PREFERENCES) ? fileType == null
                    : fileType != null;
        } else if (value instanceof ClientLog) {
            return ((ClientLog) value).getType() == null;
        } else if (value instanceof FileInfo) {
            return true;
        }
        return false;
    }

}
