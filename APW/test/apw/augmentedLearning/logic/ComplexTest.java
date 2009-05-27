package apw.augmentedLearning.logic;

import java.util.HashSet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static apw.augmentedLearning.logic.RelationBetweenSets.*;
import static apw.augmentedLearning.logic.SelectorForNominal.*;
import static apw.augmentedLearning.logic.SelectorForNumber.*;

/**
 *
 * @author Nitric
 */
public class ComplexTest {

    private Complex instance, other;

    private static String[][] elements = new String[][] {
        {"Alicja", "ma", "kota", "Milicja", "ma", "pałę"},                      // 0
        {"Alicja", "ma", "kota", "pałę"},                                       // 1
        {"ma", "kota"},                                                         // 2
        {"ma", "pałę"},                                                         // 3
        {"Alicja", "Milicja"},                                                  // 4
        {"Alicja", "ma", "kota", "Milicja", "ma", "pałę", "!", "?"},            // 5
        {"Alicja", "ma", "Szabadabada"},                                        // 6
        {""},                                                                   // 7
        {},                                                                     // 8
    };

    private static HashSet<String>[] sets = new HashSet[elements.length];

    @BeforeClass
    public static void setUpClass() {
        for (int i = 0; i < sets.length; i++) {
            sets[i] = new HashSet<String>();
            for (String s : elements[i])
                sets[i].add(s);
        }
    }
    
    @Before
    public void before() {
        instance = new Complex();
        other = new Complex();
    }

    /**
     * All the selectors in 'other' are less general. None selector in 'instance' and 'other'
     * is universal.
     */
    @Test
    public void testGenerality__Nominal_1() {
        instance.addSelector(getSelSet(0, false, sets[0]));
        instance.addSelector(getSelSet(1, false, sets[1]));
        instance.addSelector(getSelSet(2, false, sets[5]));
        other.addSelector(getSelSet(0, false, sets[1]));
        other.addSelector(getSelSet(1, false, sets[3]));
        other.addSelector(getSelSet(2, false, sets[0]));
        assertEquals(MORE_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(LESS_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * All the selectors in 'other' are less general. All selectors in 'instance' are universal.
     */
    @Test
    public void testGenerality__Nominal_2() {
        instance.addSelector(getSelUniversal(0, sets[5]));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelUniversal(2, sets[5]));
        other.addSelector(getSelSet(0, false, sets[4]));
        other.addSelector(getSelSet(1, false, sets[3]));
        other.addSelector(getSelSet(2, false, sets[0]));
        assertEquals(MORE_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(LESS_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * All the selectors in 'other' are less general. One selector in 'instance' is universal.
     */
    @Test
    public void testGenerality__Nominal_3() {
        instance.addSelector(getSelSet(0, false, sets[0]));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelUniversal(2, sets[5]));
        other.addSelector(getSelSet(0, false, sets[4]));
        other.addSelector(getSelSet(1, false, sets[3]));
        other.addSelector(getSelSet(2, false, sets[0]));
        assertEquals(MORE_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(LESS_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * All the selectors in 'other' are less general. Both 'instance' and 'other' contain
     * universal Selectors.
     */
    @Test
    public void testGenerality__Nominal_4() {
        instance.addSelector(getSelSet(0, false, sets[0]));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelUniversal(2, sets[5]));
        other.addSelector(getSelSet(0, false, sets[4]));
        other.addSelector(getSelSet(1, false, sets[3]));
        other.addSelector(getSelUniversal(2, sets[5]));
        assertEquals(MORE_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(LESS_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * All the selectors in 'other' are less general. Both 'instance' and 'other' contain
     * empty Selectors.
     */
    @Test
    public void testGenerality__Nominal_5() {
        instance.addSelector(getSelSet(0, false, sets[0]));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelEmpty(2));
        other.addSelector(getSelSet(0, false, sets[4]));
        other.addSelector(getSelSet(1, false, sets[3]));
        other.addSelector(getSelEmpty(2));
        assertEquals(MORE_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(LESS_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * All the selectors in 'other' are less general. 'other' contains an empty Selector.
     */
    @Test
    public void testGenerality__Nominal_6() {
        instance.addSelector(getSelSet(0, false, sets[0]));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelSet(2, false, sets[3]));
        other.addSelector(getSelSet(0, false, sets[4]));
        other.addSelector(getSelSet(1, false, sets[3]));
        other.addSelector(getSelEmpty(2));
        assertEquals(MORE_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(LESS_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * All the selectors in 'other' are less general. 'instance' contains only universal selectors,
     * 'other' contains only empty selectors.
     */
    @Test
    public void testGenerality__Nominal_7() {
        instance.addSelector(getSelUniversal(0, sets[5]));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelUniversal(2, sets[5]));
        other.addSelector(getSelEmpty(0));
        other.addSelector(getSelEmpty(1));
        other.addSelector(getSelEmpty(2));
        assertEquals(MORE_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(LESS_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * All selectors (and complexes too) are equal. Both complexes contains only universal selectors.
     */
    @Test
    public void testGenerality__Nominal_8() {
        instance.addSelector(getSelUniversal(0, sets[5]));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelUniversal(2, sets[5]));
        other.addSelector(getSelUniversal(0, sets[5]));
        other.addSelector(getSelUniversal(1, sets[5]));
        other.addSelector(getSelUniversal(2, sets[5]));
        assertEquals(EQUAL, instance.isMoreGeneralThan(other));
        assertEquals(EQUAL, other.isMoreGeneralThan(instance));
    }

    /**
     * Complexes are equal. Both complexes contains only empty selectors.
     */
    @Test
    public void testGenerality__Nominal_9() {
        instance.addSelector(getSelEmpty(0));
        instance.addSelector(getSelEmpty(1));
        instance.addSelector(getSelEmpty(2));

        other.addSelector(getSelEmpty(0));
        other.addSelector(getSelEmpty(1));
        other.addSelector(getSelEmpty(2));
        assertEquals(EQUAL, instance.isMoreGeneralThan(other));
        assertEquals(EQUAL, other.isMoreGeneralThan(instance));
    }

    /**
     * Complexes are equal. Both complexes contains only empty selectors, but notice, that some selectors
     * are in fact 'setSelectosrs', but theirs set of values are empty in fact.
     */
    @Test
    public void testGenerality__Nominal_10() {
        instance.addSelector(getSelEmpty(0));
        instance.addSelector(getSelSet(1, false, sets[8]));
        instance.addSelector(getSelEmpty(2));

        other.addSelector(getSelSet(0, false, sets[8]));
        other.addSelector(getSelEmpty(1));
        other.addSelector(getSelSet(2, false, sets[8]));
        assertEquals(EQUAL, instance.isMoreGeneralThan(other));
        assertEquals(EQUAL, other.isMoreGeneralThan(instance));
    }

    /**
     * Complexes are equal.
     */
    @Test
    public void testGenerality__Nominal_11() {
        instance.addSelector(getSelEmpty(0));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelSet(2, false, sets[0]));
        instance.addSelector(getSelSet(3, false, sets[1]));
        instance.addSelector(getSelSet(4, false, sets[2]));
        instance.addSelector(getSelSet(5, false, sets[3]));

        other.addSelector(getSelEmpty(0));
        other.addSelector(getSelUniversal(1, sets[5]));
        other.addSelector(getSelSet(2, false, sets[0]));
        other.addSelector(getSelSet(3, false, sets[1]));
        other.addSelector(getSelSet(4, false, sets[2]));
        other.addSelector(getSelSet(5, false, sets[3]));
        assertEquals(EQUAL, other.isMoreGeneralThan(instance));
    }

    /**
     * Complexes are not comparable. Both contain universal and empty selectors, but not at the same
     * positions.
     */
    @Test
    public void testGenerality__Nominal_12() {
        instance.addSelector(getSelEmpty(0));
        instance.addSelector(getSelSet(1, false, sets[8]));
        instance.addSelector(getSelUniversal(2, sets[5]));

        other.addSelector(getSelUniversal(0, sets[5]));
        other.addSelector(getSelSet(1, false, sets[8]));
        other.addSelector(getSelEmpty(2));

        assertEquals(NOT_COMPARABLE, instance.isMoreGeneralThan(other));
        assertEquals(NOT_COMPARABLE, other.isMoreGeneralThan(instance));
    }

    /**
     * Complexes are not comparable. Both contain universal selectors, but not at the same
     * positions and there are also selectors, that makes complexes incomparable.
     */
    @Test
    public void testGenerality__Nominal_13() {
        instance.addSelector(getSelSet(0, false, sets[5]));
        instance.addSelector(getSelSet(1, false, sets[1]));
        instance.addSelector(getSelUniversal(2, sets[5]));

        other.addSelector(getSelUniversal(0, sets[5]));
        other.addSelector(getSelSet(1, false, sets[3]));
        other.addSelector(getSelUniversal(2, sets[5]));

        assertEquals(NOT_COMPARABLE, instance.isMoreGeneralThan(other));
        assertEquals(NOT_COMPARABLE, other.isMoreGeneralThan(instance));
    }

    /**
     * Complexes are not comparable. Both contain empty selectors, but not at the same
     * positions and there are also selectors, that makes complexes incomparable.
     */
    @Test
    public void testGenerality__Nominal_14() {
        instance.addSelector(getSelSet(0, false, sets[5]));
        instance.addSelector(getSelSet(1, false, sets[3]));
        instance.addSelector(getSelUniversal(2, sets[5]));

        other.addSelector(getSelEmpty(0));
        other.addSelector(getSelSet(1, false, sets[1]));
        other.addSelector(getSelUniversal(2, sets[5]));

        assertEquals(NOT_COMPARABLE, instance.isMoreGeneralThan(other));
        assertEquals(NOT_COMPARABLE, other.isMoreGeneralThan(instance));
    }

    /**
     * One selector in 'other' is more general.
     */
    @Test
    public void testGenerality__Nominal_15() {
        instance.addSelector(getSelEmpty(0));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelSet(2, false, sets[0]));
        instance.addSelector(getSelSet(3, false, sets[1]));
        instance.addSelector(getSelSet(4, false, sets[2]));
        instance.addSelector(getSelSet(5, false, sets[3]));

        other.addSelector(getSelSet(0, false, sets[0]));
        other.addSelector(getSelUniversal(1, sets[5]));
        other.addSelector(getSelSet(2, false, sets[0]));
        other.addSelector(getSelSet(3, false, sets[1]));
        other.addSelector(getSelSet(4, false, sets[2]));
        other.addSelector(getSelSet(5, false, sets[3]));

        assertEquals(LESS_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(MORE_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * One selector in 'other' is more general.
     */
    @Test
    public void testGenerality__Nominal_16() {
        instance.addSelector(getSelEmpty(0));
        instance.addSelector(getSelSet(1, false, sets[0]));
        instance.addSelector(getSelSet(2, false, sets[0]));
        instance.addSelector(getSelSet(3, false, sets[1]));
        instance.addSelector(getSelSet(4, false, sets[2]));
        instance.addSelector(getSelSet(5, false, sets[3]));

        other.addSelector(getSelEmpty(0));
        other.addSelector(getSelUniversal(1, sets[5]));
        other.addSelector(getSelSet(2, false, sets[0]));
        other.addSelector(getSelSet(3, false, sets[1]));
        other.addSelector(getSelSet(4, false, sets[2]));
        other.addSelector(getSelSet(5, false, sets[3]));

        assertEquals(LESS_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(MORE_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * One selector in 'other' is more general.
     */
    @Test
    public void testGenerality__Nominal_17() {
        instance.addSelector(getSelSet(0, false, sets[0]));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelSet(2, false, sets[0]));
        instance.addSelector(getSelSet(3, false, sets[1]));
        instance.addSelector(getSelSet(4, false, sets[2]));
        instance.addSelector(getSelSet(5, false, sets[3]));

        other.addSelector(getSelSet(0, false, sets[0]));
        other.addSelector(getSelUniversal(1, sets[5]));
        other.addSelector(getSelSet(2, false, sets[5]));
        other.addSelector(getSelSet(3, false, sets[1]));
        other.addSelector(getSelSet(4, false, sets[2]));
        other.addSelector(getSelSet(5, false, sets[3]));

        assertEquals(LESS_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(MORE_GENERAL, other.isMoreGeneralThan(instance));
    }

    /**
     * One selector in 'other' is more general.
     */
    @Test
    public void testGenerality__Nominal_18() {
        instance.addSelector(getSelSet(0, false, sets[0]));
        instance.addSelector(getSelUniversal(1, sets[5]));
        instance.addSelector(getSelSet(2, false, sets[0]));
        instance.addSelector(getSelSet(3, false, sets[1]));
        instance.addSelector(getSelSet(4, false, sets[5]));
        instance.addSelector(getSelSet(5, false, sets[0]));

        other.addSelector(getSelSet(0, false, sets[0]));
        other.addSelector(getSelUniversal(1, sets[5]));
        other.addSelector(getSelSet(2, false, sets[0]));
        other.addSelector(getSelSet(3, false, sets[1]));
        other.addSelector(getSelSet(4, false, sets[0]));
        other.addSelector(getSelSet(5, false, sets[5]));

        assertEquals(NOT_COMPARABLE, instance.isMoreGeneralThan(other));
        assertEquals(NOT_COMPARABLE, other.isMoreGeneralThan(instance));
    }

    /*****************************************************************************************/
    /**********************************   Now for numbers   **********************************/
    /*****************************************************************************************/

    /**
     * 'instance' is more general - it contains only universal selectors, while 'other' has
     * various range selectors.
     */
    @Test
    public void testGenerality__Number_1() {
        instance.addSelector(getUniversalSelector(0));
        instance.addSelector(getUniversalSelector(1));
        instance.addSelector(getUniversalSelector(2));
        instance.addSelector(getUniversalSelector(3));
        instance.addSelector(getUniversalSelector(4));
        instance.addSelector(getUniversalSelector(5));

        other.addSelector(getSelBelongs(0, -100, 100));
        other.addSelector(getEmptySelector(1));
        other.addSelector(getSelEQ(2, 5));
        other.addSelector(getSelGT(3, 5));
        other.addSelector(getSelLE(4, 9));
        other.addSelector(getUniversalSelector(5));

        assertEquals(MORE_GENERAL, instance.isMoreGeneralThan(other));
    }
    
    @Test
    public void testGenerality__Number_2() {
        instance.addSelector(getSelBelongs(0, -100, 100));
        instance.addSelector(getSelLE(1, 200));
        instance.addSelector(getSelGT(2, -200));

        other.addSelector(getSelBelongs(0, -80, 80));
        other.addSelector(getSelLE(1, 100));
        other.addSelector(getSelGT(2, -100));

        assertEquals(MORE_GENERAL, instance.isMoreGeneralThan(other));
        assertEquals(LESS_GENERAL, other.isMoreGeneralThan(instance));
    }

    @Test
    public void testGenerality__Number_3() {
        instance.addSelector(getSelBelongs(0, -100, 100));
        instance.addSelector(getSelLE(1, 200));
        instance.addSelector(getSelGT(2, -200));

        other.addSelector(getSelBelongs(0, -80, 80));
        other.addSelector(getSelLE(1, 100));
        other.addSelector(getSelGT(2, -300));

        assertEquals(NOT_COMPARABLE, instance.isMoreGeneralThan(other));
        assertEquals(NOT_COMPARABLE, other.isMoreGeneralThan(instance));
    }   
}