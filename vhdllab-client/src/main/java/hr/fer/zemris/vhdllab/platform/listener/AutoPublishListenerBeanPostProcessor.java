package hr.fer.zemris.vhdllab.platform.listener;

import java.util.List;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AutoPublishListenerBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private ApplicationContext context;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        List<Class<?>> implementedInterfaces = ClassUtils.getAllInterfaces(bean
                .getClass());
        for (Class<?> implementedInterface : implementedInterfaces) {
            AutoPublished autoPublished = implementedInterface
                    .getAnnotation(AutoPublished.class);
            if (autoPublished != null) {
                Class<?> publisherClass = autoPublished.publisher();
                Object object = context.getBean(StringUtils
                        .uncapitalize(publisherClass.getSimpleName()));
                try {
                    MethodUtils.invokeMethod(object, "addListener", bean);
                } catch (Exception e) {
                    throw new IllegalStateException(
                            "Auto publishing event listener failed", e);
                }
            }
        }
        return bean;
    }

}
