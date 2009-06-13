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

package apw.classifiers.fuzzyRuleClassifier;

import java.util.Random;

/**
 *
 * @author przemo
 */
public class RandomClass {
    private static double min = 0.0;
    private static double max = 0.0;
    private static Random rand = new Random();
   
    public static void setMin(double _m){min=_m;}
    public static void setMax(double _m){max=_m;}
    public static double getMin(){return min;}
    public static double getMax(){return max;}

    public static double rDouble(){
        double range = max - min;
        return rand.nextDouble()*(rand.nextDouble() < 0.5?(range/10.0):(-range/10.0));
    }

    public static double nextDouble(){
        return rand.nextDouble();
    }

    public static int nextInt(int val,int range){
        return (val + rand.nextInt(range - 1) + 1) % range;
    }

    public static boolean nextBoolean(){return rand.nextBoolean();}

    public static void reset(){
        rand = new Random(System.currentTimeMillis() + 
                          System.nanoTime() +
                          (int)(10000*Math.random()));
    }

}
