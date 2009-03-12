package hr.fer.zemris.vhdllab.service.filetype;

import hr.fer.zemris.vhdllab.entity.FileType;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class AbstractFileTypeFactory {

    @Autowired
    private ApplicationContext context;

    @SuppressWarnings("unchecked")
    protected <T> T getBean(FileType fileType, Class<T> beanInterfaceClass) {
        String type = fileType.toString().toLowerCase(Locale.ENGLISH);
        String interfaceName = beanInterfaceClass.getSimpleName();
        String beanName = type + interfaceName;
        return (T) context.getBean(beanName, beanInterfaceClass);
    }
}
