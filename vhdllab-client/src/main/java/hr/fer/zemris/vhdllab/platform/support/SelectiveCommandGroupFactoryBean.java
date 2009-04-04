package hr.fer.zemris.vhdllab.platform.support;

import hr.fer.zemris.vhdllab.platform.context.Environment;

import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandGroupFactoryBean;

public class SelectiveCommandGroupFactoryBean extends CommandGroupFactoryBean {

    @Override
    protected void initCommandGroupMembers(CommandGroup group) {
        Object[] members = getMembers();
        for (int i = 0; i < members.length; i++) {
            Object o = members[i];
            if (isDevelopmentMenu(o) && !Environment.isDevelopment()) {
                /*
                 * Development menu is only initialized in a development
                 * environment!
                 */
                getMembers()[i] = null;
            }
        }
        super.initCommandGroupMembers(group);
    }

    private boolean isDevelopmentMenu(Object o) {
        return o instanceof CommandGroup
                && ((CommandGroup) o).getId().equals("developmentMenu");
    }

}
