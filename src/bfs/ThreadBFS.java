package bfs;

import java.util.Queue;

public class ThreadBFS implements Runnable{
	private int index;
	private int start;
	private int end;
	private int currentVertex;
	
	public ThreadBFS(int index, int start, int end, int currentVertex) {
		this.index = index;
		this.start = start;
		this.end = end;
		this.currentVertex = currentVertex;
	}
	
	public void run() {
        long startTime = System.nanoTime();
        try {
			System.out.println("Thread " + index + " start");
			Graph graph = Main.getGraph();
			Queue<Integer> q = Main.getQ();
			int[] trace = Main.getTrace();
			int[][] matrix = graph.getMatrix();
	
			for (int nextVertex = start; nextVertex < end; nextVertex++) {
				if (matrix[currentVertex][nextVertex]!=0 && trace[nextVertex]==-1) {
					q.add(nextVertex);
					trace[nextVertex] = currentVertex;
					System.out.println("thread " + index + " " + nextVertex);
				}
			}
			Main.setQ(q);
			Main.setTrace(trace);
	        long endTime = System.nanoTime();
	        System.out.println("thread " + index + " exitting");
	        System.out.println((endTime-startTime)/1e6 + "ms");
        } catch(Exception e) {
        	
        }

	}

}
