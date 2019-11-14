package ustccq.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class JMeterCodeRead_NewDriver {

    private static final String CLASSPATH_SEPARATOR = File.pathSeparator;

    private static final String OS_NAME = System.getProperty("os.name");// $NON-NLS-1$

    private static final String OS_NAME_LC = OS_NAME.toLowerCase(java.util.Locale.ENGLISH);

    private static final String JAVA_CLASS_PATH = "java.class.path";// $NON-NLS-1$

    private static final String JMETER_LOGFILE_SYSTEM_PROPERTY = "jmeter.logfile";// $NON-NLS-1$

    private static final String HEADLESS_MODE_PROPERTY = "java.awt.headless";// $NON-NLS-1$
    /** The class loader to use for loading JMeter classes. */
//    private static final DynamicClassLoader loader;

    /** The directory JMeter is installed in. */
    private static final String JMETER_INSTALLATION_DIRECTORY;

    private static final List<Exception> EXCEPTIONS_IN_INIT = new ArrayList<>();

    static {
        final List<URL> jars = new LinkedList<>();
        final String initiaClasspath = System.getProperty(JAVA_CLASS_PATH);

        // Find JMeter home dir from the initial classpath
        String tmpDir;
        StringTokenizer tok = new StringTokenizer(initiaClasspath, File.pathSeparator);
        if (tok.countTokens() == 1
                || (tok.countTokens()  == 2 // Java on Mac OS can add a second entry to the initial classpath
                    && OS_NAME_LC.startsWith("mac os x")// $NON-NLS-1$
                   )
           ) {
            File jar = new File(tok.nextToken());
            try {
                tmpDir = jar.getCanonicalFile().getParentFile().getParent();
            } catch (IOException e) {
                tmpDir = null;
            }
        } else {// e.g. started from IDE with full classpath
            tmpDir = System.getProperty("jmeter.home","");// Allow override $NON-NLS-1$ $NON-NLS-2$
            if (tmpDir.length() == 0) {
                File userDir = new File(System.getProperty("user.dir"));// $NON-NLS-1$
                tmpDir = userDir.getAbsoluteFile().getParent();
            }
        }
//        JMETER_INSTALLATION_DIRECTORY=tmpDir;
        System.out.println(tmpDir);
        JMETER_INSTALLATION_DIRECTORY="D:\\apache-jmeter-5.0";

        /*
         * Does the system support UNC paths? If so, may need to fix them up
         * later
         */
        boolean usesUNC = OS_NAME_LC.startsWith("windows");// $NON-NLS-1$

        // Add standard jar locations to initial classpath
        StringBuilder classpath = new StringBuilder();
        File[] libDirs = new File[] { new File(JMETER_INSTALLATION_DIRECTORY + File.separator + "lib"),// $NON-NLS-1$ $NON-NLS-2$
                new File(JMETER_INSTALLATION_DIRECTORY + File.separator + "lib" + File.separator + "ext"),// $NON-NLS-1$ $NON-NLS-2$
                new File(JMETER_INSTALLATION_DIRECTORY + File.separator + "lib" + File.separator + "junit")};// $NON-NLS-1$ $NON-NLS-2$
        for (File libDir : libDirs) {
            File[] libJars = libDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (libJars == null) {
                new Throwable("Could not access " + libDir).printStackTrace(); // NOSONAR No logging here
                continue;
            }
            Arrays.sort(libJars); // Bug 50708 Ensure predictable order of jars
            for (File libJar : libJars) {
                try {
                    String s = libJar.getPath();

                    // Fix path to allow the use of UNC URLs
                    if (usesUNC) {
                        if (s.startsWith("\\\\") && !s.startsWith("\\\\\\")) {// $NON-NLS-1$ $NON-NLS-2$
                            s = "\\\\" + s;// $NON-NLS-1$
                        } else if (s.startsWith("//") && !s.startsWith("///")) {// $NON-NLS-1$ $NON-NLS-2$
                            s = "//" + s;// $NON-NLS-1$
                        }
                    } // usesUNC

                    jars.add(new File(s).toURI().toURL());// See Java bug 4496398
                    classpath.append(CLASSPATH_SEPARATOR);
                    classpath.append(s);
                } catch (MalformedURLException e) { // NOSONAR
                    EXCEPTIONS_IN_INIT.add(new Exception("Error adding jar:"+libJar.getAbsolutePath(), e));
                }
            }
        }

        // ClassFinder needs the classpath
        System.setProperty(JAVA_CLASS_PATH, initiaClasspath + classpath.toString());
        
    }
    
    private JMeterCodeRead_NewDriver() {
    	
    }
    
	public static void main(String[] args) {
		System.out.println("nothing but tst");
	}

}
