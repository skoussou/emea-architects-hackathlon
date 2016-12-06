package emea.summit.architects;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Log4jTest extends HttpServlet {

    /**
     * Serialization UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger instance
     */
    private Log log = LogFactory.getLog("test");

    /**
     * {@inheritDoc}
     */
    protected void doGet(
                    HttpServletRequest request,
                    HttpServletResponse response) throws ServletException,
                    IOException {
        log.info("from log4j=== test log4j log");
        log.debug("from log4j=== test log4j debug log");
        log.error("from log4j=== test log4j error log");

        System.out
            .println("from system.out.println==== test system.out.println log");
        System.err
            .println("from system.err.println==== test system.error.println log");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("from printwriter=== test printwriter log");

        try {
            List<String> ar = new ArrayList<String>();
            ar.add("1");
            ar.add("2");

            log.info("from log4j=== get arraylist index=2" + ar.get(2));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            log.error("from log4j=== error===" + e.getMessage(), e);
            System.err.println("from system.err.println=== error==="
                + e.getMessage());
        }

        out.close();
    }
}
