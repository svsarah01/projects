public class OffByN implements CharacterComparator {
    private int diff;
    public OffByN(int n) {
        diff = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return (Math.abs(Character.toLowerCase(y) - Character.toLowerCase(x)) == diff);
    }
}
