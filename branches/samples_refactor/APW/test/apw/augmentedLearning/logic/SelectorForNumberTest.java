package apw.augmentedLearning.logic;

import org.junit.Test;
import static org.junit.Assert.*;
import static apw.augmentedLearning.logic.SelectorForNumber.*;
import static apw.augmentedLearning.logic.RelationBetweenSets.*;

/**
 *
 * @author Nitric
 */
public class SelectorForNumberTest {

    private SelectorForNumber inst, oth;

    @Test
    public void testIsMoreGeneralThan_forUniversalSelector() {
        inst = getUniversalSelector(0);
        oth = getUniversalSelector(0);
        assertEquals(EQUAL, inst.isMoreGeneralThan(oth));
        assertEquals(EQUAL, oth.isMoreGeneralThan(inst));

        oth = getEmptySelector(0);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelBelongs(0, -3, 3);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelEQ(0, 5);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, -50);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 100);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));
    }       

    @Test
    public void testIsMoreGeneralThan_forRangeSelector() {
        inst = getSelBelongs(0, -10, 10);

        // Range selector 'x' Universal selector
        oth = getUniversalSelector(0);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        // Range selector 'x' Empty selector
        oth = getEmptySelector(0);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        // Range selector 'x' Range selector
        oth = getSelBelongs(0, -15, 15);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelBelongs(0, -15, 10);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelBelongs(0, -15, 8);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelBelongs(0, -10, 15);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelBelongs(0, -10, 10);
        assertEquals(EQUAL, inst.isMoreGeneralThan(oth));
        assertEquals(EQUAL, oth.isMoreGeneralThan(inst));

        oth = getSelBelongs(0, -10, 8);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelBelongs(0, -8, 15);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelBelongs(0, -8, 10);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelBelongs(0, 8, 8);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        // Range selector 'x' Equals selector

        oth = getSelEQ(0, -12);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelEQ(0, -10);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelEQ(0, -2);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelEQ(0, 10);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelEQ(0, 12);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        // Range selector 'x' LowerOrEqual selector
        oth = getSelLE(0, -12);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, -10);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 0);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 10);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 12);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));
                
        // Range selector 'x' GreaterThan selector
        oth = getSelGT(0, -12);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, -10);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, 0);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, 10);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, 12);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));
    }
    
    @Test
    public void testIsMoreGeneralThan_forEmptySelector() {
        inst = getEmptySelector(0);
        oth = getEmptySelector(0);
        
        assertEquals(EQUAL, inst.isMoreGeneralThan(oth));
        assertEquals(EQUAL, oth.isMoreGeneralThan(inst));
        
        /*
        assertEquals(, inst.isMoreGeneralThan(oth));
        assertEquals(, oth.isMoreGeneralThan(inst));
        */
    }

    @Test
    public void testIsMoreGeneralThan_forLESelector() {
        inst = getSelLE(0, 10);
        oth = getEmptySelector(0);

        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, 0);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 8);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 10);
        assertEquals(EQUAL, inst.isMoreGeneralThan(oth));
        assertEquals(EQUAL, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 12);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));
    }

    @Test
    public void testIsMoreGeneralThen_forGTSelector() {
        inst = getSelGT(0, 10);

        oth = getEmptySelector(0);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 15);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, 8);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, 10);
        assertEquals(EQUAL, inst.isMoreGeneralThan(oth));
        assertEquals(EQUAL, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, 12);
        assertEquals(MORE_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(LESS_GENERAL, oth.isMoreGeneralThan(inst));
    }

    @Test
    public void testIsMoreGeneralThen_forEQSelector() {
        inst = getSelEQ(0, 0);

        // EQSelector 'x' LESelector
        oth = getSelLE(0, -10);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 0);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelLE(0, 10);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        // EQSelector 'x' GTSelector
        oth = getSelGT(0, -10);
        assertEquals(LESS_GENERAL, inst.isMoreGeneralThan(oth));
        assertEquals(MORE_GENERAL, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, 0);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));

        oth = getSelGT(0, 10);
        assertEquals(NOT_COMPARABLE, inst.isMoreGeneralThan(oth));
        assertEquals(NOT_COMPARABLE, oth.isMoreGeneralThan(inst));
    }
}