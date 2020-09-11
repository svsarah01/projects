public class ArrayDequeTest {

    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        ArrayDeque<String> ad1 = new ArrayDeque<String>();

        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        passed = checkSize(1, ad1.size()) && passed;
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.addLast("middle");
        passed = checkSize(2, ad1.size()) && passed;

        ad1.addLast("back");
        passed = checkSize(3, ad1.size()) && passed;

        System.out.println("Printing out deque: ");
        ad1.printDeque();

        printTestStatus(passed);
    }

    /** Adds an item, then removes an item, and ensures that arraydeque is empty afterwards. */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.removeFirst();
        // should be empty
        passed = checkEmpty(true, ad1.isEmpty()) && passed;

        ad1.addFirst(20);
        ad1.addLast(30);
        ad1.addFirst(10);
        ad1.addLast(40);
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.removeFirst();
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.removeLast();
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.removeLast();
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.removeLast();
        // should be empty
        passed = checkEmpty(true, ad1.isEmpty()) && passed;

        printTestStatus(passed);
    }

    /** Adds many items to the front and then removes them from the front, checking for resizing capabilities. */
    public static void resizeTest() {

        System.out.println("Running addFirst/removeFirst test.");
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        boolean passed = checkEmpty(true, ad1.isEmpty());

        // adding 1-100 starting w/ 100 and going to 1
        for (int i = 100; i > 0; i = i - 1) {
            ad1.addFirst(i);
        }
        //checking size
        passed = checkSize(100, ad1.size()) && passed;

        //removing the first 75
        for (int i = 0; i < 75; i = i + 1) {
            ad1.removeFirst();
        }
        //checking size
        passed = checkSize(25, ad1.size()) && passed;

        printTestStatus(passed);
    }

    /** Adds many items to the back and then removes them from the back, checking for resizing capabilities. */
    public static void resizeTest2() {

        System.out.println("Running addLast/removeLast test.");
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        boolean passed = checkEmpty(true, ad1.isEmpty());

        // adding 1-100 starting at 1 and going to 100
        for (int i = 1; i <= 100; i = i + 1) {
            ad1.addLast(i);
        }
        //checking size
        passed = checkSize(100, ad1.size()) && passed;

        //removing the last 75
        for (int i = 0; i < 75; i = i + 1) {
            ad1.removeLast();
        }
        //checking size
        passed = checkSize(25, ad1.size()) && passed;

        printTestStatus(passed);
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        resizeTest();
        resizeTest2();
    }
}
