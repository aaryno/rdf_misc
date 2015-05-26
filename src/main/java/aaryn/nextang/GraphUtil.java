package aaryn.nextang;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class GraphUtil {

	public static Vertex getOrCreateVertex(Graph graph, String object) {
	    object=object.replaceAll("\"","");
		Vertex v=graph.getVertex(object);
		if (v==null){
			v=graph.addVertex(object);
		}
		return v;
	}
	public static Vertex getVertex(Graph graph, String object) {
	    object=object.replaceAll("\"","");
		Vertex v=graph.getVertex(object);
		return v;
	}

}
