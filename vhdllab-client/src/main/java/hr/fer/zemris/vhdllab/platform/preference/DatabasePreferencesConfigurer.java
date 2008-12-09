package hr.fer.zemris.vhdllab.platform.preference;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabasePreferencesConfigurer {

    @Autowired
    private UserFileManager manager;

    @PostConstruct
    public void initPreferencesFactory() {
        DatabasePreferences.setManager(manager);
    }
}
