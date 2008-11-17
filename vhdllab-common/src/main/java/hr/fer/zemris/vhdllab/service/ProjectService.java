package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;

import java.util.List;

public interface ProjectService {

    ProjectInfo save(ProjectInfo project);

    void delete(ProjectInfo project);

    List<ProjectInfo> findByUser(Caseless userId);

}
