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
package apw.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class Nominal extends Attribute {

    private Map<Double, String> dts = new Hashtable<Double, String>();
    private Map<String, Double> std = new Hashtable<String, Double>();
    private Double[] doubleKeyBuffer;
    private String[] stringKeyBuffer;

    public Nominal(String values) throws ParseException {
        String[] v = values.split(",");
        if (v.length == 0)
            throw new ParseException("", 0);
        for (int i = 0; i < v.length; i++) {
            v[i] = v[i].trim();
            std.put(v[i], Double.valueOf(i));
            dts.put(Double.valueOf(i), v[i]);
        }
    }

    public Set<String> getKeys() {
        return std.keySet();
    }

    /**
     * The method name reads as follows: "get sorted representation keys".
     * @return lazy created Double [] array
     */
    public Double[] getSortedRKeys() {
        if (doubleKeyBuffer == null) {
            String[] s = getSortedIKeys();
            doubleKeyBuffer = new Double[s.length];
            for (int i = 0; i < s.length; i++)
                doubleKeyBuffer[i] = std.get(s[i]);
            return doubleKeyBuffer;
        }
        return doubleKeyBuffer;
    }

    /**
     * The method name reads as follows: "get sorted interpretation keys".
     * @return lazy created String [] array
     */
    public String[] getSortedIKeys() {
        if (stringKeyBuffer == null) {
            Set<String> keys = std.keySet();
            List<String> list = new ArrayList();
            list.addAll(keys);
            Collections.sort(list);
            stringKeyBuffer = keys.toArray(new String[]{});
        }
        return stringKeyBuffer;
    }

    @Override
    public String toString() {
        return name + ":NOMINAL" + std.toString().replace("[", "{").replace("]", "}");
    }

    @Override
    public Object getInterpretation(Object o) {
        if (o == null)
            return null;
        return dts.get(o);
    }

    @Override
    public Object getRepresentation(Object o) {
        if (o == null)
            return null;
        if (o instanceof String) {
            String s = (String) o;
            return std.get(s);
        } else
            throw new IllegalArgumentException("o must be String");
    }

    @Override
    public Object getRepresentation(String s) {
        return getRepresentation((Object) s);
    }
}
