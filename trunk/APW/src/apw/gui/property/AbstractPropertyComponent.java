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
package apw.gui.property;

import apw.gui.property.validation.Description;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public abstract class AbstractPropertyComponent implements PropertyComponent {

    IPropertyChangeListener listener;
    protected Set<Annotation> annotations;

    public void registerListener(IPropertyChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Call this method when validation error occurs. Default implementation
     * will cause a notification message to be presented.
     * 
     * @param text user presented message
     */
    public void validationErrorMessage(String text) {
        if (listener != null)
            listener.validationErrorMessage(text);
    }

    /**
     * Call this method every time the property has changed to keep
     * backing model in sync.
     *
     * @param validated wether or not the newValue passes validation
     * @param oldValue 
     * @param newValue
     */
    public void propertyChanged(boolean validated, Object oldValue, Object newValue) {
        if (listener != null)
            listener.propertyChanged(validated, oldValue, newValue);
    }

    /**
     * Perform basic annotation assesments valid for all PropertyTypes.
     * @param desc
     */
    public void initialize(PropertyDescriptor desc) {
        // Create a Set<Annotation> out of Annotation[] ensuring
        // no duplicates exist (and there are no multiple interpretations).
        Annotation[] anns = desc.anns;
        Set<Class<? extends Annotation>> ac = new HashSet();
        Set<Annotation> as = new HashSet();
        Class<? extends Annotation> clazz;
        for (int i = 0; i < anns.length;
                i++) {
            clazz = anns[i].annotationType();
            if (ac.contains(clazz))
                throw new DuplicatedPropertyAnnotationException(clazz, desc);
            ac.add(clazz);
            as.add(anns[i]);
        }
        // Now we check if all annotations can be used with the particular
        // PropertyComponent
        Set<Class<? extends Annotation>> valid = new HashSet();
        // one special case for annotation common for all property types
        valid.add(Description.class);
        configureValidAnnotationSet(valid);
        // now throw an exception if a property is annotated wrong
        for (Annotation a : as)
            if (!valid.contains(a.annotationType()))
                throw new PropertyAnnotationMismatchException(a, desc.clazz);
        // last step - assigns annotation set
        this.annotations = as;
    }

    public abstract void configureValidAnnotationSet(
            Set<Class<? extends Annotation>> valid);
}
