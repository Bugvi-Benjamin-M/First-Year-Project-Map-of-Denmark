package ModelPackage;

import Model.BST;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jakob on 22-03-2017.
 */
public class TestBST {
    BST bst;

    @Before
    public void setUp() throws Exception {
        System.out.println("before");
        bst = new BST();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("after");

    }

    @Test
    public void test(){

    }

}