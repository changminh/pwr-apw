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
package apw.core.util;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.net.*;

/**
 * This abstract class can be used to obtain a list of all classes in a classpath.
 *
 * <em>Caveat:</em> When used in environments which utilize multiple class loaders--such as
 * a J2EE Container like Tomcat--it is important to select the correct classloader
 * otherwise the classes returned, if any, will be incompatible with those declared
 * in the code employing this class lister.
 * to get a reference to your classloader within an instance method use:
 *  <code>this.getClass().getClassLoader()</code> or
 *  <code>Thread.currentThread().getContextClassLoader()</code> anywhere else
 * <p>
 * @author Kris Dover <krisdover@hotmail.com>
 * @version 0.2.0
 * @since   0.1.0
 */
public abstract class ClassList {


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
                        if(!superClassQualify) continue;
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
        for(String s : map.keySet()) {
            set.addAll(map.get(s));
        }
        return set;
    }
}
