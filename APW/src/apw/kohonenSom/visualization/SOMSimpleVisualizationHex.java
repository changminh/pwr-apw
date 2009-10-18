package apw.kohonenSom.visualization;

import apw.kohonenSom.logic.SOMKohonenMap;
import apw.kohonenSom.patterns.SOMSamplesLoader;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Christopher Wadowski
 */
public class SOMSimpleVisualizationHex implements SOMVisualization{
    private int hexX;
    private int hexY;
    private int dotSize;

    public SOMSimpleVisualizationHex(){
        hexX = 15;
        hexY = 23;
        dotSize = 5;
    }

    @Override
    public BufferedImage generateVoronoiMap(
            SOMKohonenMap network, SOMSamplesLoader samples){
        int
                x = network.getXMax(),
                y = network.getYMax();

        Color[][] colors = new Color[x][];

        for(int ix=0; ix<x; ix++)
            colors[ix] = new Color[y];

        //TODO

        return paintMap(colors, x, y, 1);
    }

    @Override
    public BufferedImage generateFeatMap(
            SOMKohonenMap network, int feat, SOMSamplesLoader samples){
        int
                x = network.getXMax(),
                y = network.getYMax();

        Color[][] colors = new Color[x][];

        for(int ix=0; ix<x; ix++)
            colors[ix] = new Color[y];

        double[][][] weights = network.getWeights();

        double minVal, maxVal, val;
        minVal = maxVal = weights[0][0][feat];

        for(int ix=0; ix<x; ix++){
            for(int iy=0; iy<y; iy++){
                val = weights[ix][iy][feat];

                if(val < minVal){
                    minVal = val;
                }
                if(val > maxVal){
                    maxVal = val;
                }
            }
        }

        int r,g,b;
        double amp = maxVal - minVal;

        for(int ix=0; ix<x; ix++){
            for(int iy=0; iy<y; iy++){
                val = weights[ix][iy][feat];
                r = (int)(((val-minVal)/amp)*255);
                b = 255 - r;
                colors[ix][iy] = new Color(r, 0, b);
            }
        }

        return paintMap(colors, x, y, 1);
    }

    @Override
    public BufferedImage generatePatternDensityMap(
            SOMKohonenMap network, SOMSamplesLoader samples){
        int
                x = network.getXMax(),
                y = network.getYMax();

        Color[][] colors = new Color[x][];

        for(int ix=0; ix<x; ix++)
            colors[ix] = new Color[y];

        ArrayList<Integer>[][] patternsMap = network.getMap();

        double minVal, maxVal, val;
        minVal = maxVal = patternsMap[0][0].size();

        for(int ix=0; ix<x; ix++){
            for(int iy=0; iy<y; iy++){
                val = patternsMap[ix][iy].size();

                if(val < minVal){
                    minVal = val;
                }
                if(val > maxVal){
                    maxVal = val;
                }
            }
        }

        int r,g,b;
        double amp = maxVal - minVal;

        for(int ix=0; ix<x; ix++){
            for(int iy=0; iy<y; iy++){
                val = patternsMap[ix][iy].size();
                r = (int)(((val-minVal)/amp)*255);
                b = 255 - r;
                colors[ix][iy] = new Color(r, 0, b);
            }
        }

        return paintMap(colors, x, y, 1);
    }

    @Override
    public BufferedImage generateClassMap(
            SOMKohonenMap network, SOMSamplesLoader samples){
        int
                x = network.getXMax(),
                y = network.getYMax();

        Color[][] colors = new Color[x][];

        for(int ix=0; ix<x; ix++)
            colors[ix] = new Color[y];

        //TODO

        return paintMap(colors, x, y, 1);
    }

    @Override
    public BufferedImage generateUMap(
            SOMKohonenMap network, SOMSamplesLoader samples){
        double[][] uMapValues = network.generateUMap();

        int
                xm = network.getXMax()*2 - 1,
                ym = network.getYMax()*2 - 1,
                x = network.getXMax(),
                y = network.getYMax();

        Color[][] colors = new Color[xm][];

        for(int ix=0; ix<xm; ix++)
            colors[ix] = new Color[ym];

        double minDist = uMapValues[0][0];
        double maxDist = uMapValues[0][0];

        for(int ix=0; ix<uMapValues.length; ix++){
            for(int iy=0; iy<uMapValues[0].length; iy++){
                if(minDist > uMapValues[ix][iy]){
                    minDist = uMapValues[ix][iy];
                }
                if(maxDist < uMapValues[ix][iy]){
                    maxDist = uMapValues[ix][iy];
                }
            }
        }

        for(int ix=0; ix<colors.length; ix++){
            for(int iy=0; iy<colors[0].length; iy++){
                double dist = uMapValues[ix][iy];
                colors[ix][iy] =
                        getUMapColor(minDist, maxDist, dist);
            }
        }

        BufferedImage map;
        map = paintMap(colors, xm, ym, 2);
        map = paintDotsOnMap(map, x, y, 2, 2);
        return map;
    }

    //------------------------------------------------
    private static Color getUMapColor(
            double minDist, double maxDist, double dist){
        double fa, fb, c;
        
        fa = 255.0/(maxDist - minDist);
        fb = (-255.0*minDist)/(maxDist - minDist);
         
        c = fa*dist + fb;

        int color = 255 - (int)java.lang.Math.round(c);

        return new Color(color,color,color);
    }

    private BufferedImage paintMap(
            Color[][] colors, int x, int y, int shift){
        int xp = x*hexX;
        int yp = (y-1)*(hexY/2 + hexY/4) + hexY;

        BufferedImage map =
                new BufferedImage(xp, yp, BufferedImage.TYPE_INT_ARGB);

        Point[][] hexCenters = findHex(x, y, shift);

        map = paintHexes(map, hexCenters, colors);

        return map;
    }

    private BufferedImage paintDotsOnMap(
            BufferedImage map, int x, int y, int step, int shift){
        
        ArrayList<Point> dotsCenters = findDots(x, y, step, shift);

        map = paintDots(map, dotsCenters);

        return map;
    }
    
    private ArrayList<Point> findDots(
            int x, int y, int step, int shift) {
        ArrayList<Point> neurons = new ArrayList<Point>();

        int rx = this.hexX/2;
        int ry = this.hexY/2;

        for(int iy=0, miy=ry, mix; iy<y; iy++){
            int s = 1 + shift;

            if(iy%2==0){
                mix = rx;
            }else{
                mix = s*rx;
            }

            for(int ix=0; ix<x; ix++){
                neurons.add(new Point(mix, miy));
                mix += (2*rx)*step;
            }
            miy+=(ry +ry/2)*step;
        }

        return neurons;
    }

    private Point[][] findHex(int x, int y, int shift) {
        Point[][] hexCenters = new Point[x][];

        int rx = this.hexX/2;
        int ry = this.hexY/2;

        for(int ix=0; ix<x; ix++)
            hexCenters[ix] = new Point[y];

        int mix, miy, offset;

        offset = 0;
        mix = rx;

        for(int ix=0; ix<x; ix++){
            miy = ry;

            int s = 0;
            boolean toRight = true;

            for(int iy=0; iy<y; iy++){
                offset = rx*s;

                hexCenters[ix][iy] = new Point(mix+offset, miy);

                miy += ry + ry/2;

                if(s<shift && toRight)
                    s++;
                if(s>0 && !toRight)
                    s--;
                if(s==0)
                    toRight=true;
                if(s==shift)
                    toRight=false;
            }

            mix += rx*2;
        }
        
        return hexCenters;
    }

    private BufferedImage paintHexes(
            BufferedImage map, Point[][] hexCenters, Color[][] colors) {

        for(int ix=0; ix<colors.length; ix++){
            for(int iy=0; iy<colors[0].length; iy++){
                int hx, hy;

                hx = hexCenters[ix][iy].x;
                hy = hexCenters[ix][iy].y;

                Color c = colors[ix][iy];
                map = paintHex(map, hx, hy, c);
            }
        }

        return map;
    }

    private BufferedImage paintHex(
            BufferedImage image, int hx, int hy, Color c) {
        int xr = this.hexX/2;
        int yr = this.hexY/2;

        int r = 0;

        for(int iy=hy-yr; iy<hy; iy++){
            for(int ix=hx-r; ix<=hx+r; ix++){
                if(ix>=0 && iy>=0 &&
                            ix<image.getWidth() && iy<image.getHeight()){
                    image.setRGB(
                            ix,
                            iy,
                            c.getRGB());
                }
            }
            if(r<xr)
                r++;
        }

        r = 0;

        for(int iy=hy+yr; iy>=hy; iy--){
            for(int ix=hx-r; ix<=hx+r; ix++){
                if(ix>=0 && iy>=0 &&
                            ix<image.getWidth() && iy<image.getHeight()){
                    image.setRGB(
                            ix,
                            iy,
                            c.getRGB());
                }
            }
            if(r<xr)
                r++;
        }

        return image;
    }

    private BufferedImage paintDots(BufferedImage image, ArrayList<Point> dots){
        for(int di=0; di<dots.size(); di++){
            int
                    x = dots.get(di).x,
                    y = dots.get(di).y;

            int dotr = dotSize/2;
            int r = 0;

            int actualColor = image.getRGB(x, y);
            Color comparatorColor = new Color(100,100,100);
            Color c = Color.BLACK;
            if(actualColor<comparatorColor.getRGB())
                c = Color.white;

            for(int ix=x-dotr; ix<x; ix++, r++){
                for(int iy=y-r; iy<=y+r; iy++){
                    if(ix>=0 && iy>=0 &&
                            ix<image.getWidth() && iy<image.getHeight()){
                        image.setRGB(
                                ix,
                                iy,
                                c.getRGB());
                    }
                }
            }

            for(int ix=x; ix<=x+dotr; ix++, r--){
                for(int iy=y-r; iy<=y+r; iy++){
                    if(ix>=0 && iy>=0 &&
                            ix<image.getWidth() && iy<image.getHeight()){
                        image.setRGB(
                                ix,
                                iy,
                                c.getRGB());
                    }
                }
            }
        }

        return image;
    }
}
