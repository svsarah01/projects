import java.lang.*;

public class OffByN implements CharacterComparator {
    private int diff;
    public OffByN(int n) {
        diff = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return (Math.abs(y - x) == diff);
    }
}
