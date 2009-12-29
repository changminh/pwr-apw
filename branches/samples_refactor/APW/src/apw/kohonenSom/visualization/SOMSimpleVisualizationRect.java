package apw.kohonenSom.visualization;

import apw.kohonenSom.logic.SOMKohonenMap;
import apw.kohonenSom.patterns.SOMSamplesLoader;
import java.awt.image.BufferedImage;

/**
 *
 * @author Christopher Wadowski
 */
public class SOMSimpleVisualizationRect implements SOMVisualization{

    @Override
    public BufferedImage generateFeatMap(
            SOMKohonenMap network, int feat, SOMSamplesLoader samples) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BufferedImage generateVoronoiMap(
            SOMKohonenMap network, SOMSamplesLoader samples) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BufferedImage generatePatternDensityMap(
            SOMKohonenMap network, SOMSamplesLoader samples) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BufferedImage generateClusterMap(
            SOMKohonenMap network, SOMSamplesLoader samples) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BufferedImage generateUMap(
            SOMKohonenMap network, SOMSamplesLoader samples) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
