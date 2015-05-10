package test;

import static org.junit.Assert.*;

import org.dwc.job.cards.PokerCard;
import org.dwc.job.cards.PokerDeck;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeckTest {
    private PokerDeck deck = new PokerDeck();
    private PokerCard card = new PokerCard();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDeck() {
		//We should be able to draw 52 cards 
		//without running out of cards in the deck. 
		//I'm running this test because I'm getting null cards on some
		//card draws and I need to track down why this is happening.
		for(int o=1; o<10; o++) {
			System.out.println("Starting Deck " + String.valueOf(o));
			for(int i=1; i<53; i++) {
				card = deck.dealCard();
				if(card == null) {
					System.out.println("Card " + String.valueOf(i) + " was NULL");
				}
			}
			deck.resetDeck();
		}
	}

}
