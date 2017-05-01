import Model.Addresses.TenarySearchTrie;
import Model.Addresses.Value;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Nik on 28/04/17.
 */
public class TestTenarySearchTrie {


    @Test
    public void testBasicKeysThatMatch(){
        TenarySearchTrie trie = new TenarySearchTrie();
        trie.put("Rued Langgaards Vej 7", new Value(0, 0f, 0f, false));
        String output = trie.keysThatMatch("ggaards").get(false).get(0);
        assertEquals("Rued Langgaards Vej 7", output);
    }

    /**
     * This test is difficult to get to work as expected because it is not easy to return to
     * an earlier node in the trie. The prefix has to reset itself everytime it meets a node that does not
     * match the pattern.
     */
    @Test
    public void testSpecialCaseKeysThatMatch(){
        TenarySearchTrie trie = new TenarySearchTrie();
        trie.put("AAABAAAB", new Value(0, 0f, 0f, false));
        String output = trie.keysThatMatch("ABA").get(false).get(0);
        assertEquals("AAABAAAB", output);
    }

    @Test
    public void testBasicPutAndGet(){
        TenarySearchTrie trie = new TenarySearchTrie();
        trie.put("Algade 12", new Value(0, 2f, 3f, false));
        Float xoutput = trie.get("Algade 12").get(0).getX();
        Float youtput = trie.get("Algade 12").get(0).getY();
        assertTrue(xoutput.equals(2f));
        assertTrue(youtput.equals(3f));
    }

    @Test
    public void testMultipleEntries(){
        TenarySearchTrie trie = new TenarySearchTrie();
        trie.put("A B C D E F G", new Value(0, 0f, 0f, false));
        trie.put("A B C D E F G", new Value(0, 1f, 1f, false));
        int expectedSize = trie.get("A B C D E F G").size();
        assertEquals(2, expectedSize);

        Float x = trie.get("A B C D E F G").get(1).getX();
        assertTrue(x.equals(1f));
    }

    @Test
    public void testSignificanceHashMap(){
        TenarySearchTrie trie = new TenarySearchTrie();
        trie.put("A B C D E F G", new Value(0, 0f, 0f, false));
        trie.put("A B C D E F G", new Value(0, 0f, 0f, false));
        trie.put("A B C D E F G", new Value(0, 0f, 0f, true));
        int expectedSizeIfFalse = trie.keysThatMatch("B C D").get(false).size();
        int expectedSizeIfTrue = trie.keysThatMatch("B C D").get(true).size();
        assertEquals(2, expectedSizeIfFalse);
        assertEquals(1, expectedSizeIfTrue);
    }

    @Test
    public void testInvalidSearch(){
        TenarySearchTrie trie = new TenarySearchTrie();
        trie.put(" A B C ", new Value(0, 0f, 0f, false));
        assertEquals(null, trie.get("A BC"));
    }




}
