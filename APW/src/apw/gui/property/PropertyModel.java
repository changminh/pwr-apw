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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a bridge between property class' code and it's programatic
 * representation.
 * 
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class PropertyModel {

    public List<PropertyDescriptor> props;
    Object propertyObject;

    /**
     * Reflectively parses <code>propertyObject</code> to build programmatic
     * representation of properties.
     * 
     * @param propertyObject a property object from which properties are to
     *                       be extracted
     */
    PropertyModel(Object propertyObject) {
        // field initialization
        this.propertyObject = propertyObject;

        props = new ArrayList();

        // property extraction
        Class clazz = propertyObject.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getType().
                    getClass().
                    equals(Property.class.getClass()))
                try {
                    field.setAccessible(true);
                    PropertyDescriptor pd = new PropertyDescriptor();
                    pd.anns = field.getAnnotations();
                    pd.name = field.getName();
                    pd.prop = (Property) field.get(propertyObject);
                    pd.clazz = getTypeClass(field);
                    props.add(pd);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(PropertyModel.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(PropertyModel.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
        }
    }

    private static Class getTypeClass(Field field) {
        Class clazz = null;
        Type genericFieldType = field.getGenericType();
        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            for (Type fieldArgType : fieldArgTypes)
                clazz = (Class) fieldArgType;
        }
        return clazz;
    }
}


    
