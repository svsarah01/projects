public class OffByOne implements CharacterComparator {

    @Override
    public boolean equalChars(char x, char y) {
        return (Math.abs(Character.toLowerCase(y) - Character.toLowerCase(x)) == 1); /* @source https://stackoverflow.com/questions/13113010/how-to-turn-uppercase-to-lowercase-using-the-charat-method */
    }
}
