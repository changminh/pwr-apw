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
package apw.gui.property.component;

import apw.gui.property.DuplicatedPropertyAnnotationException;
import apw.gui.property.AbstractPropertyComponent;
import apw.gui.property.PropertyAnnotationMismatchException;
import apw.gui.property.PropertyDescriptor;
import apw.gui.property.validation.Description;
import apw.gui.property.validation.FileMustExist;
import apw.gui.property.validation.FileSuffix;
import apw.gui.property.validation.NotDirectory;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class FileComponent extends AbstractPropertyComponent {

    JPanel fileComponentPane;
    JButton showFileSelectionDialog;
    JTextField pathTextField;
    File file = null;
    Action showDialogAction = new AbstractAction("...") {

        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(null);
            if (JFileChooser.APPROVE_OPTION == result) {
                File f = fc.getSelectedFile();
                if (f != null && validates(f)) {
                    pathTextField.setText(f.toString());
                    propertyChanged(true, file, f = file);
                }

            }
        }
    };
    // validation handling
    private boolean validateExistence = false;
    private Set<String> validationSuffixes = null;
    private boolean validateNotDirectory = false;
    private Object message;
    // validation messages
    private String notExistsMessage;
    private String wrongSuffixMessage;
    private String directoryMessage;

    private boolean validates(File f) {
        boolean validates = true;
        validates &= validate(validateExistence, f.exists(), notExistsMessage);
        validates &= validate(validateNotDirectory, f.isDirectory(), directoryMessage);

        if (validationSuffixes != null) {
            boolean validatedSuffix = false;
            for (String sfx : validationSuffixes)
                validatedSuffix |= f.getName().
                        endsWith(sfx);
            validates &= validate(true, validatedSuffix, wrongSuffixMessage);
        }

        return validates;
    }

    private boolean validate(boolean consider, boolean test, String message) {
        if (!consider) return true; // not consider so it's ok
        if (test) return true; // consider and passes so it's also ok

        // consider and not passed
        this.message = message;
        return false;
    }

    public JComponent getComponent() {
        if (fileComponentPane != null) return fileComponentPane;

        // object creation
        fileComponentPane = new JPanel();
        showFileSelectionDialog = new JButton(showDialogAction);
        pathTextField = new JTextField();

        // layout
        fileComponentPane.setLayout(new BorderLayout());
        fileComponentPane.add(pathTextField, BorderLayout.CENTER);
        fileComponentPane.add(showFileSelectionDialog, BorderLayout.EAST);

        return fileComponentPane;
    }

    public void configureValidAnnotationSet(
            Set<Class<? extends Annotation>> valid) {
        valid.add(FileMustExist.class);
        valid.add(FileSuffix.class);
        valid.add(NotDirectory.class);
    }

    public String noticeMessage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
