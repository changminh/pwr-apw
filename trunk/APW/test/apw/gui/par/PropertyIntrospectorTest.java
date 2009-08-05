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
package apw.gui.par;

import apw.gui.par.validation.Range;
import java.util.Arrays;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class PropertyIntrospectorTest {

    public PropertyIntrospectorTest() {
    }
    static Object toIntrospect;

    @BeforeClass
    public static void setUpClass() throws Exception {
        /************************************************************/
        /*                 Introspected test class                  */
        /************************************************************/
        toIntrospect = new Object() {

            @Range(min = 0, max = 5, message = "0..5")
            Property<Integer> firstProperty = new Property<Integer>(5);
        };
        /************************************************************/
    }

    /**
     * Test of listProperties method, of class PropertyModel.
     */
    @Test
    public void testlistPropertyFields() {
        System.out.println("listProperties");
        PropertyDescriptor instance = new PropertyModel(toIntrospect).props.get(0);
        System.out.println("Properties:  " + instance.prop);
        System.out.println("Names:       " + instance.name);
        //assertEquals(expResult, result.get(0).getName());
    }
}
