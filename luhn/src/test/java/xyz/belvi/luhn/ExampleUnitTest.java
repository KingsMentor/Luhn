package xyz.belvi.luhn;

import org.junit.Test;

import xyz.belvi.luhn.cardValidator.CardValidator;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        CardValidator cardValidator = new CardValidator("5399832631479478");
        assertEquals(true,cardValidator.isValidCardNumber());
    }
}