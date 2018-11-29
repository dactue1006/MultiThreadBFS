package bfs;

import java.io.*;
import java.util.*;

public class Main {
	private final static String path = System.getProperty("user.dir") + "//src//bfs//test";
	private static Graph graph;
	private static int[] trace;
	private static Queue<Integer> q;
	private static List<Thread> arrThread;
	private static int numThreads = 1;
	
	public static Graph getGraph() {
		return graph;
	}

	public static void setGraph(Graph graph) {
		Main.graph = graph;
	}

	public static int[] getTrace() {
		return trace;
	}

	public static void setTrace(int[] trace) {
		Main.trace = trace;
	}

	public static Queue<Integer> getQ() {
		return q;
	}

	public static void setQ(Queue<Integer> q) {
		Main.q = q;
	}

	public static void initialGraph(String line) throws NullPointerException {
		String[] arrStr = line.split(" ");
		int n = Integer.parseInt(arrStr[0]),
			first = Integer.parseInt(arrStr[1]),
			last = Integer.parseInt(arrStr[2]);
		graph = new Graph(n, first, last);
	}
	
	public static void changeGraph(String line) {
		String[] arrStr = line.split(" ");
		int a = Integer.parseInt(arrStr[0]) - 1,
			b = Integer.parseInt(arrStr[1]) - 1;
		int[][] matrix = graph.getMatrix();
		matrix[a][b] = matrix[b][a] = 1;
		graph.setMatrix(matrix);
	}
	
	public static void readFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		try {
			StringBuilder sb = new StringBuilder();
			// read first line
			String line = br.readLine();
			initialGraph(line);

			
			while ((line = br.readLine()) != null) {
				changeGraph(line);
				sb.append(line);
				sb.append(System.lineSeparator());
			}
		} catch(Exception e) {
			
		} finally {
			br.close();
		}
	}

	public static void BFS() throws InterruptedException {
        long start = System.currentTimeMillis();
		trace = new int[graph.getN()];
		Arrays.fill(trace, -1);
		q = new LinkedList<>();
		q.add(graph.getFirst()-1);
		trace[graph.getFirst()-1] = -2;
		int[][] matrix = graph.getMatrix();
		while (!q.isEmpty()) {
			int currentVertex = q.remove();
			System.out.println(q);
			arrThread = new ArrayList<>();
			for (int i = 0; i < numThreads; i++) {
				Thread thread = 
					new Thread(
						new ThreadBFS(
								i+1, 
								graph.getN()*i/numThreads, 
								graph.getN()*(i+1)/numThreads, currentVertex));
				arrThread.add(thread);
				thread.start();
			}
			
			for (int i = 0; i < numThreads; i++) {
				arrThread.get(i).join();
			}
		}
        long end = System.currentTimeMillis();
        System.out.println(end - start + "ms");

	}
	
	public static void printPath() {
		System.out.println("From vertex " + (graph.getFirst()) + " you can visit");
		for (int i = 0; i < graph.getN(); i++)
			if (trace[i]!=-1)
				System.out.print(i+1 + " ");
		System.out.println();
		if (trace[graph.getLast()-1] == -1)
			System.out.println("Not found");
		else {
			int f = graph.getFirst()-1,
				l = graph.getLast()-1;
			while (f != l) {
				System.out.print(l+1+"->");
				l = trace[l];
			}
			System.out.println(graph.getFirst());
		}
	}
	
	public static void inputNumThread() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter number of thread (must be less than number of vertex): ");
		int num = sc.nextInt();	
		numThreads = num > graph.getN() ? graph.getN() : num;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Main thread start");
		readFile();
		inputNumThread();
		BFS();
		printPath();
		graph.printMatrix();
		System.out.println("Main thread exitting");

	}

}
