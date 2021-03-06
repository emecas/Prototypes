package com.me.servidor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
/*import org.json.JSONArray;
import org.json.JSONException;
*/

/**
 * He imporatdo las libre de JSON porque no he encontrado el jar oficial (y que no sea del 2009)... a�n
 * https://github.com/douglascrockford/JSON-java
 */
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Clase que se encarga de enviar una peticion y gestionar la respuesta 
 * devolviendo un JSON Array.
 * 
 * @author Ricardo
 *
 */
public class HttpPostConnector {
	
	//public static String IP_Server = "localhost";// IP DE NUESTRO PC
	public static String IP_Server = "crazyrockets.hol.es";// IP DE NUESTRO SERVIDOR
	
	
	/* Conexiones */
	
	// Conexi�n a registro
	public static String URL_CONNECT_REGISTRO = "http://" + IP_Server + "/adduser.php";
	
	// Conexi�n a login
	public static String URL_CONNECT_LOGIN = "http://" + IP_Server + "/acces.php";
	

	private InputStream is = null;
	private String result = "";

	public JSONArray getserverdata(ArrayList<NameValuePair> parameters,
			String urlwebserver) {

		// conecta via http y envia un post.
		httppostconnect(parameters, urlwebserver);

		if (is != null) {// si obtuvo una respuesta
			getpostresponse();
			return getjsonarray();
		} else
			return null;

	}

	// peticion HTTP
	private void httppostconnect(ArrayList<NameValuePair> parametros,
			String urlwebserver) {

		//
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urlwebserver);
			httppost.setEntity(new UrlEncodedFormEntity(parametros));
			// ejecuto peticion enviando datos por POST
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
//			Log.e("log_tag", "Error in http connection " + e.toString());
		}

	}

	private void getpostresponse() {

		// Convierte respuesta a String
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
			//Log.e("getpostresponse", " result= " + sb.toString());
		} catch (Exception e) {
			//Log.e("log_tag", "Error converting result " + e.toString());
		}
	}

	public JSONArray getjsonarray() {
		// parse json data
		try {
			JSONArray jArray = new JSONArray(result);
			return jArray;
		} catch (JSONException e) {
			//Log.e("log_tag", "Error parsing data " + e.toString());
			return null;
		}

	}
	
	public String getTrace(){
		return result;
	}

}
