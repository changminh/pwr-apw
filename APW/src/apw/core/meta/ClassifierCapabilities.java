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
package apw.core.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p> Annotation used by GUI to determine Classifier capabilities.
 * Used in scenario when user loads particular {@link Samples} set and
 * is presented with a list of appropriate classifiers. This list is
 * filtered according to boolean values set in this annotation.</p>
 *
 * <p>This annotation will help presenting user with a pleasant interface
 * experience since no exceptions should be thrown while using inappropriate
 * classifier for a set.</p>
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClassifierCapabilities {

    /**
     * <code>true</code> means that the classifier can predict nominal attribute
     * as a class value.
     */
    boolean multiClass();

    /**
     * <code>true</code> means that the classifier is capable of regression,
     * in other words: accepts numerical class attribute.
     */
    boolean regression() default false;

    /**
     * Set to true if the classifier accepts Samples nominal attributes.
     */
    boolean handlesNominalAtts() default false;

    /**
     * Set to true if the classifier accepts Samples numeric attributes.
     */
    boolean handlesNumericAtts() default false;
}
