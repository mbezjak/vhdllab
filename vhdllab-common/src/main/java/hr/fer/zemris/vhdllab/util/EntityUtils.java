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
package hr.fer.zemris.vhdllab.util;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.PreferencesFile;
import hr.fer.zemris.vhdllab.entity.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class EntityUtils {

    public static List<PreferencesFile> cloneFiles(List<PreferencesFile> files) {
        List<PreferencesFile> clonedFiles = new ArrayList<PreferencesFile>(
                files.size());
        for (PreferencesFile file : files) {
            clonedFiles.add(new PreferencesFile(file));
        }
        return clonedFiles;
    }

    public static Set<File> cloneFiles(Set<File> files) {
        Set<File> clonedFiles = new HashSet<File>(files.size());
        for (File file : files) {
            clonedFiles.add(new File(file));
        }
        return clonedFiles;
    }

    public static List<Project> cloneProjects(List<Project> projects) {
        List<Project> clonedProjects = new ArrayList<Project>(projects.size());
        for (Project project : projects) {
            clonedProjects.add(new Project(project));
        }
        return clonedProjects;
    }

    public static File lightweightClone(File file) {
        File clone = new File(file);
        clone.setData(null);
        return clone;
    }

    public static Project lightweightClone(Project project) {
        Project clone = new Project(project);
        clone.setFiles(null);
        return clone;
    }

    public static void setNullFiles(Collection<Project> projects) {
        for (Project project : projects) {
            project.setFiles(null);
        }
    }

}
