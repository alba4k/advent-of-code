import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

class DSU {
    private Map<Node, Node> parent;
    public int components;

    public DSU(ArrayList<Node> nodes) {
        parent = new HashMap<>();
        for (Node node : nodes)
            parent.put(node, node);
        
        components = nodes.size();
    }

    public Node find(Node i) {
        if (parent.get(i) == i)
            return i;

        parent.put(i, find(parent.get(i)));
        return parent.get(i);
    }

    public boolean union(Node i, Node j) {
        Node root_i = find(i);
        Node root_j = find(j);

        if (root_i != root_j) {
            parent.put(root_i, root_j);
            components--;
            return true;
        }
        return false;
    }
}

public class Part2 {
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

    static ArrayList<Edge> getSortedEdge(ArrayList<Node> nodes) {
        ArrayList<Edge> allEdges = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i++)
            for (int j = i + 1; j < nodes.size(); j++)
                allEdges.add(new Edge(nodes.get(i), nodes.get(j)));
        
        allEdges.sort(Comparator.comparingDouble(Edge::getLength));
        
        return allEdges;
    }

    public static void main(String[] args) 
    throws FileNotFoundException {
        ArrayList<Node> nodes = getNodes("input");
        ArrayList<Edge> allEdges = getSortedEdge(nodes);

        DSU dsu = new DSU(nodes);
        Edge last_edge = null;

        for (Edge edge : allEdges) {
            if (dsu.union(edge.u, edge.v))
                last_edge = edge;
            
            if (dsu.components == 1)
                break;
        }

        // not checking last_edge
        long x1 = last_edge.u.x;
        long x2 = last_edge.v.x;
        long result = x1 * x2;
            
        System.out.println(result);
    }
}