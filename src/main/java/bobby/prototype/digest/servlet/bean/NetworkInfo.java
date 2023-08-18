package bobby.prototype.digest.servlet.bean;

/**
 * Bean for Network (aka Community) information
 * @author bobby.white
 *
 */
public class NetworkInfo {
	public static final String NETWORK0 = "";
	String name;
	String networkId;
	String url;
	String description;
	
	/**
	 * Public constructor
	 * 
	 * @param networkId
	 * @param name
	 * @param description
	 */
	public NetworkInfo(String networkId, String name, String description) {
		this.networkId = networkId;
		this.name = name;
		this.description = description;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the networkId
	 */
	public String getNetworkId() {
		return networkId;
	}
	/**
	 * @param networkId the networkId to set
	 */
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NetworkInfo [");
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (networkId != null) {
			builder.append("networkId=");
			builder.append(networkId);
			builder.append(", ");
		}
		if (url != null) {
			builder.append("url=");
			builder.append(url);
			builder.append(", ");
		}
		if (description != null) {
			builder.append("description=");
			builder.append(description);
		}
		builder.append("]");
		return builder.toString();
	}
}
