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
package apw.gui.property;

import apw.gui.property.component.StringComponent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class PropertyFactory {

    /********* Dynamic type to component mapping section ahead ************/
    private interface TypeToGUIHandler {
        // TODO: the JComponent must be replaced by >>PropertyComponent<< class

        // public PropertyComponent getComponentForType();
        public PropertyComponent getComponentForType();
    }
    /**
     * Map
     */
    private static Map<Class, TypeToGUIHandler> propertyMap = new HashMap();

    static {
        propertyMap.put(String.class, new TypeToGUIHandler() {
            public PropertyComponent getComponentForType() {
                return new StringComponent();
            }
        });

    }

    public static PropertyComponent mapTypeToGUIComponent(Class o) {
        // TODO: Change fallback value for something relevant
        PropertyComponent comp = new StringComponent();
        if (propertyMap.containsKey(o))
            comp = propertyMap.get(o).
                    getComponentForType();
        return comp;
    }
}
