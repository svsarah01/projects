import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void test() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        String errorMessage = "";

        for (int i = 0; i < 200; i++) {
            Integer rand = StdRandom.uniform(4);
            if (rand == 0) {
                errorMessage += "addFirst(" + i + ") \n";
                solution.addFirst(i);
                student.addFirst(i);
                assertEquals(errorMessage, solution.getFirst(), student.get(0));
            }
            if (rand == 1) {
                errorMessage += "addLast(" + i + ") \n";
                solution.addLast(i);
                student.addLast(i);
                assertEquals(errorMessage, solution.getLast(), student.get(student.size() - 1));
            }
            if (rand == 2) {
                if (solution.isEmpty()) {
                    continue;
                }
                errorMessage += "removeFirst() \n";
                Integer x = solution.removeFirst();
                Integer y = student.removeFirst();
                assertEquals(errorMessage, x, y);
            }
            if (rand == 3) {
                if (solution.isEmpty()) {
                    continue;
                }
                errorMessage += "removeLast() \n";
                Integer x = solution.removeLast();
                Integer y = student.removeLast();
                assertEquals(errorMessage, x, y);
            }
        }

    }
}
