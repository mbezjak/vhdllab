package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.api.comm.MethodConstants;
import hr.fer.zemris.vhdllab.entities.Caseless;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * A main vhdllab servlet. One that processes all {@link Method}s.
 * 
 * @author Miro Bezjak
 */
public class AjaxServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * A logger instance.
     */
    private static final Logger LOG = Logger.getLogger(AjaxServlet.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String remoteUser = request.getRemoteUser();
        if (remoteUser == null) {
            LOG.fatal("Current security implementation can't work without "
                    + "user authentication and session tracking!");
            throw new ServletException(
                    "Security implementation cant work with no remote user");
        }
        Method<Serializable> method = deserializeObject(request);
        if (method == null) {
            /*
             * Since error occurred during deserialization there could not be
             * any valid respose so simply response that user sent a bad request
             * (such that could not be deserialized).
             * 
             * Note that error during deserialization could also mean external
             * tempering (trying to modify bytes of serialized method) and
             * trying to break security of this servlet.
             */
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (!isSecurityValid(method, request)) {
            /*
             * This means that user is trying to perform action as one user
             * while he is logged as another. This could mean that he is trying
             * to break security of this servlet.
             */
            if (LOG.isEnabledFor(Level.WARN)) {
                LOG.warn("Possible security issue caused by user: "
                        + remoteUser
                        + ". He signed a request method with userId: "
                        + method.getUserId());
            }
            method.setStatus(Method.SE_NO_PERMISSION);
            returnResponse(method, request, response);
        }

        RegisteredMethod regMethod = MethodFactory
                .getMethod(method.getMethod());
        if (regMethod == null) {
            // TODO tu jos treba separirat metode koji obicni korisnik smije
            // obavit i one koje samo admin moze. Npr. delete.global.file smije
            // samo admin. To napravit preko request.isUserInRole metode
            if (LOG.isEnabledFor(Level.WARN)) {
                LOG.warn("A user (" + remoteUser
                        + ") requested inexistent method ("
                        + method.getMethod() + ")");
            }
            method.setStatus(MethodConstants.SE_INVALID_METHOD_CALL);
            returnResponse(method, request, response);
        }

        try {
            regMethod.run(method, request);
        } catch (SecurityException e) {
            /*
             * This indicates for example that a user tried to delete a file
             * that belongs to another user.
             */
            if (LOG.isEnabledFor(Level.WARN)) {
                LOG.warn("A user (" + remoteUser
                        + ") tried to manipulate with "
                        + "a resource that does not belong to him", e);
            }
            method.setStatus(MethodConstants.SE_NO_PERMISSION);
        } catch (RuntimeException e) {
            if (LOG.isEnabledFor(Level.WARN)) {
                LOG.warn("An unknow exception occurred", e);
            }
            method.setStatus(MethodConstants.SE_INTERNAL_SERVER_ERROR);
        }
        returnResponse(method, request, response);
    }

    /**
     * @param method
     * @param request
     * @return
     */
    private boolean isSecurityValid(Method<Serializable> method,
            HttpServletRequest request) {
        return method.getUserId().equals(new Caseless(request.getRemoteUser()))
                || request.isUserInRole("admin");
    }

    /**
     * @param request
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private Method<Serializable> deserializeObject(HttpServletRequest request)
            throws IOException {
        InputStream is = request.getInputStream();
        GZIPInputStream gzip = new GZIPInputStream(is);
        ObjectInputStream ois = new ObjectInputStream(gzip);
        try {
            return (Method<Serializable>) ois.readObject();
        } catch (ClassNotFoundException e) {
            LOG.error("Possible security issue caused by user: "
                    + request.getRemoteUser(), e);
            return null;
        } catch (ClassCastException e) {
            LOG.error("Possible security issue caused by user: "
                    + request.getRemoteUser(), e);
            return null;
        } finally {
            ois.close();
        }
    }

    /**
     * This method is called to actually send the results back to the caller.
     * 
     * @param method
     *            a method to send
     * @param request
     *            http servlet request
     * @param response
     *            http servlet response
     * @throws IOException
     *             if method can not send results
     */
    private void returnResponse(Method<Serializable> method,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Cache-Control", "no-cache");
        GZIPOutputStream gzip = new GZIPOutputStream(response.getOutputStream());
        ObjectOutputStream oos = new ObjectOutputStream(gzip);
        oos.writeObject(method);
        oos.close(); // must be close (just flushing isn't enough)
    }

}