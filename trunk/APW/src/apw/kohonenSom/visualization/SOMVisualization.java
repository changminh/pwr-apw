package apw.kohonenSom.visualization;

import apw.kohonenSom.logic.SOMKohonenMap;
import apw.kohonenSom.patterns.SOMSamplesLoader;
import java.awt.image.BufferedImage;

/**
 *
 * @author Christopher Wadowski
 */
public interface SOMVisualization {
    public BufferedImage generateFeatMap(SOMKohonenMap network, int feat,
            SOMSamplesLoader samples);

    public BufferedImage generateVoronoiMap(SOMKohonenMap network,
            SOMSamplesLoader samples);

    public BufferedImage generatePatternDensityMap(SOMKohonenMap network,
            SOMSamplesLoader samples);

    public BufferedImage generateClassMap(SOMKohonenMap network,
            SOMSamplesLoader samples);

    public BufferedImage generateUMap(SOMKohonenMap network,
            SOMSamplesLoader samples);
}
