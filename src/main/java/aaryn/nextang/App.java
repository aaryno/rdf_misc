package aaryn.nextang;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
		TextRdfReader reader=new TextRdfReader();
		Graph graph = reader.readGraph("/starfleet.txt");
		//GraphSONWriter.outputGraph(graph,System.out);
		//System.out.println("HELO");
		Collection<Vertex> classVertices;
		String vName=":sflt/Enterprise-D";
		Vertex ent=GraphUtil.getVertex(graph, vName);
		Vertex nameableVertex=GraphUtil.getOrCreateVertex(graph,":sflt/Nameable");
		graph.addEdge(null,  ent,  nameableVertex, ":rdf/type");


		Vertex starship=GraphUtil.getOrCreateVertex(graph, ":sflt/StarShip");
		Vertex galaxy=GraphUtil.getOrCreateVertex(graph,":sflt/GalaxyClass");
		graph.addEdge(null,  galaxy,  starship, ":rdfs/subClassOf");

		try {
			classVertices = getClasses(vName,graph);
			System.out.println(vName +" belongs to Classes:");
			for (Vertex v2 : classVertices){
				System.out.println(v2.getId());
			}
		} catch (Exception e) {
			System.err.println("Vertex not found: "+vName);
		}

		vName=":sflt/hasWeapon";
		try {
			Collection<Vertex> rangeVertices = getRangeVertices(vName,graph);
			System.out.println("RANGE FOR "+vName +":");
			for (Vertex v2 : rangeVertices){
				System.out.println(v2.getId());
			}
		} catch (Exception e) {
			System.err.println("Vertex not found: "+vName);
		}
		
		try {
			Collection<Vertex> domainVertices = getDomainVertices(vName,graph);
			System.out.println("DOMAIN FOR "+vName +":");
			for (Vertex v2 : domainVertices){
				System.out.println(v2.getId());
			}
		} catch (Exception e) {
			System.err.println("Vertex not found: "+vName);
		}
    }
    
    
    private static Collection<Vertex> getRangeVertices(String entity, Graph graph) {
    	return getAdjacentVertices(entity, graph, Direction.OUT, ":rdfs/range");
    }
    private static Collection<Vertex> getDomainVertices(String entity, Graph graph) {
    	return getAdjacentVertices(entity, graph, Direction.OUT, ":rdfs/domain");
    }
   
 	private static Collection<Vertex> getAdjacentVertices(String entity, 
 			Graph graph, Direction direction, String label) {
    	Vertex v = GraphUtil.getVertex(graph,entity);
    	Collection<Vertex> adjVertices=new HashSet<Vertex>();
    	if (v!=null){
    		System.out.println(v.getId());
    		for (Vertex inV : v.getVertices(direction,label)){
    			adjVertices.add(inV);
    		}
    	}
    	return adjVertices;
	}

	public static Collection<Vertex> getClasses(String entity, Graph graph) throws Exception{
    	Vertex v = GraphUtil.getVertex(graph,entity);
    	if (v!=null){
    		System.out.println("JI");
    		return getClasses(v, graph,true);
    	}
    	throw new Exception("Vertex not found");
    }
    
    
    public static Collection<Vertex> getClasses(Vertex v, Graph graph, boolean typeOnly){
    	Collection<Vertex> vertices=new HashSet<Vertex>();

    	for (Edge e : v.getEdges(Direction.OUT)){
   // 		System.out.println("Edge: "+e.getLabel());
    	}

    	String subClassProperty=":rdfs/subClassOf";
    	String typeProperty=":rdf/type";
    	
    	if (typeOnly){
	    	if (v.getEdges(Direction.OUT,typeProperty)!=null){
		    	for (Edge e : v.getEdges(Direction.OUT,typeProperty)){
		    		Vertex typeVertex=e.getVertex(Direction.IN);
		    		 Collection<Vertex> classVertices= getClasses(typeVertex,graph,false);
		    		 vertices.add(typeVertex);
		    		 vertices.addAll(classVertices);
		    	}
	    	}
    	} else {
	    	if (v.getEdges(Direction.OUT,subClassProperty)!=null){
		    	for (Edge e : v.getEdges(Direction.OUT,subClassProperty)){
		    		Vertex superClassVertex=e.getVertex(Direction.IN);
		    		 vertices.add(superClassVertex);
		    		 Collection<Vertex> classVertices = getClasses(superClassVertex,graph,false);
//		    		 Collection<Vertex> toReturnVertices= new ArrayList<Vertex>();
//		    		 for (Vertex vertex : classVertices){
//		    			 if (vertex.getId().equals(":owl/Class")){
//		    				 toReturnVertices.add(vertex);
//		    			 } else {
//		    				 System.out.println("V: "+vertex.getId());
//		    			 }
//		    		 }
		    		 vertices.addAll(classVertices);
		    	}
	    	}
    	}
    	return vertices;
    }
}
