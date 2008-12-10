package hr.fer.zemris.vhdllab.platform.remoting.exception;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class AutoAddExceptionHandlerBeanPostProcessor implements
        BeanPostProcessor {

    @Autowired
    private ExceptionHandlerProvider provider;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof ExceptionHandler) {
            provider.addHandler((ExceptionHandler) bean);
        }
        return bean;
    }

}
