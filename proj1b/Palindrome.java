public class Palindrome {

    public Deque<Character> wordToDeque(String word) {

        Deque ad = new ArrayDeque();

        for (int i = 0; i < word.length(); i++) {
            ad.addLast(word.charAt(i)); /* @source https://www.techiedelight.com/iterate-over-characters-string-java/ */
        }

        return ad;
    }

    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        }
        if (word.length() == 1 | word.length() == 0) {
            return true;
        }

        Deque ad = wordToDeque(word);
        if (ad.removeFirst() == ad.removeLast()) {
            return isPalindrome(word.substring(1, word.length() - 1)); /* @source https://beginnersbook.com/2013/12/java-string-substring-method-example/ */
        } else {
            return false;
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null) {
            return false;
        }
        if (word.length() == 1 | word.length() == 0) {
            return true;
        }
        if (cc == null) {
            return isPalindrome(word);
        }

        Deque ad = wordToDeque(word);
        if (cc.equalChars((Character) ad.removeFirst(), (Character) ad.removeLast())) {
            return isPalindrome(word.substring(1, word.length() - 1), cc);
        } else {
            return false;
        }
    }
}
