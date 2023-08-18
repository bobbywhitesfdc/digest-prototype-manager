package bobby.prototype.digest.servlet.bean;

import java.util.ArrayList;
import java.util.TreeSet;

import bobby.sfdc.prototype.NewsFeedOptions;
import bobby.sfdc.prototype.json.CommunitiesPage;
import bobby.sfdc.prototype.json.Community;
import bobby.sfdc.prototype.json.FeedDirectory;
import bobby.sfdc.prototype.json.FeedDirectoryItem;
import bobby.sfdc.prototype.json.Organization;
import bobby.sfdc.prototype.json.UserProfile;
import bobby.sfdc.prototype.oauth.json.OAuthTokenSuccessResponse;

/**
 * Connection Context is used to allow the servlet to pass enough information to the JSP to construct the Digest Options dialog
 * 
 * @author bobby.white
 *
 */
public class ConnectionContext {
	ArrayList<NetworkInfo> networkScopes=new ArrayList<NetworkInfo>();
	String loginUrl="";
	String organizationName="";
	String username="";
	String email="";
	String userType="";
	String displayName="";
	String organizationId="";
	String selectedNetwork="";
	String selectedFeed="";
	private FeedDirectory feeds;
	/**
	 * user set options from the forms, ensure that it's never null
	 **/
	private NewsFeedOptions newsFeedOptions= new NewsFeedOptions();
	
	/**
	 * Returns true of if the user is considered internal, otherwise returns false.  Internal in this context means a user that has some access to "Network 0" - the internal Chatter Feeds.  
	 * Fully licensed users would be expected to return true.
	 * Chatter External users would be expected to return true.
	 * Chatter Free users would be expected to return true.
	 * Essentially, only "Portal" users would be expected to return false;
	 *   
	 * @return
	 */
	public boolean isInternalUser() {
		return !(this.userType != null && this.userType.compareTo("Portal")==0);
	}
	   

	
	/**
	 * Uber constructor to initialize everything we care about in one shot
	 * 
	 * @param auth
	 * @param profile
	 * @param orgInfo
	 * @param communitiesPage
	 */
	public ConnectionContext(OAuthTokenSuccessResponse auth,
			UserProfile profile, Organization orgInfo,
			CommunitiesPage communitiesPage) {
		
		this.loginUrl = auth.getInstance_url();
		
		if (profile != null && profile.getUserDetail() != null) {
		   this.displayName = profile.getUserDetail().getDisplayName();
		   this.email = profile.getUserDetail().getEmail();
		   this.userType = profile.getUserDetail().getUserType();
		   this.username = profile.getUserDetail().getUsername();
	    }
		
		this.organizationId = orgInfo.getOrgId();
		this.organizationName = orgInfo.getName();
		
		
		
		int activeCommunities=0;
		for(Community community : communitiesPage.getCommunities()) {
			if (community.isActive()) {
				if (activeCommunities==0 && isInternalUser()) {
					// Generate a dummy entry for the Internal Org, but only if they aren't a "Portal" user type (e.g. external member of the Community)
					this.networkScopes.add(new NetworkInfo(NetworkInfo.NETWORK0,"Internal Network","Internal Network"));
				}
				activeCommunities++;
				this.networkScopes.add(new NetworkInfo(community.getId(),community.getName(),community.getDescription()));
			}
		}
		
	}
	/**
	 * @return the networkScopes
	 */
	public ArrayList<NetworkInfo> getNetworkScopes() {
		return networkScopes;
	}
	/**
	 * @param networkScopes the networkScopes to set
	 */
	public void setNetworkScopes(ArrayList<NetworkInfo> networkScopes) {
		this.networkScopes = networkScopes;
	}
	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		return loginUrl;
	}
	/**
	 * @param loginUrl the loginUrl to set
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	/**
	 * @return the organizationName
	 */
	public String getOrganizationName() {
		return organizationName;
	}
	/**
	 * @param organizationName the organizationName to set
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the organizationId
	 */
	public String getOrganizationId() {
		return organizationId;
	}
	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 50;
		StringBuilder builder = new StringBuilder();
		builder.append("ConnectionContext [");
		if (networkScopes != null) {
			builder.append("networkScopes=");
			builder.append(networkScopes.subList(0,
					Math.min(networkScopes.size(), maxLen)));
			builder.append(", ");
		}
		if (loginUrl != null) {
			builder.append("loginUrl=");
			builder.append(loginUrl);
			builder.append(", ");
		}
		if (organizationName != null) {
			builder.append("organizationName=");
			builder.append(organizationName);
			builder.append(", ");
		}
		if (username != null) {
			builder.append("username=");
			builder.append(username);
			builder.append(", ");
		}
		if (email != null) {
			builder.append("email=");
			builder.append(email);
			builder.append(", ");
		}
		if (userType != null) {
			builder.append("userType=");
			builder.append(userType);
			builder.append(", ");
		}
		if (displayName != null) {
			builder.append("displayName=");
			builder.append(displayName);
			builder.append(", ");
		}
		if (organizationId != null) {
			builder.append("organizationId=");
			builder.append(organizationId);
			builder.append(", ");
		}
		if (selectedNetwork != null) {
			builder.append("selectedNetwork=");
			builder.append(selectedNetwork);
			builder.append(", ");
		}
		if (selectedFeed != null) {
			builder.append("selectedFeed=");
			builder.append(selectedFeed);
			builder.append(", ");
		}
		if (feeds != null) {
			builder.append("feeds=");
			builder.append(feeds);
			builder.append(", ");
		}
		if (newsFeedOptions != null) {
			builder.append("newsFeedOptions=");
			builder.append(newsFeedOptions);
		}
		builder.append("]");
		return builder.toString();
	}



	/**
	 * @return the selectedNetwork
	 */
	public String getSelectedNetwork() {
		return selectedNetwork;
	}



	/**
	 * @param selectedNetwork the selectedNetwork to set
	 */
	public void setSelectedNetwork(String selectedNetwork) {
		if (selectedNetwork==null) {
			selectedNetwork="";
		}
		this.selectedNetwork = selectedNetwork;
	}
	
	/**
	 * @return List of Available Feeds for the selected Network
	 */
	public ArrayList<FeedSelection> getAvailableFeeds() {
		
		TreeSet<FeedSelection> result = new TreeSet<FeedSelection>();
		if (this.feeds != null && this.feeds.getFeeds() != null) {
			String defaultFeedType = getDefaultFeedTypeForNetwork();
			for(FeedDirectoryItem item : feeds.getFeeds()) {
				
				FeedSelection feedSelection = new FeedSelection(item);
				feedSelection.setDefaultSelection(feedSelection.getFeedType().compareTo(defaultFeedType)==0);
				feedSelection.setSelected(getNewsFeedOptions().getFeedResourceOverride() != null 
						&& feedSelection.getFeedElementsUrl().compareTo(getNewsFeedOptions().getFeedResourceOverride())==0); // Matches Current Selection
				result.add(feedSelection);
			}	
		}
		/* This will construct a list that is in order */
		ArrayList<FeedSelection> sortedList = new ArrayList<FeedSelection>(result);
		for (FeedSelection current : sortedList) {
			System.out.println(current.toString());
		}
		return sortedList;
	}

	public String getDefaultFeedTypeForNetwork() {
		return this.selectedNetwork.isEmpty() ? "News" : "UserProfile";
	}



	/**
	 * @return the feeds
	 */
	public FeedDirectory getFeeds() {
		return feeds;
	}


	/**
	 * @param feeds the feeds to set
	 */
	public void setFeeds(FeedDirectory feeds) {
		this.feeds = feeds;
	}



	public NewsFeedOptions getNewsFeedOptions() {
		return this.newsFeedOptions;
	}



	/**
	 * @param newsFeedOptions the newsFeedOptions to set
	 */
	public void setNewsFeedOptions(NewsFeedOptions newsFeedOptions) {
		this.newsFeedOptions = newsFeedOptions;
	}



	/**
	 * @return the selectedFeed
	 */
	public String getSelectedFeed() {
		return selectedFeed;
	}



	/**
	 * @param selectedFeed the selectedFeed to set
	 */
	public void setSelectedFeed(String selectedFeedUrl) {
		String feedLabel = "";
		if (selectedFeed !=null) {
			for(FeedSelection availableFeed : this.getAvailableFeeds()) {
				if (availableFeed.getFeedElementsUrl().compareTo(selectedFeedUrl)==0) {
					feedLabel = availableFeed.getLabel();
					break;
				}
			}
		}
		this.selectedFeed = feedLabel;
	}
}
