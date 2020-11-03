package bearmaps.proj2c;

import bearmaps.proj2ab.DoubleMapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;
    private List<Vertex> solution;
    private double solutionWeight;
    private int numStatesExplored;
    private double time;

    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Vertex> edgeTo;
    private DoubleMapPQ<Vertex> pq;
    private Vertex start;
    private Vertex end;
    private AStarGraph h;

    private void relax(WeightedEdge<Vertex> e) {
        if (e == null) {
            return;
        }
        Vertex from = e.from();
        Vertex to = e.to();
        double weight = e.weight();
        double heuristic = h.estimatedDistanceToGoal(to, end);
        if (!distTo.containsKey(to) || distTo.get(from) + weight < distTo.get(to)) {
            distTo.put(to, distTo.get(from) + weight);
            edgeTo.put(to, from);
            if (pq.contains(to)) {
                pq.changePriority(to, distTo.get(to) + heuristic);
            } else {
                pq.add(to, distTo.get(to) + heuristic);
            }
        }
    }

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        h = input;
        this.start = start;
        this.end = end;
        numStatesExplored = 0;
        Stopwatch sw = new Stopwatch();
        pq = new DoubleMapPQ<>();
        pq.add(start, input.estimatedDistanceToGoal(start, end));
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        distTo.put(start, 0.0);
        while (pq.size() != 0 && !pq.getSmallest().equals(end)) {
            time = sw.elapsedTime();
            if (time > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                solution = Collections.emptyList();
                solutionWeight = 0;
                break;
            }
            Vertex best = pq.removeSmallest();
            numStatesExplored += 1;
            for (WeightedEdge<Vertex> e : input.neighbors(best)) {
                relax(e);
            }
        }
        time = sw.elapsedTime();
        if (pq.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
            solution = Collections.emptyList();
            solutionWeight = 0;
        } else if (pq.getSmallest().equals(end)) {
            outcome = SolverOutcome.SOLVED;
            solutionWeight = distTo.get(end);
            /* creating solution list */
            solution = new ArrayList<>();
            solution.add(end);
            Vertex v = end;
            while (v != start) {
                Vertex e = edgeTo.get(v);
                solution.add(e);
                v = e;
            }
            Collections.reverse(solution);
        }
    }

    public SolverOutcome outcome() {
        return outcome;
    }

    public List<Vertex> solution() {
        return solution;
    }

    public double solutionWeight() {
        return solutionWeight;
    }

    public int numStatesExplored() {
        return numStatesExplored;
    }

    public double explorationTime() {
        return time;
    }
}
