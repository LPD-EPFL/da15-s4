package ch.epfl.lpd;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import ch.epfl.lpd.net.PointToPointLink;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws Exception
    {

        String sMessage = "test!";

        System.out.println("Testing PTPLinks");

        /** send to port 9001, listen on port 9000 */
        PointToPointLink link1 = new PointToPointLink("127.0.0.1", 9001, 9001);

        System.out.println("Sending message: " + sMessage);
        link1.sendOnce(sMessage);

        System.out.println("Attempting to receive a message.. ");
        String rMessage = link1.receiveOnce();

        System.out.println("Comparing '" + rMessage.toString() + "' with '" + sMessage.toString() + "'.");

        assertEquals(sMessage, rMessage);
    }
}
