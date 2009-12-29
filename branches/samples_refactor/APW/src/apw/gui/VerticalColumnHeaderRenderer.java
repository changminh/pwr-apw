package apw.gui;

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
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


/**
 *
 * @author Juanjo Vega
 */
public class VerticalColumnHeaderRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        // Header rendering
        TableCellRenderer renderer = table.getTableHeader().getDefaultRenderer();

        final Component component = renderer.getTableCellRendererComponent(
                table, "", false, false, -1, column);

        component.setBounds(0, 0,
                table.getTableHeader().getWidth(),
                table.getTableHeader().getHeight());

        JLabel label = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {
                component.paint(g);
                super.paintComponent(g);
            }

        };
        Icon icon = VerticalCaption.getVerticalCaption(label, value.toString(), false);

        label.setIcon(icon);
        label.setVerticalAlignment(JLabel.BOTTOM);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
}
class VerticalCaption {

    static Icon getVerticalCaption(JComponent component, String caption, boolean clockwise) {
        Font f = component.getFont();
        FontMetrics fm = component.getFontMetrics(f);
        int captionHeight = fm.getHeight();
        int captionWidth = fm.stringWidth(caption);
        BufferedImage bi = new BufferedImage(captionHeight + 2,
                captionWidth + 12, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();

        g.setColor(new Color(0, 0, 0, 0)); // transparent
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

        g.setColor(component.getForeground());
        g.setFont(f);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (clockwise)
            g.rotate(Math.PI / 2);
        else {
            g.rotate(-Math.PI / 2);
            g.translate(-bi.getHeight(), bi.getWidth());
        }
        g.drawString(caption, 6, -6);

        Icon icon = new ImageIcon(bi);
        return icon;
    }
}
