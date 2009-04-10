package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class PredefinedFileDaoImplTest {

    private static final String TEST_LOCATION = "src/test/resources/predefined";

    private PredefinedFileDaoImpl dao;

    @Before
    public void initClass() {
        dao = new PredefinedFileDaoImpl();
        dao.setLocation(new java.io.File(TEST_LOCATION));
        dao.initFiles();
    }

    @Test
    public void getPredefinedFiles() {
        Set<File> files = dao.getPredefinedFiles();
        assertNotNull(files);
        assertEquals(2, files.size());

        File file = (File) CollectionUtils.get(files, 0);
        assertNull(file.getId());
        assertNull(file.getVersion());
        assertNull(file.getProject());
        assertEquals(FileType.PREDEFINED, file.getType());
        assertTrue(file.getName().startsWith("PREDEFINED_FILE"));
        assertNotNull(file.getData());

        // Files are defensively copied before return!
        file.setName("new predefined name");
        assertFalse(files.equals(dao.getPredefinedFiles()));
    }

    @Test
    public void findByName() {
        assertNull(dao.findByName("non existing predefined file name"));
        assertNotNull(dao.findByName("PREDEFINED_FILE1"));
    }

    @Test
    public void getPredefinedFilesWithServletContext() {
        dao = new PredefinedFileDaoImpl();
        dao.setServletContext(new ServletContextMock());
        dao.initFiles();

        assertFalse(dao.getPredefinedFiles().isEmpty());
    }

    class ServletContextMock implements ServletContext {

        @Override
        public Object getAttribute(String name) {
            return null;
        }

        @Override
        public Enumeration<?> getAttributeNames() {
            return null;
        }

        @Override
        public ServletContext getContext(String uripath) {
            return null;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public String getInitParameter(String name) {
            return null;
        }

        @Override
        public Enumeration<?> getInitParameterNames() {
            return null;
        }

        @Override
        public int getMajorVersion() {
            return 0;
        }

        @Override
        public String getMimeType(String file) {
            return null;
        }

        @Override
        public int getMinorVersion() {
            return 0;
        }

        @Override
        public RequestDispatcher getNamedDispatcher(String name) {
            return null;
        }

        @Override
        public String getRealPath(String path) {
            return new java.io.File(TEST_LOCATION).getPath();
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        @Override
        public URL getResource(String path) throws MalformedURLException {
            return null;
        }

        @Override
        public InputStream getResourceAsStream(String path) {
            return null;
        }

        @Override
        public Set<?> getResourcePaths(String path) {
            return null;
        }

        @Override
        public String getServerInfo() {
            return null;
        }

        @Deprecated
        @Override
        public Servlet getServlet(String name) throws ServletException {
            return null;
        }

        @Override
        public String getServletContextName() {
            return null;
        }

        @Deprecated
        @Override
        public Enumeration<?> getServletNames() {
            return null;
        }

        @Deprecated
        @Override
        public Enumeration<?> getServlets() {
            return null;
        }

        @Override
        public void log(String msg) {
        }

        @Deprecated
        @Override
        public void log(Exception exception, String msg) {
        }

        @Override
        public void log(String message, Throwable throwable) {
        }

        @Override
        public void removeAttribute(String name) {
        }

        @Override
        public void setAttribute(String name, Object object) {
        }

    }
}
