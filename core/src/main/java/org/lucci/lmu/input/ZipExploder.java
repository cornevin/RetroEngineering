package org.lucci.lmu.input;

/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * class for exploding jar/zip files onto the file system
 *
 * @author Barry Feigenbaum
 */
public class ZipExploder {
    /**
     * create a zip exploder for unpacking .jar/.zip files
     */
    public ZipExploder() {
        this(false);
    }

    /**
     * create a zip exploder for unpacking .jar/.zip files onto the file system
     *
     * @param verbose -
     *          set to <code>true</code> for verbose mode
     */
    public ZipExploder(boolean verbose) {
        setVerbose(verbose);
    }

    protected boolean verbose;

    /**
     * Get the verbose mode state.
     *
     * @return verbosity
     */
    public boolean getVerbose() {
        return verbose;
    }

    /**
     * set the verbose mode state
     *
     * @param f -
     *          verbosity
     */
    public void setVerbose(boolean f) {
        verbose = f;
    }


    /**
     * Explode source JAR and/or ZIP files into a target directory
     *
     * @param jarNames
     *          names of source files
     * @param destDir
     *          target directory name (should already exist)
     * @exception IOException
     *              error creating a target file
     */
    public void process(String[] jarNames, String destDir) throws IOException {
        processJars(jarNames, destDir);
    }

    /**
     * Explode source JAR files into a target directory
     *
     * @param jarNames
     *          names of source files
     * @param destDir
     *          target directory name (should already exist)
     * @exception IOException
     *              error creating a target file
     */
    public void processJars(String[] jarNames, String destDir) throws IOException {
        for (int i = 0; i < jarNames.length; i++) {
            processFile(jarNames[i], destDir);
        }
    }


    /**
     * Explode source ZIP or JAR file into a target directory
     *
     * @param zipName
     *          names of source file
     * @param destDir
     *          target directory name (should already exist)
     * @exception IOException
     *              error creating a target file
     */
    public void processFile(String zipName, String destDir) throws IOException {
        String source = new File(zipName).getCanonicalPath();
        String dest = new File(destDir).getCanonicalPath();
        ZipFile f = null;
        try {
            f = new ZipFile(source);
            Map fEntries = getEntries(f);
            String[] names = (String[]) fEntries.keySet().toArray(new String[] {});

            // copy all files
            for (int i = 0; i < names.length; i++) {
                System.out.println("Here all the files : " + names[i]);
                String name = names[i];
                if(names[i].equals("META-INF/MANIFEST.MF")) {
                    ZipEntry e = (ZipEntry) fEntries.get(name);
                    copyFileEntry(dest, f, e);
                }
            }
        } catch (IOException ioe) {
            String msg = ioe.getMessage();
            if (msg.indexOf(zipName) < 0) {
                msg += " - " + zipName;
            }
            throw new IOException(msg);
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    /** Get all the entries in a ZIP file. */
    protected Map getEntries(ZipFile zf) {
        Enumeration e = zf.entries();
        Map m = new HashMap();
        while (e.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) e.nextElement();
            m.put(ze.getName(), ze);
        }
        return m;
    }

    /**
     * copy a single entry from the archive
     *
     * @param destDir
     * @param zf
     * @param ze
     * @throws IOException
     */
    public void copyFileEntry(String destDir, ZipFile zf, ZipEntry ze) throws IOException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(zf.getInputStream(ze)));
        try {
            copyFileEntry(destDir, ze.isDirectory(), ze.getName(), dis);
        } finally {
            System.out.println(";(");
            try {
                dis.close();
            } catch (IOException ioe) {
            }
        }
    }

    protected void copyFileEntry(String destDir, boolean destIsDir, String destFile,
                                 DataInputStream dis) throws IOException {
        System.out.println("LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOL");
        byte[] bytes = readAllBytes(dis);
        File file = new File(destFile);
        String parent = file.getParent();
        if (parent != null && parent.length() > 0) {
            File dir = new File(destDir, parent);
            if (dir != null) {
                dir.mkdirs();
            }
        }
        File outFile = new File(destDir, destFile);
        System.out.println("OutFile : " + outFile);
        if (destIsDir) {
            outFile.mkdir();
        } else {
            FileOutputStream fos = new FileOutputStream(outFile);
            try {
                System.out.println("please");
                fos.write(bytes, 0, bytes.length);
            } finally {
                System.out.println("NOP");
                try {
                    fos.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    // *** below may be slow for large files ***
    /** Read all the bytes in a ZIPed file */
    protected byte[] readAllBytes(DataInputStream is) throws IOException {
        byte[] bytes = new byte[0];
        for (int len = is.available(); len > 0; len = is.available()) {
            byte[] xbytes = new byte[len];
            int count = is.read(xbytes);
            if (count > 0) {
                byte[] nbytes = new byte[bytes.length + count];
                System.arraycopy(bytes, 0, nbytes, 0, bytes.length);
                System.arraycopy(xbytes, 0, nbytes, bytes.length, count);
                bytes = nbytes;
            } else if (count < 0) {
                // accommodate apparent bug in IBM JVM where
                // available() always returns positive value on some files
                break;
            }
        }
        return bytes;
    }

    protected void print(String s) {
        System.out.print(s);
    }

    /** Print command help text. */
    protected static void printHelp() {
        System.out.println();
        System.out.println("Usage: java " + ZipExploder.class.getName()
                + " (-jar jarFilename... | -zip zipFilename...)... -dir destDir {-verbose}");
        System.out.println("Where:");
        System.out.println("  jarFilename path to source jar, may repeat");
        System.out.println("  zipFilename path to source zip, may repeat");
        System.out.println("  destDir    path to target directory; should exist");
        System.out.println("Note: one -jar or -zip is required; switch case or order is not important");
    }

    protected static void reportError(String msg) {
        System.err.println(msg);
        // printHelp();
        System.exit(1);
    }

    /**
     * Main command line entry point.
     *
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            printHelp();
            System.exit(0);
        }
        List zipNames = new ArrayList();
        List jarNames = new ArrayList();
        String destDir = null;
        boolean jarActive = false, zipActive = false, destDirActive = false;
        boolean verbose = false;
        // process arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-') { // switch
                arg = arg.substring(1);
                if (arg.equalsIgnoreCase("jar")) {
                    jarActive = true;
                    zipActive = false;
                    destDirActive = false;
                } else if (arg.equalsIgnoreCase("zip")) {
                    zipActive = true;
                    jarActive = false;
                    destDirActive = false;
                } else if (arg.equalsIgnoreCase("dir")) {
                    jarActive = false;
                    zipActive = false;
                    destDirActive = true;
                } else if (arg.equalsIgnoreCase("verbose")) {
                    verbose = true;
                } else {
                    reportError("Invalid switch - " + arg);
                }
            } else {
                if (jarActive) {
                    jarNames.add(arg);
                } else if (zipActive) {
                    zipNames.add(arg);
                } else if (destDirActive) {
                    if (destDir != null) {
                        reportError("duplicate argument - " + "-destDir");
                    }
                    destDir = arg;
                } else {
                    reportError("Too many parameters - " + arg);
                }
            }
        }
        if (destDir == null || (zipNames.size() + jarNames.size()) == 0) {
            reportError("Missing parameters");
        }
        if (verbose) {
            System.out.println("Effective command: " + ZipExploder.class.getName() + " "
                    + (jarNames.size() > 0 ? "-jars " + jarNames + " " : "")
                    + (zipNames.size() > 0 ? "-zips " + zipNames + " " : "") + "-dir " + destDir);
        }
        try {
            ZipExploder ze = new ZipExploder(verbose);
            ze.process((String[]) jarNames.toArray(new String[jarNames.size()]), destDir);
        } catch (IOException ioe) {
            System.err.println("Exception - " + ioe.getMessage());
            ioe.printStackTrace(); // *** debug ***
            System.exit(2);
        }
    }
}




