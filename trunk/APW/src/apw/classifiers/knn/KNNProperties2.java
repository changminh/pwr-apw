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

import apw.gui.property.Property;
import apw.gui.property.validation.Description;
import apw.gui.property.validation.FileMustExist;
import apw.gui.property.validation.FileSuffix;
import apw.gui.property.validation.FileSelectionMode;
import apw.gui.property.validation.Range;
import apw.gui.property.validation.Regex;
import java.io.File;

/**
 * Test property object.
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class KNNProperties2 {

    /**
     * An int property
     */
    @Range(max = 5, min = 1, message = "test")
    @Description(value="test property")
    Property<Integer> firstProperty = new Property();
    /**
     * REGEX validated name. Expression source:
     * <a href="http://examples.caspan.com/first_name_validate/">
     * this site</a>.
     */
    @Regex(value = "/^([a-z]+(\\'|-|\\.\\s|\\s)?[a-z]*){1,2}$/i",
    message = "Please enter your first name only. Please note sir " +
    "names are not part of a first name. If you look at your birth " +
    "certificate it does nor have Dr.,  Mr., Jr., or Miss. Please " +
    "only submit your first name in this field.")
    @Description(value = "Property field containing user's first name.")
    Property<String> firstName = new Property("Józek");
    /**
     * Source file in WEKA ARFF format.
     */
    @FileSelectionMode(directory = true)
    @FileMustExist(message = "Please select input file.")
    @Description("Input Sample file in WEKA ARFF file format.")
    @FileSuffix(values = {"arff"}, message = "Please select an ARFF file.")
    Property<File> sampleFile = new Property(new File("data/iris.arff"));
}
