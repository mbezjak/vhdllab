package hr.fer.zemris.vhdllab.dao.impl.support;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Emulates {@link OpenEntityManagerInViewFilter} that production ready
 * application will have installed. However unlike filter that opens entity
 * manager for every new web request this listener opens entity manager after
 * application context is started. This means that
 * {@link PlatformTransactionManager} will not open entity manager in the scope
 * of a transaction but use existing thread bound entity manager.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class OpenEntityManagerInViewListener implements ApplicationListener {

    @PersistenceUnit
    public EntityManagerFactory entityManagerFactory;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            EntityManager em = entityManagerFactory.createEntityManager();
            /*
             * Binds entity manager to current thread - the one running all the
             * tests.
             */
            TransactionSynchronizationManager.bindResource(
                    entityManagerFactory, new EntityManagerHolder(em));
        }
    }

}
