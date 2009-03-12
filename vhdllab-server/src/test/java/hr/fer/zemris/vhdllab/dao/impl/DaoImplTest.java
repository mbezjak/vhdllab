package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.ClientLogDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entity.ClientLog;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.entity.ProjectType;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for {@link FileDaoImpl}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class DaoImplTest extends AbstractDaoSupport {

    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ClientLogDao clientLogDao;

    @Test
    public void saveAndLoad() {
        Project p = new Project();
        p.setName("test");
        p.setType(ProjectType.USER);
        p.setUserId("user_id");
        projectDao.persist(p);

        File f = new File();
        f.setData("a new data");
        f.setName("file_name");
        f.setType(FileType.SCHEMA);

        p.addFile(f);

        Project project = projectDao.load(1);
        System.out.println(project);
        System.out.println(f);
    }

    @Test
    public void saveAndLoadClientLog() {
        ClientLog log = new ClientLog("userid");
        log.setData("a data of a client log");
        System.out.println(log);
        clientLogDao.persist(log);

        log = clientLogDao.load(1);
        System.out.println(log);
    }

}
