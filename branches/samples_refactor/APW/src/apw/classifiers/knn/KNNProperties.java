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
package apw.classifiers.knn;

import net.java.dev.properties.BaseBean;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.annotations.Length;
import net.java.dev.properties.annotations.Range;
import net.java.dev.properties.annotations.Regex;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableIndexed;
import net.java.dev.properties.container.ObservableProperty;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class KNNProperties extends BaseBean {

    @Length(min = 5, max = 12, message = "Name must be between 5 and 12 characters")
    @Regex(exp = "[a-zA-Z]*", message = "Name must be comprized of alpha characters only")
    public final Property<String> name = ObservableProperty.create("testname");
    @Range(min = 0, max = 200, message = "Age must within reasonable bounds")
    public final Property<Double> age = ObservableProperty.create(0.0);
    @Length(min = 1, max = 255, message = "Description must exist and must not exceed 255 characters")
    public final Property<String> description = ObservableProperty.create("");
    @Length(min = 2, max = 5, message = "Must select at least 2 entries and no more than 5")
    public final IndexedProperty<Integer> selection = ObservableIndexed.create();
    public final IndexedProperty<String> values = ObservableIndexed.create(
            "BBB", "CCC", "DDD", "EEE", "FFF", "GGG");
    public final Property<enumTest> enumTest = ObservableProperty.create();

    public enum enumTest {

        a, b
    };

    /** Creates a new instance of ValidationBean */
    public KNNProperties() {
        BeanContainer.bind(this);
    }
}
