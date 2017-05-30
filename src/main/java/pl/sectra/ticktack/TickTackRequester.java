package pl.sectra.ticktack;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import pl.sectra.ticktack.model.json.Punch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kmatysiak
 * Date: 18.09.2016
 * Time: 23:13
 */
public class TickTackRequester {

	private static final Logger LOG = Logger.getLogger(TickTackRequester.class);

	private static final String TICK_TACK_REQUEST_TEMPLATE = "{serviceUrl}/{userId}/{date}";

	private final String tickTackUrl;

	public TickTackRequester(String tickTackUrl) {
		this.tickTackUrl = tickTackUrl;
	}

	public List<Punch> doRequest(String userId, LocalDate punchDate) throws IOException {

		HttpClient httpClient = new DefaultHttpClient();

		String requestUrl = String.format(TICK_TACK_REQUEST_TEMPLATE, tickTackUrl, userId, punchDate);

		HttpResponse response = httpClient.execute(new HttpGet(requestUrl));
		InputStream inputStream = response.getEntity().getContent();
		String json = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
		return new Gson().fromJson(json, new TypeToken<ArrayList<Punch>>(){}.getType());
	}
}
