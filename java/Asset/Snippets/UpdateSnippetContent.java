package dev.marketo.samples.Snippets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.eclipsesource.json.JsonObject;

public class UpdateSnippetContent {
	public String marketoInstance = "CHANGE ME";//Replace this with the host from Admin Web Services
	public String marketoIdURL = marketoInstance + "/identity";	
	public String clientId = "CHANGE ME";	//Obtain from your Custom Service in Admin>Launchpoint
	public String clientSecret = "CHANGE ME";	//Obtain from your Custom Service in Admin>Launchpoint
	public String idEndpoint = marketoIdURL + "/oauth/token?grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
	public int id;//id of snippet to update
	public String type;//type of content to update, Text, HTML, or Dynamic Content
	public String content;//updated content
	
	public static void main(String[] args){
		UpdateSnippetContent update = new UpdateSnippetContent();
		update.id = 7;
		update.type = "Text";
		update.content = "Hello World!";
		String result  = update.postData();
		System.out.println(result);
	}
	public String postData(){
		String result = null;
		try {
			String requestBody = buildRequest();
			String endpoint = marketoInstance + "/rest/asset/v1/snippet/" + id + "/content.json?access_token=" + getToken();
			URL url = new URL(endpoint.toString());
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("accept", "text/json");
            urlConn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
			wr.write(requestBody);
			wr.flush();
			int responseCode = urlConn.getResponseCode();
			if (responseCode == 200){
				InputStream inStream = urlConn.getInputStream();
				result = convertStreamToString(inStream);
			}else{
				result = "Status Code: " + responseCode;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	public String getToken(){
		String token = null;
		try {
			URL url = new URL(idEndpoint);
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("accept", "application/json");
            int responseCode = urlConn.getResponseCode();
            if (responseCode == 200) {
                InputStream inStream = urlConn.getInputStream();
                Reader reader = new InputStreamReader(inStream);
                JsonObject jsonObject = JsonObject.readFrom(reader);
                token = jsonObject.get("access_token").asString();
            }else {
                throw new IOException("Status: " + responseCode);
            }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
            e.printStackTrace();
        }
		return token;
	}

	private String buildRequest(){
		StringBuilder sb = new StringBuilder();
		sb.append("&type=" + type);
		sb.append("&content=" + content);
		return sb.toString();
	}
	
    private String convertStreamToString(InputStream inputStream) {

        try {
            return new Scanner(inputStream).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return "";
        }
    }
}