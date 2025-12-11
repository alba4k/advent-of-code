import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

class Node {
    public long x;
    public long y;
    public long z;

    public Node(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return x == node.x &&
               y == node.y &&
               z == node.z;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y, z);
    }
}

class Edge {
    public Node u;
    public Node v;
    private double length;

    public Edge(Node u, Node v) {
        this.u = u;
        this.v = v;

        this.length = -1;
    }

    double getLength() {
        if(this.length != -1) return this.length;

        long dx = u.x - v.x;
        long dy = u.y - v.y;
        long dz = u.z - v.z;

        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}

public class Part1 {
    static ArrayList<Node> getNodes(String filename) 
    throws FileNotFoundException {
        ArrayList<Node> nodes = new ArrayList<>();
        Scanner input = new Scanner(new File(filename));

        while(input.hasNextLine()) {
            Scanner line = new Scanner(input.nextLine());
            line.useDelimiter("\\s*,\\s*");
            long x = line.nextLong();
            long y = line.nextLong();
            long z = line.nextLong();

            nodes.add(new Node(x, y, z));

            line.close();
        }
        
        input.close();

        return nodes;
    }

    static Edge longest(ArrayList<Edge> edges) {
        if(edges == null || edges.size() == 0) return null;

        Edge current = edges.get(0);

        for(Edge edge : edges)
            if(edge.getLength() > current.getLength())
                current = edge;

        return current;
    }

    static ArrayList<Edge> getShortestEdges(ArrayList<Node> nodes, int k) {
        ArrayList<Edge> shortest = new ArrayList<Edge>();

        for(int i = 0; i < nodes.size(); i++) {
            for(int j = i+1; j < nodes.size(); j++) {
                Edge current = new Edge(nodes.get(i), nodes.get(j));

                if(shortest.size() < k) {
                    shortest.add(current);
                    continue;
                }

                Edge current_longest = longest(shortest);
                if(current.getLength() < current_longest.getLength()) {
                    shortest.remove(current_longest);
                    shortest.add(current);
                }
            }
        }

        return shortest;
    }

    public static void main(String[] args) 
    throws FileNotFoundException {
        ArrayList<Node> nodes = getNodes("input");
        ArrayList<Edge> edges = getShortestEdges(nodes, 1000);
        ArrayList<Long> largest = new ArrayList<>();
        largest.add(0L);
        largest.add(0L);
        largest.add(0L);
        
        Map<Node, HashSet<Node>> adj = new HashMap<>();
        
        for(Node node : nodes)
            adj.put(node, new HashSet<>());

        for(Edge edge : edges) {
            adj.get(edge.u).add(edge.v);
            adj.get(edge.v).add(edge.u);
        }

        HashSet<Node> visited = new HashSet<>();

        for(Node node : nodes) {
            long component_size = 0;
            if(!visited.contains(node)) {
                LinkedList<Node> queue = new LinkedList<>();
                
                queue.addLast(node);
                visited.add(node);

                while(!queue.isEmpty()) {
                    Node current = queue.removeFirst();
                    component_size++;

                    for(Node neighbor : adj.get(current)) {
                        if(!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.addLast(neighbor);
                        }
                    }
                }
            }

            long min = Math.min(Math.min(largest.get(0), largest.get(1)), largest.get(2));
            if(component_size > min) {
                largest.remove(min);
                largest.add(component_size);
            }
        }

        long result = 1;
        for(long n : largest) result *= n;
        
        System.out.println(result);
    }
}
