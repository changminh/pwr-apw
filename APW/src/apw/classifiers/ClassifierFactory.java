/*
 *  Copyright (c) 2009, Wrocław University of Technology
 *  All rights reserved.
 *  Redistribution  and use in source  and binary  forms,  with or
 *  without modification,  are permitted provided that the follow-
 *  ing conditions are met:
 * 
 *   • Redistributions of source code  must retain the above copy-
 *     right  notice, this list  of conditions and  the  following
 *     disclaimer.
 *   • Redistributions  in binary  form must  reproduce the  above
 *     copyright notice, this list of conditions and the following
 *     disclaimer  in  the  documentation and / or other materials
 *     provided with the distribution.
 *   • Neither  the name of the  Wrocław University of  Technology
 *     nor the names of its contributors may be used to endorse or
 *     promote products derived from this  software without speci-
 *     fic prior  written permission.
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRI-
 *  BUTORS "AS IS" AND ANY  EXPRESS OR IMPLIED WARRANTIES, INCLUD-
 *  ING, BUT NOT  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTA-
 *  BILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 *  NO EVENT SHALL THE COPYRIGHT HOLDER OR  CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCURE-
 *  MENT OF SUBSTITUTE  GOODS OR SERVICES;  LOSS OF USE,  DATA, OR
 *  PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER  CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING  NEGLIGENCE  OR  OTHERWISE) ARISING IN ANY WAY
 *  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSI-
 *  BILITY OF SUCH DAMAGE.
 */
package apw.classifiers;

import apw.core.meta.ClassifierCapabilities;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class ClassifierFactory {

    public static final Set<Class> registeredClassifiers = new HashSet();

    /**
     * Filter out registered classifiers according to parameters.
     *
     * @param numAtt
     * @param nomAtt
     * @param numClz
     * @param nomClz
     * @param includeUndefined
     * @return
     */
    public static Set<Class> getFeasibleClassifierSet(
            boolean numAtt,
            boolean nomAtt,
            boolean numClz,
            boolean nomClz,
            boolean includeUndefined) {
        Set<Class> set = new HashSet();
        for (Class c : registeredClassifiers) {
            boolean passed = false;
            if (c.getAnnotation(ClassifierCapabilities.class) != null) {
                ClassifierCapabilities caps = (ClassifierCapabilities) c.getAnnotation(ClassifierCapabilities.class);
                if (caps == null)
                    continue;
                passed = true;

                if (numAtt)
                    passed &= caps.handlesNumericAtts();
                if (nomAtt)
                    passed &= caps.handlesNominalAtts();
                if (nomClz)
                    passed &= caps.multiClass();
                if (numClz)
                    passed &= caps.regression();

            } else if (includeUndefined)
                passed = true;
            if (passed)
                set.add(c);
        }
        return set;
    }

    /**
     * Initiates <code>registeredClassifiers</code> set using selected
     * class loader.
     */
    public static final void initializeClassifierList(ClassLoader classLoader) {
//        try {
//            registeredClassifiers.addAll(discoverClassifiers(classLoader));
//        } catch (ClassNotFoundException ex) {
//            System.out.println("Enumerating jarfiles not possible");
//        }
        registeredClassifiers.addAll(loadClassifiers(classLoader));
    }

    /**
     * <p>Loads plugin classifiers as listed in
     * <code>META-INF/classifier-plugin.properties</code>
     * file. Returns a set of loaded classes. </p>
     *
     * <p>A code safe to run under Java WebStart facility.</p>
     * @param classLoader
     * @return set of loaded classifiers
     */
    public static Set<Class> loadClassifiers(ClassLoader classLoader) {
        Set<Class> classifiers = new HashSet();
        try {
            Enumeration urls = classLoader.getResources("META-INF/classifier-plugin.properties");
            while (urls.hasMoreElements()) {
                URL pluginUrl = (URL) urls.nextElement();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(pluginUrl.openStream()));
                while (true) {
                    String line = reader.readLine();
                    if (line == null)
                        break;
                    String[] parts = line.split("=");
                    if (parts.length != 2)
                        continue;
                    String key = parts[0];
                    String value = parts[1];
                    if ("Classifier".compareTo(key) == 0)
                        try {
                            Class pluginClass = classLoader.loadClass(value);
                            if (pluginClass == null)
                                continue;
                            if (Classifier.class.isAssignableFrom(pluginClass))
                                // ClassifierFactory.registeredClassifiers.add(pluginClass);
                                classifiers.add(pluginClass);
                        } catch (NoClassDefFoundError ncdfe) {
                            // trying to initialize a plugin with a missing
                            // class
                        }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return classifiers;
    }

    /**
     * Use this method to enumurate contents of jarfiles on classpath while
     * seeking for classifiers. Only safe to run from commandline and Java
     * WebStart facility with local codebase.
     *
     * @param classLoader
     */
    public static Set<Class> discoverClassifiers(ClassLoader classLoader)
            throws ClassNotFoundException {
        Map<String, Set<Class>> m = findClasses(
                classLoader,
                null,
                toSet("apw.classifiers.Classifier"),
                toSet("apw.classifiers"),
                null);
        Set<Class> set = flatout(m);
        return set;
    }

    /**
     * Just a convinience method.
     * @param s
     * @return
     */
    private static HashSet<String> toSet(String s) {
        return new HashSet<String>(Arrays.asList(s.split(",")));
    }

    /**
     * Searches the classpath for all classes matching a specified search criteria,
     * returning them in a map keyed with the interfaces they implement or null if they
     * have no interfaces. The search criteria can be specified via interface, package
     * and jar name filter arguments
     * <p>
     * @param classLoader       The classloader whose classpath will be traversed
     * @param interfaceFilter   A Set of fully qualified interface names to search for
     *                          or null to return classes implementing all interfaces
     * @param packageFilter     A Set of fully qualified package names to search for or
     *                          or null to return classes in all packages
     * @param jarFilter         A Set of jar file names to search for or null to return
     *                          classes from all jars
     * @return A Map of a Set of Classes keyed to their interface names
     *
     * @throws ClassNotFoundException if the current thread's classloader cannot load
     *                                a requested class for any reason
     */
    public static Map<String, Set<Class>> findClasses(ClassLoader classLoader,
            Set<String> interfaceFilter,
            Set<String> parentClassFilter,
            Set<String> packageFilter,
            Set<String> jarFilter)
            throws ClassNotFoundException {
        Map<String, Set<Class>> classTable = new HashMap();
        Object[] classPaths;
        try {
            // get a list of all classpaths
            classPaths = ((java.net.URLClassLoader) classLoader).getURLs();
        } catch (ClassCastException cce) {
            // or cast failed; tokenize the system classpath
            classPaths = System.getProperty("java.class.path", "").split(File.pathSeparator);
        }
        Class superClass;

        for (int h = 0; h < classPaths.length; h++) {
            Enumeration files = null;
            JarFile module = null;
            // for each classpath ...
            File classPath;
            try {
                classPath = new File((URL.class).isInstance(classPaths[h]) ? ((URL) classPaths[h]).toURI().getPath() : classPaths[h].toString());
            } catch (URISyntaxException ex) {
                throw new ClassNotFoundException("URI syntax exception: " + ex.getMessage());
            }
            if (classPath.isDirectory() && jarFilter == null) {   // is our classpath a directory and jar filters are not active?
                List<String> dirListing = new ArrayList();
                // get a recursive listing of this classpath
                recursivelyListDir(dirListing, classPath, new StringBuffer());
                // an enumeration wrapping our list of files
                files = Collections.enumeration(dirListing);
            } else if (classPath.getName().endsWith(".jar")) {    // is our classpath a jar?
                // skip any jars not list in the filter
                if (jarFilter != null && !jarFilter.contains(classPath.getName()))
                    continue;
                try {
                    // if our resource is a jar, instantiate a jarfile using the full path to resource
                    module = new JarFile(classPath);
                } catch (MalformedURLException mue) {
                    throw new ClassNotFoundException("Bad classpath. Error: " + mue.getMessage());
                } catch (IOException io) {
                    throw new ClassNotFoundException("jar file '" + classPath.getName() +
                            "' could not be instantiate from file path. Error: " + io.getMessage());
                }
                // get an enumeration of the files in this jar
                files = module.entries();
            }

            // for each file path in our directory or jar
            while (files != null && files.hasMoreElements()) {
                // get each fileName
                String fileName = files.nextElement().toString();
                // we only want the class files
                if (fileName.endsWith(".class")) {
                    // convert our full filename to a fully qualified class name
                    String className = fileName.replaceAll("/", ".").substring(0, fileName.length() - 6);
                    // debug class list
                    // System.out.println(className);
                    // skip any classes in packages not explicitly requested in our package filter
                    if (packageFilter != null && !qualifies(packageFilter, className))
                        continue;
                    // get the class for our class name
                    Class theClass = null;
                    try {
                        theClass = Class.forName(className, false, classLoader);
                    } catch (NoClassDefFoundError e) {
                        System.out.println("Skipping class '" + className + "' for reason " + e.getMessage());
                        continue;
                    }
                    // skip interfaces
                    if (theClass.isInterface())
                        continue;
                    //then get an array of all the interfaces in our class
                    Class[] classInterfaces = theClass.getInterfaces();

                    // remove not qalifying classes

                    if (parentClassFilter != null) {
                        boolean superClassQualify = false;
                        superClass = theClass.getSuperclass();
                        while (superClass != null) {
                            if (qualifies(parentClassFilter, superClass.getName()))
                                superClassQualify = true;
                            superClass = superClass.getSuperclass();
                        }
                        if (!superClassQualify)
                            continue;
                    }
                    // for each interface in this class, add both class and interface into the map
                    String interfaceName = null;
                    for (int i = 0; i < classInterfaces.length || (i == 0 && interfaceFilter == null); i++) {
                        if (i < classInterfaces.length) {
                            interfaceName = classInterfaces[i].getName();
                            // was this interface requested?
                            if (interfaceFilter != null && !interfaceFilter.contains(interfaceName))
                                continue;
                        }
                        // is this interface already in the map?
                        if (classTable.containsKey(interfaceName))
                            // if so then just add this class to the end of the list of classes implementing this interface
                            classTable.get(interfaceName).add(theClass);
                        else {
                            // else create a new list initialised with our first class and put the list into the map
                            Set<Class> allClasses = new HashSet();
                            allClasses.add(theClass);
                            classTable.put(interfaceName, allClasses);
                        }
                    }

                }
            }

            // close the jar if it was used
            if (module != null)
                try {
                    module.close();
                } catch (IOException ioe) {
                    throw new ClassNotFoundException("The module jar file '" + classPath.getName() +
                            "' could not be closed. Error: " + ioe.getMessage());
                }

        } // end for loop

        return classTable;
    } // end method

    private static boolean qualifies(Set<String> packageFilter, String className) {
        for (String filter : packageFilter)
            if (className.startsWith(filter))
                return true;
        return false;
    }

    /**
     * Recursively lists a directory while generating relative paths. This is a helper function for findClasses.
     * Note: Uses a StringBuffer to avoid the excessive overhead of multiple String concatentation
     *
     * @param dirListing     A list variable for storing the directory listing as a list of Strings
     * @param dir                 A File for the directory to be listed
     * @param relativePath A StringBuffer used for building the relative paths
     */
    private static void recursivelyListDir(List<String> dirListing, File dir, StringBuffer relativePath) {
        int prevLen; // used to undo append operations to the StringBuffer

        // if the dir is really a directory
        if (dir.isDirectory()) {
            // get a list of the files in this directory
            File[] files = dir.listFiles();
            // for each file in the present dir
            for (int i = 0; i < files.length; i++) {
                // store our original relative path string length
                prevLen = relativePath.length();
                // call this function recursively with file list from present
                // dir and relateveto appended with present dir
                recursivelyListDir(dirListing, files[i], relativePath.append(prevLen == 0 ? "" : "/").append(files[i].getName()));
                //  delete subdirectory previously appended to our relative path
                relativePath.delete(prevLen, relativePath.length());
            }
        } else
            // this dir is a file; append it to the relativeto path and add it to the directory listing
            dirListing.add(relativePath.toString());
    }

    public static final Set<Class> flatout(Map<String, Set<Class>> map) {
        HashSet<Class> set = new HashSet();
        for (String s : map.keySet())
            set.addAll(map.get(s));
        return set;
    }
}
