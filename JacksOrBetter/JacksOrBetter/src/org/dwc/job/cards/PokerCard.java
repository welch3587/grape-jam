package org.dwc.job.cards;

import java.io.Serializable;

/**
 * This Poker Card is based on Kevin L. Suffecool's cpp code located here
 * http://www.suffecool.net/poker/evaluator.html. I'm adapting it to Java
 * as part of a larger effort to educate myself.
 * 
 * From Kevin's page, here are some bit pattern examples:
 * 
 * xxxAKQJT 98765432 CDHSrrrr xxPPPPPP
 * 00001000 00000000 01001011 00100101    King of Diamonds
 * 00000000 00001000 00010011 00000111    Five of Spades
 * 00000010 00000000 10001001 00011101    Jack of Clubs
 * 00000010 00000000 00101001 00011101    Jack of Hearts
 * 00000000 00000001 00010000 00000010    Two of Spades
 * 00000000 00000001 10000000 00000010    Two of Clubs
 * 00000000 00000001 01000000 00000010    Two of Diamonds
 * I will be storing these integer values in the cardValue variable.
 *
 * @author Dave Welch
 */

public class PokerCard implements Serializable {
	private static final long serialVersionUID = 5882464;
    private int cardValue = 0;
    private int cardStatus = 0;
    
    private static int CLUB = 0x8000;
    private static int DIAMOND = 0x4000;
    private static int HEART = 0x2000;
    private static int SPADE = 0x1000;

    public PokerCard() {
        initializeCard(0, 0);
    }
    
    public PokerCard(int inValue, int inStatus) {
        initializeCard(inValue, inStatus);
    }
    
    public synchronized void initializeCard(int inValue, int inStatus) {
        if(cardValue == 0) {
            cardValue = inValue;
            cardStatus = inStatus;
        }
    }

    public synchronized int getValue() {
        return cardValue;
    }
    
    public synchronized int getStatus() {
        return cardStatus;
    }

    public synchronized void setStatus(int inStatus) {
        cardStatus = inStatus;
    }

    public synchronized String getCardName() {
        //Needs to perform the bitwise comparisons for both the value and suit
        //Returns a string with the name in the format "<value> of <suit>"
        String cardName = "";
        int cardRank = ((cardValue & 3840)/256);

        if(cardRank < 9) {
            cardName = String.valueOf(cardRank+2);
        } else {
            if(cardRank == 9) {
                cardName = "Jack";
            }
            if(cardRank == 10) {
                cardName = "Queen";
            }
            if(cardRank == 11) {
                cardName = "King";
            }
            if(cardRank == 12) {
                cardName = "Ace";
            }
        }
        
        cardName = cardName.concat(" of " + getSuitName());
        
        return cardName;
    }

    public synchronized String getSuitName() {
        //A simple bitwise comparison to determine the suit
        //then returning the name of the suit.
        String suitName = "";
        
        if((cardValue & CLUB) == CLUB) {
            suitName = "Clubs";
        }
        
        if((cardValue & DIAMOND) == DIAMOND) {
            suitName = "Diamonds";
        }

        if((cardValue & HEART) == HEART) {
            suitName = "Hearts";
        }

        if((cardValue & SPADE) == SPADE) {
            suitName = "Spades";
        }
        return suitName;
    }
    
    public synchronized String getDefaultCardBackground() {
    	//This method will allow us to grab the card background image in case
    	//we need that rather than the face of the card
    	return "b1fv.png";
    }
    
    public synchronized String getCardGraphicName() {
        //Same basic logic as getCardName except I'm using this to create
        //a string that specified the name of the file containing the card graphic
    	//These names have to match the file names for the particular set of 
    	//cards graphics. I've seen different sets use different naming 
    	//conventions (like "c1" or "AC" for the ace of clubs) 
        String cardGraphicName = "";
        
        //If we have an unset card (we haven't dealt it yet)
        if(cardValue == 0) {
        	return getDefaultCardBackground();
        }
        
        int cardRank = ((cardValue & 3840)/256);

        if((cardValue & CLUB) == CLUB) {
            cardGraphicName = cardGraphicName.concat("c");
        }
        
        if((cardValue & DIAMOND) == DIAMOND) {
            cardGraphicName = cardGraphicName.concat("d");
        }

        if((cardValue & HEART) == HEART) {
            cardGraphicName = cardGraphicName.concat("h");
        }

        if((cardValue & SPADE) == SPADE) {
            cardGraphicName = cardGraphicName.concat("s");
        }

        if(cardRank < 9) {
            cardGraphicName = cardGraphicName.concat(String.valueOf(cardRank+2));
        } else {
            if(cardRank == 9) {
                cardGraphicName = cardGraphicName.concat("j");
            }
            if(cardRank == 10) {
                cardGraphicName = cardGraphicName.concat("q");
            }
            if(cardRank == 11) {
                cardGraphicName = cardGraphicName.concat("k");
            }
            if(cardRank == 12) {
                cardGraphicName = cardGraphicName.concat("1");
            }
        }
        
        cardGraphicName = cardGraphicName.concat(".png");
        
        return cardGraphicName;
    }
    
    public synchronized int getPrime() {
        //String binaryString = "00000000000000000000000000111111";
        //int base = 2;
        int decimal = Integer.parseInt("00000000000000000000000000111111", 2);
        return (cardValue & decimal);
    }
    
}
