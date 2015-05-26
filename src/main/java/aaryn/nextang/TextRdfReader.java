package aaryn.nextang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;

public class TextRdfReader {

	public Graph readGraph(String filename) throws IOException{
		Graph graph=new TinkerGraph();
		InputStream is = getClass().getResourceAsStream(filename);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line=null;
		boolean midLine=false;
		String subject=null;
		String predicate=null;
		String object=null;
		while((line=br.readLine())!=null && line.length()!=0){
			if (line.contains("[")){
				
				if (subject==null){
					subject=line.replaceAll("^ ","").split(" ")[0].replaceAll("\\[","").replaceAll("\\(","");
				}
				String rest="";
				if (predicate==null){
					rest=line.replaceAll("^.*"+subject,"");
					rest=rest.replaceAll("^ ","");
				    predicate=rest.split(" ")[0];
				}
				if (object==null && rest.indexOf(" ")>0){
					object=rest.split(" ")[1].replaceAll("\\]","");
	
					if (object.indexOf("phaser bank")>0){
						int h=6;
					}
					Vertex inV=GraphUtil.getOrCreateVertex(graph,subject);
					Vertex outV=GraphUtil.getOrCreateVertex(graph,object);
					graph.addEdge(null,inV,outV,predicate);
					subject=null;
					predicate=null;
					object=null;
					
				}
				
				line=br.readLine();
			}
		}
		return graph;
	}
	
	public static void main(String[] arg) throws IOException{
		TextRdfReader reader=new TextRdfReader();
		Graph graph = reader.readGraph("/starfleet.txt");
		GraphSONWriter.outputGraph(graph,System.out);
	}
}
