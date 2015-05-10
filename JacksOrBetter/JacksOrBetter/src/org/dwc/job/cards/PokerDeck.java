package org.dwc.job.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


/**
 * This Poker Deck is based on Kevin L. Suffecool's cpp code located here
 * http://www.suffecool.net/poker/evaluator.html. I'm adapting it to Java
 * as part of a larger project for the purposes of getting more comfortable 
 * with Java.
 * 
 * From Kevin's page, here are some bit pattern examples:
 * 
 * xxxAKQJT 98765432 CDHSrrrr xxPPPPPP
 * 00001000 00000000 01001011 00100101    King of Diamonds
 * 00000000 00001000 00010011 00000111    Five of Spades
 * 00000010 00000000 10001001 00011101    Jack of Clubs
 * 
 * I'm going to expand it a little by also associating a picture
 * with each card.
 * @author Dave Welch
 */
public class PokerDeck implements Serializable {
	private static final long serialVersionUID = 5771464;
    private int[] primes = new int[13];
    private ArrayList<PokerCard> cardDeck = new ArrayList<>();
    
    private static int STATUS_INDECK = 1;
    private static int STATUS_INPLAY = 2;
    private static int STATUS_INDISCARD = 3;
    private static int STATUS_REMOVED = 4;
    
    public PokerDeck() {
        initPrimes();
        int tempCardCalc;
        int i; //suit counter
        int j; //card rank counter
        int n=0; //deck array counter (52 total cards)
        int suit = 0x8000; //suit value base (bit shifted later
        
        //Setting up the deck for the first time
        for ( i = 0; i < 4; i++) {
            for ( j = 0; j < 13; j++, n++ ) {
                tempCardCalc = primes[j] | (j << 8) | suit | (1 << (16+j));
                cardDeck.add(new PokerCard(tempCardCalc, STATUS_INDECK));
            }
            suit >>= 1;
        }
        shuffleDeck();
    }
    private synchronized void initPrimes() {
        // = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41];
        //These values are used later when calculating the value of 
        //a hand of poker.
        primes[0] =  2;
        primes[1] =  3;
        primes[2] =  5;
        primes[3] =  7;
        primes[4] =  11;
        primes[5] =  13;
        primes[6] =  17;
        primes[7] =  19;
        primes[8] =  23;
        primes[9] =  29;
        primes[10] =  31;
        primes[11] =  37;
        primes[12] =  41;
    }
    
    public synchronized void resetDeck() {
        for(PokerCard card : cardDeck) {
            if(card.getStatus() != STATUS_INDECK) { 
                card.setStatus(STATUS_INDECK); 
            }
        }
        shuffleDeck();
    }
    
    public synchronized void discardCard(PokerCard pc) {
        for(PokerCard card : cardDeck) {
            if(card == pc) { 
                card.setStatus(STATUS_INDISCARD); 
            }
        }
    }
    
    public synchronized void shuffleDeck() {
        Collections.shuffle(cardDeck);
    }
    
    public synchronized PokerCard dealSpecificCard(String inCardGraphic) {
        for(PokerCard pc : cardDeck) {
            if(inCardGraphic.equals(pc.getCardGraphicName())) {
                pc.setStatus(STATUS_INPLAY);
                return pc;
            }
        }
        return null;
    }
    
    public synchronized PokerCard dealCard() {
        PokerCard topCard = takeTopCard();
        if(!(topCard == null)) {
            return topCard;
        }
        //If we get to this point, it means we've dealt all cards out of the deck
        //We need to reset the deck and then deal the top card.
        //This should never happen in JacksorBetter, but if I re-use this 
        //PokerDeck I'll want this in it.
        resetDeck();
        topCard = takeTopCard();
        if(!(topCard == null)) {
            return topCard;
        } else { //The deck has no cards left to deal. All card are in play or removed
            return null;
        }
    }

    private synchronized PokerCard takeTopCard() {
        for(PokerCard card : cardDeck) {
            if(card.getStatus() == STATUS_INDECK) {
                card.setStatus(STATUS_INPLAY);
                return card;
            }
        }
        return null;
    }
    
}
