package hr.fer.zemris.vhdllab.entity;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public class HistoryListenerConfigurer {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @PostConstruct
    public void bindFactoryToHistoryListener() {
        HistoryListener.setEntityManagerFactory(entityManagerFactory);
    }

}