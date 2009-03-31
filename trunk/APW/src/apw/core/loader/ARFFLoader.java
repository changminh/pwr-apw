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
 *     disclaimer in the  documentation and/or other mate provided
 *     with the distribution.
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
package apw.core.loader;

import apw.core.Attribute;
import apw.core.Samples;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * $Rev $
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class ARFFLoader {

    public static final String ARFF_COMMENT_TAG = "%";
    public static final String ARFF_RELATION_TAG = "@relation";
    BufferedReader in;
    String name;
    String line;
    List<Attribute> atts;
    /** Supposed to be of type long, but ParseException accepts only
    int as a argument = LAME */
    int lNo = 0;

    public ARFFLoader(File input) throws IOException, ParseException {
        this(new FileInputStream(input));
    }

    public ARFFLoader(InputStream in) throws ParseException, IOException {
        this.in = new BufferedReader(new InputStreamReader(in));
        parse();
    }

    public Samples getSamples() {
        // TODO: Do me do
        return null;
    }

    private String nextLine() throws IOException {
        line = in.readLine();
        lNo++;
        while (line != null &&
                (line.startsWith(ARFF_COMMENT_TAG) ||
                line.trim().length() == 0)) {
            line = in.readLine();
            lNo++;
        }
        return line;
    }

    private void parse() throws ParseException, IOException {
        readHeader();
        readData();
    }

    /**
     * <a href="http://www.cs.waikato.ac.nz/~ml/weka/arff.html">specification</a>
     * @throws java.text.ParseException
     * @throws java.io.IOException
     */
    private void readHeader() throws ParseException, IOException {
        boolean done = false;

        // get relation name
        String l = nextLine();
        if (!l.toLowerCase().startsWith(ARFF_RELATION_TAG))
            errorMessage("relation");
        name = l.substring(ARFF_RELATION_TAG.length()).trim();
    }

    private void readData() throws ParseException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Throws error message with line number and last token read.
     *
     * @param resource_msg 		resource key pointing to localized error message
     * @throws ParseException 	containing the error message
     */
    private void errorMessage(String resource_msg) throws ParseException {
        String ls = System.getProperty("line.separator");
        final String bundle = "apw/core/loader/ARFFLoader";

        String message =
                ResourceBundle.getBundle(bundle).getString(resource_msg) + ls;
        String exc =
                ResourceBundle.getBundle(bundle).getString("parseException") + ": " + message;
        String linetxt =
                ResourceBundle.getBundle(bundle).getString("line_no") + lNo + ": \"" + line + "\"";

        throw new ParseException(exc + linetxt, lNo);
    }

    public static void main(String args[]) {
        try {
            File f = new File("data/iris.arff");
            ARFFLoader l = new ARFFLoader(f);
        } catch (ParseException ex) {
            System.out.println("Parse łeksepszyn!");
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Something gone wrong!");
        }

    }
}
