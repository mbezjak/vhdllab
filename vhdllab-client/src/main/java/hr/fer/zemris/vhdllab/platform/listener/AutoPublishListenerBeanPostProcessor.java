/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.platform.listener;

import java.util.List;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

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
