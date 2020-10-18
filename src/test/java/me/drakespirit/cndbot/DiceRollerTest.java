package me.drakespirit.cndbot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class DiceRollerTest {
    
    private DiceRoller diceRoller;
    
    @BeforeEach
    void setUp() {
        diceRoller = new DiceRoller(new Random(1337));
    }
    
    @Test
    void roll_ShouldReturnEmptyListWhenStringIsInvalid() {
        String invalidString = "invalid";
        
        List<Integer> actual = diceRoller.roll(invalidString);
        
        assertThat(actual).isEmpty();
    }
    
    @Test
    void roll_ShouldReturnOneResultWhenPrefixIs1() {
        String prefixOne = "1d6";
        
        List<Integer> actual = diceRoller.roll(prefixOne);
        
        assertThat(actual.size()).isEqualTo(1);
    }
    
    @Test
    void roll_ShouldReturnTwoResultsWhenPrefixIs2() {
        String prefixTwo = "2d6";
        
        List<Integer> actual = diceRoller.roll(prefixTwo);
        
        assertThat(actual.size()).isEqualTo(2);
    }
    
    @Test
    void roll_ShouldReturnTwentyResultsWhenPrefixIs20() {
        String prefixTwenty = "20d6";
        
        List<Integer> actual = diceRoller.roll(prefixTwenty);
        
        assertThat(actual.size()).isEqualTo(20);
    }
    
    @Test
    void roll_ShouldReturnAsManyResultsAsSpecifiedInThePrefixWhenPrefixIsTheMaximumAllowedValue() {
        String prefixMax = DiceRoller.MAX_DICE + "d6";
        
        List<Integer> actual = diceRoller.roll(prefixMax);
        
        assertThat(actual.size()).isEqualTo(DiceRoller.MAX_DICE);
    }
    
    @Test
    void roll_ShouldReturnEmptyListWhenPrefixIsOverTheMaximumAllowedValue() {
        String prefixMax = (DiceRoller.MAX_DICE + 1) + "d6";
        
        List<Integer> actual = diceRoller.roll(prefixMax);
        
        assertThat(actual).isEmpty();
    }
    
    @Test
    void roll_ShouldReturnRollsOf1ResultWhenPostfixIs1() {
        String prefixOne = DiceRoller.MAX_DICE + "d1";
        
        List<Integer> actual = diceRoller.roll(prefixOne);
        
        assertThat(actual).containsOnly(1);
    }
    
    @Test
    void roll_ShouldReturnRollsOf1to6ResultWhenPostfixIs6() {
        String prefixOne = DiceRoller.MAX_DICE + "d6";
    
        List<Integer> actual = diceRoller.roll(prefixOne);
    
        assertThat(actual).allSatisfy(number -> {
            assertThat(number).isGreaterThanOrEqualTo(1);
            assertThat(number).isLessThanOrEqualTo(6);
        });
    }
    
    @Test
    void roll_ShouldReturnExpectedResultWhenDIsCapitalized() {
        String capitalD = "1D6";
        
        List<Integer> actual = diceRoller.roll(capitalD);
        
        assertThat(actual.size()).isEqualTo(1);
    }
    
    @Test
    void roll_ShouldRollOneDieWhenNoPrefixIsSpecified() {
        String noPrefix = "d20";
        
        List<Integer> actual = diceRoller.roll(noPrefix);
        
        assertThat(actual.size()).isEqualTo(1);
    }
    
    @Test
    void format_ShouldReturnNothingWhenStringIsInvalid() {
        String invalid = "invalid";
        
        String actual = diceRoller.format(invalid);
        
        assertThat(actual).isEqualTo("nothing");
    }
    
    @Test
    void format_ShouldReturnInputWhenStringIsAlreadyCorrectlyFormatted() {
        String correctlyFormatted = "1d6";
        
        String actual = diceRoller.format(correctlyFormatted);
        
        assertThat(actual).isEqualTo(correctlyFormatted);
    }
    
    @Test
    void format_ShouldFormatToLowercaseWhenDIsUppercase() {
        String capitalD = "1D6";
        
        String actual = diceRoller.format(capitalD);
        
        assertThat(actual).isEqualTo("1d6");
    }
    
    @Test
    void format_ShouldPrepend1WhenNoPrefix() {
        String noPrefix = "d6";
        
        String actual = diceRoller.format(noPrefix);
        
        assertThat(actual).isEqualTo("1d6");
    }
    
}