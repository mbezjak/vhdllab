package hr.fer.zemris.vhdllab.servlets.methods;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	DoMethodCompileFileTest.class,
	DoMethodCreateNewFileTest.class,
	DoMethodCreateNewProjectTest.class,
	DoMethodExistsFileTest.class,
	DoMethodExistsProjectTest.class,
	DoMethodFindProjectsByUserTest.class,
	DoMethodGenerateVHDLTest.class,
	DoMethodLoadFileBelongsToProjectIdTest.class,
	DoMethodLoadFileContentTest.class,
	DoMethodLoadFileNameTest.class,
	DoMethodLoadFileTypeTest.class,
	DoMethodLoadProjectFilesIdTest.class,
	DoMethodLoadProjectNameTest.class,
	DoMethodLoadProjectNmbrFilesTest.class,
	DoMethodLoadProjectOwnerIdTest.class,
	DoMethodRenameFileTest.class,
	DoMethodRenameProjectTest.class,
	DoMethodRunSimulationTest.class,
	DoMethodSaveFileTest.class,
	DoMethodSaveProjectTest.class
})
public class AllPackageTests {
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllPackageTests.class);
	}
	
}
