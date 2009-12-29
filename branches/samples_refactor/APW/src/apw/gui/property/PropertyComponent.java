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

import javax.swing.JComponent;

/**
 *
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public interface PropertyComponent {

    /**
     * Use this method to provide GUI builder appropriate swing
     * component which will be used to display and interact with
     * user.
     *
     * @return swing component
     */
    abstract public JComponent getComponent();

    /**
     * GUI builder implemenetation uses this class to handle
     * user input feedback.
     * @param listener
     */
    abstract public void registerListener(IPropertyChangeListener listener);

    /**
     * Property Component is expected to handle specific annotations.
     *
     * @param annotations to handle
     * @throws PropertyAnnotationMismatchException if annotations contains
     * unhandled element
     */
    abstract public void initialize(PropertyDescriptor desc)
            throws PropertyAnnotationMismatchException;

    /**
     * <p>Plain or html text explaining property meaning, e.g. "Value
     * must be in range [0, 1] inclusively. Low values are recommended
     * due to low performance penalty, but higher values might be necessary
     * to discover all solutions. Typically values lower than 0.2 suffice."
     * </p>
     *
     * <p>This method will be called every time a property component will
     * receive focus. Implementation might look like: </p>
     *
     * <pre>
     * public String noticeMessage() {
     *    return "A number of trees in your garden.";
     * }
     * </pre>
     *
     * @return
     */
    public String noticeMessage();
}
