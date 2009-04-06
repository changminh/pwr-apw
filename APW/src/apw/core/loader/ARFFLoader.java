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
package apw.core.loader;

import apw.core.Attribute;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.util.FastVector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * $Rev$
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class ARFFLoader {

    private static final String ARFF_COMMENT_TAG = "%";
    private static final String ARFF_RELATION_TAG = "@relation";
    private static final String ARFF_ATTRIBUTE_TAG = "@attribute";
    BufferedReader in;
    String name;
    String line;
    ArrayList<Attribute> atts = new ArrayList<Attribute>();
    FastVector data = new FastVector();
    /** Supposed to be of type long, but ParseException accepts only
    int as a argument = LAME */
    int lNo = 0;
    private Samples samples;

    public ARFFLoader(File input) throws IOException, ParseException {
        this(new FileInputStream(input));
    }

    public ARFFLoader(InputStream in) throws ParseException, IOException {
        this.in = new BufferedReader(new InputStreamReader(in));
        parse();
        samples.setData(data);
    }

    public Samples getSamples() {
        return samples;
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

    @SuppressWarnings("unchecked")
    private void parseAttribute(String k) throws ParseException {

        String attName;
        int i;
        // TODO: Add name with spaces handling
        k = k.trim();
        // extract attribute name
        // name is quoted
        if (k.charAt(0) == '"') {
            i = k.indexOf('"', 1);
            attName = k.substring(1, i);
            i++;
        } else {
            i = 1;
            try {
                while (!Character.isWhitespace(k.charAt(i))) {
                    i++;
                }
            } catch (IndexOutOfBoundsException _) {
                // no whitespace character = wrong file format
                errorMessage("unparsable_attribute");
            }
            attName = k.substring(0, i);
        }
        k = k.substring(i).trim();
        atts.add(Attribute.createAttribute(attName, k));
        System.out.println("attname =" + attName + "; rest =" + k + ";");

    }

    /**
     * <a href="http://www.cs.waikato.ac.nz/~ml/weka/arff.html">specification</a>
     * @throws java.text.ParseException
     * @throws java.io.IOException
     */
    private void readHeader() throws ParseException, IOException {

        // get relation name
        String l = nextLine(), k;
        if (!l.toLowerCase().startsWith(ARFF_RELATION_TAG))
            errorMessage("relation");
        name = l.substring(ARFF_RELATION_TAG.length()).trim();

        while ((l = nextLine()).toLowerCase().startsWith(ARFF_ATTRIBUTE_TAG)) {
            k = l.substring(ARFF_ATTRIBUTE_TAG.length() + 1);
            parseAttribute(k);
        }
        samples = new Samples(atts);
        System.out.println(atts.toString());
    }

    private void readData() throws ParseException, IOException {
        String l, t[];
        Object o[];
        while ((l = nextLine()) != null) {
            t = l.split(",");
            o = new Object[t.length];
            for (int i = 0; i < t.length; i++)
                o[i] = atts.get(i).getRepresentation(t[i].trim());
            Sample q = new Sample(samples, o);
            data.addElement(q);
        }
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
                ResourceBundle.getBundle(bundle).getString(
                resource_msg) + ls;
        String exc =
                ResourceBundle.getBundle(bundle).getString(
                "parseException") + ": " + message;
        String linetxt =
                ResourceBundle.getBundle(bundle).getString(
                "line_no") + lNo + ": \"" + line + "\"";

        throw new ParseException(exc + linetxt, lNo);
    }

    public static void main(String args[]) {
        try {
            File f = new File("data/iris.arff");
            ARFFLoader l = new ARFFLoader(f);
            System.out.println("No: " + l.getSamples().size());
            System.out.println("Samples:\n" + l.getSamples().toString());
        } catch (ParseException ex) {
            System.out.println("Parse łeksepszyn!");
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Something gone wrong!");
        }

    }
}
