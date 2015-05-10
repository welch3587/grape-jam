package test;

import static org.junit.Assert.*;

import org.dwc.job.JacksOrBetter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JorBTest {
	JacksOrBetter JorB = new JacksOrBetter();
	String[] cardArray = {"cj.png", "hj.png", "h2.png", "c3.png", "d4.png"};
	int betValue = 1;
	
	@Before
	public void setUp() throws Exception {
		JorB.stackDeck(cardArray);
		JorB.setBet(betValue);
		JorB.goToFinishPhase();
	}

	@After
	public void tearDown() throws Exception {
	} 
	
	@Test
	public void testHandRank() {
		assertEquals(4205, JorB.getHandRank());
	}

	@Test
	public void testCard1() {
		assertEquals(cardArray[0],JorB.getCardImageName(1));
	}

	@Test
	public void testCard2() {
		assertEquals(cardArray[1],JorB.getCardImageName(2));
	}

	@Test
	public void testCard3() {
		assertEquals(cardArray[2],JorB.getCardImageName(3));
	}

	@Test
	public void testCard4() {
		assertEquals(cardArray[3],JorB.getCardImageName(4));
	}

	@Test
	public void testCard5() {
		assertEquals(cardArray[4],JorB.getCardImageName(5));
	}
	
	@Test
	public void testPayout() {
		assertEquals(betValue,JorB.getPayout());
	}

	@Test
	public void testCredits() {
		assertEquals(1000+betValue,JorB.getCredits());
	}


}
