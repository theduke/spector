package at.theduke.spector.eventwriter;

import java.util.ArrayList;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import at.theduke.spector.Event;

public class ElasticSearchWriter extends BaseWriter implements Writer {
	
	/**
	 * Unique writer identifier.
	 */
	static final String name = "elasticsearch";
	
	String esCluster = "elasticsearch";
	
	String esIndex = "spector";
	String esType = "event";
	
	String esHost = "localhost";
	int esPort = 9300;
	
	
	Client esClient;
	
	public ElasticSearchWriter(String host, int port)  {
		this.esHost = host;
		this.esPort = port;
	}
	
	@Override
	protected void connect() {
		
		Settings settings = ImmutableSettings.settingsBuilder()
		        .put("cluster.name", esCluster).build();
		
		esClient = new TransportClient(settings)
        	.addTransportAddress(new InetSocketTransportAddress(esHost, esPort));
		
		connected = true;
		
		logger.debug("Connected to ElasticSearch server " + esHost + " on port " + Integer.toString(esPort));
	}
	
	@Override
	protected void close() {
		esClient.close();
		connected = false;
	}
	
	protected ArrayList<Event> executeFlush(ArrayList<Event> events) {
		logger.debug("ElasticSearchWriter is flushing " + events.size() + " events");
		
		BulkRequestBuilder bulkRequest = esClient.prepareBulk();
		
		for (Event event : events) {
			bulkRequest.add(esClient.prepareIndex(esIndex, esType).setSource(event.serialize()));
		}
		
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
		    logger.error("Errors ocurred while writing to ElasticSerach");
		}
		
		return null;
	}
	
	/**
	 * Getters and setters.
	 */

	public String getEsCluster() {
		return esCluster;
	}

	public void setEsCluster(String esCluster) {
		this.esCluster = esCluster;
	}

	public String getEsIndex() {
		return esIndex;
	}

	public void setEsIndex(String esIndex) {
		this.esIndex = esIndex;
	}

	public String getEsType() {
		return esType;
	}

	public void setEsType(String esType) {
		this.esType = esType;
	}

	public String getEsHost() {
		return esHost;
	}

	public void setEsHost(String esHost) {
		this.esHost = esHost;
	}

	public int getEsPort() {
		return esPort;
	}

	public void setEsPort(int esPort) {
		this.esPort = esPort;
	}

	public Client getEsClient() {
		return esClient;
	}

	public void setEsClient(Client esClient) {
		this.esClient = esClient;
	}
	
	
}
