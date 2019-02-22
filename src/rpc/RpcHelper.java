package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

public class RpcHelper {
	private static final String TYPE = "application/json";
	private static final String ALLOWED = "Access-Control-Allow-Origin";
	
	//write a JSONArray to handle HTTP response
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException{
		PrintWriter out = response.getWriter();
		response.setContentType(TYPE);
		response.addHeader(ALLOWED, "*");
		out.print(array);
		out.close();
	}
	public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException{
		PrintWriter out = response.getWriter();
		response.setContentType(TYPE);
		response.addHeader(ALLOWED, "*");
		out.print(obj);
		out.close();
	}
	
	//parse HTTP request body
	// for set/unset favorite
	//put multiple line body strings into a single string
	public static JSONObject readJSONObject(HttpServletRequest request) {
		StringBuilder sBuilder = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			//getReader return the body
			String line = null;
			while ((line = reader.readLine()) != null) {
				sBuilder.append(line);
			}
			return new JSONObject(sBuilder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONObject();
	}
}
