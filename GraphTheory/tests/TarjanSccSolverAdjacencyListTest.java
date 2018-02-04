import static com.google.common.truth.Truth.assertThat;
import com.google.common.collect.ImmutableList;

import java.util.*;
import org.junit.*;

public class TarjanSccSolverAdjacencyListTest {

  // Initialize graph with 'n' nodes.
  public static List<List<Integer>> createGraph(int n) {
    List<List<Integer>> graph = new ArrayList<>();
    for(int i = 0; i < n; i++) graph.add(new ArrayList<>());
    return graph;
  }

  // Add directed edge to graph.
  public static void addEdge(List<List<Integer>> graph, int from, int to) {
    graph.get(from).add(to);
  }

  @Test(expected=IllegalArgumentException.class)
  public void nullGraphConstructor() {
    new TarjanSccSolverAdjacencyList(null);
  }

  @Test
  public void singletonCase() {
    int n = 1;
    List<List<Integer>> g = createGraph(n);
    
    TarjanSccSolverAdjacencyList solver = new TarjanSccSolverAdjacencyList(g);
    solver.solve();

    int[] actual = solver.getSccs();
    int[] expected = new int[n];
    assertThat(actual).isEqualTo(expected);
    assertThat(solver.sccCount()).isEqualTo(1);
  }

  @Test
  public void testTwoDisjointComponents() {
    int n = 5;
    List<List<Integer>> g = createGraph(n);

    addEdge(g, 0, 1);
    addEdge(g, 1, 0);
    
    addEdge(g, 2, 3);
    addEdge(g, 3, 4);
    addEdge(g, 4, 2);
    
    TarjanSccSolverAdjacencyList solver = new TarjanSccSolverAdjacencyList(g);
    solver.solve();

    List<List<Integer>> expectedSccs = ImmutableList.of(
      ImmutableList.of(0, 1),
      ImmutableList.of(2, 3, 4)
    );
    
    assertThat(solver.sccCount()).isEqualTo(expectedSccs.size());
    assertThat(isScc(solver.getSccs(), expectedSccs)).isTrue();
  }

  @Test
  public void testButterflyCase() {
    int n = 5;
    List<List<Integer>> g = createGraph(n);

    addEdge(g, 0, 1);
    addEdge(g, 1, 2);
    addEdge(g, 2, 3);
    addEdge(g, 3, 1);
    addEdge(g, 1, 4);
    addEdge(g, 4, 0);
    
    TarjanSccSolverAdjacencyList solver = new TarjanSccSolverAdjacencyList(g);
    solver.solve();

    List<List<Integer>> expectedSccs = ImmutableList.of(
      ImmutableList.of(0, 1, 2, 3, 4)
    );
    
    assertThat(solver.sccCount()).isEqualTo(expectedSccs.size());
    assertThat(isScc(solver.getSccs(), expectedSccs)).isTrue();
  }

  private static boolean isScc(int[] ids, List<List<Integer>> expectedSccs) {
    Set<Integer> set = new HashSet<>();
    for(List<Integer> indexes : expectedSccs) {
      set.clear();
      for (int index : indexes) set.add(ids[index]);
      if (set.size() != 1) return false;
    }
    return true;
  }
}
