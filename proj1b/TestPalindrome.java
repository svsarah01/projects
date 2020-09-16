import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome<T> {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque<Character> d = palindrome.wordToDeque("persiflage");
        Deque actual = new ArrayDeque();
        for (int i = 0; i < "persiflage".length(); i++) {
            actual.addLast("persiflage".charAt(i));
        }
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), d.get(i));
        }
        assertEquals(actual.size(), d.size());
    }

    @Test
    public void emptyTestWordToDeque() {
        Deque<Character> ch = palindrome.wordToDeque("");
        Deque actual = new ArrayDeque();
        assertEquals(actual.size(), ch.size());
        assertEquals(actual.get(0), ch.get(0));
    }

    @Test
    public void testPalindrome() {
        assertFalse(palindrome.isPalindrome("moon"));
        assertTrue(palindrome.isPalindrome("noon"));
        assertFalse(palindrome.isPalindrome(null));
        assertTrue(palindrome.isPalindrome("a"));
        assertFalse(palindrome.isPalindrome("Racecar"));
        assertTrue(palindrome.isPalindrome("racecar"));
    }

    @Test
    public void testOffByOnePalindrome() {
        CharacterComparator cc = new OffByOne();
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertFalse(palindrome.isPalindrome(null, cc));
        assertTrue(palindrome.isPalindrome("a", cc));
        assertTrue(palindrome.isPalindrome("racecar", null));
        assertFalse(palindrome.isPalindrome("moon", null));
    }
}