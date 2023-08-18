package bobby.prototype.digest.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;

import bobby.prototype.digest.servlet.bean.ConnectionContext;
import bobby.sfdc.prototype.DigestMaker;
import bobby.sfdc.prototype.NewsFeedOptions;
import bobby.sfdc.prototype.NewsFeedOptions.Depth;
import bobby.sfdc.prototype.NewsFeedOptions.Frequency;
import bobby.sfdc.prototype.json.CommunitiesPage;
import bobby.sfdc.prototype.json.Community;
import bobby.sfdc.prototype.json.FeedDirectory;
import bobby.sfdc.prototype.json.Organization;
import bobby.sfdc.prototype.json.UserProfile;
import bobby.sfdc.prototype.oauth.AuthenticationHelper;
import bobby.sfdc.prototype.oauth.json.OAuthTokenSuccessResponse;


/**
 * Servlet implementation class OAuthCallback
 */
public class OAuthCallback extends HttpServlet {
	public static final String CONTEXT_BEAN = "connection";

	private static final String DIGEST_OPTIONS_RESOURCE = "digestOptions.jsp";
	private static final String DIGEST_OPTIONS2_RESOURCE = "digestOptions2.jsp";
	private static final String ERROR_RESOURCE = "/error.jsp";
	public static final String LOGIN_PAGE_RESOURCE = "login.jsp";
	public static final String RUN_PAGE_RESOURCE = "digestRunPage.jsp";
	public static final String RESULTS_PAGE_RESOURCE = "results.jsp";
	public static final String QUEUED_PAGE_RESOURCE= "digestRequested.jsp";


	private static final String DIGEST_MAKER_ACCESS_TOKEN_COOKIE = "DigestMaker.access_token";
	private static final long serialVersionUID = 1L;
	public static final String OAUTH_AUTHORIZE_RESOURCE = "/services/oauth2/authorize";
	public static final String CONSUMER_KEY = "consumer_key";
	private static final String CONSUMER_SECRET = "consumer_secret";
	private static final String DEFAULT_LOGINURL = "https://login.salesforce.com";
	private String consumerKey;
	private String consumerSecret;
	private static final Logger _logger = Logger.getLogger(OAuthCallback.class.getName());

	private static final String HTML_LINEBREAK = "<br>\n";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAuthCallback() {
        super();
        _logger.info("constructor");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("start")!=null) {
			processStartPage(request, response);
		} else if (request.getParameter("code")!=null){
			processOAuthCodeResponse(request, response);
		} else if (request.getParameter("optionsPage1")!=null){
			processOptionsPage1(request, response);
		} else if (request.getParameter("optionsPage2")!=null){
			processOptionsPage2(request, response);
		} else if (request.getParameter("run")!=null){
			processRunPage(request, response);
		} else {
		    echoRequest(request,response);
		}
	}

	private void processOptionsPage1(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			String submitValue = request.getParameter("optionsPage1");
			if (submitValue != null && submitValue.compareToIgnoreCase("Previous")==0) {
				forward(request,response,LOGIN_PAGE_RESOURCE);
				return;
			}
			OAuthTokenSuccessResponse auth = restoreState(request);
			
			if (auth == null) {
				_logger.info("No saved auth token found");
				forwardWithMessage(request,response,LOGIN_PAGE_RESOURCE,"Error:  No authentication token available");
				return;
			}
			
			/**
			 * take the input from the first form and update the session beans
			 */
		    updateSessionBeansPage1(request);

									
			forward(request,response,DIGEST_OPTIONS2_RESOURCE);
			
		} catch (Throwable e) {
			try {
				handleExceptionFlow(request,response,e);
			} catch (ServletException e1) {
				_logger.warning(e1.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param pageResource
	 * @param msg
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void forwardWithMessage(HttpServletRequest request,
			HttpServletResponse response, String pageResource,
			String msg) throws ServletException, IOException {
		
		if (!pageResource.startsWith("/")) {
			pageResource = "/" + pageResource;
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pageResource + "?msg=" + URLEncoder.encode(msg,"UTF-8"));
		
		dispatcher.forward(request, response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param pageResource
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void forward(HttpServletRequest request,
			HttpServletResponse response, String pageResource) throws ServletException, IOException {
		
		if (!pageResource.startsWith("/")) {
			pageResource = "/" + pageResource;
		}
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pageResource);
		
		dispatcher.forward(request, response);
	}

	private void processOptionsPage2(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			String submitValue = request.getParameter("optionsPage2");
			if (submitValue != null && submitValue.compareToIgnoreCase("Previous")==0) {
				forward(request,response,DIGEST_OPTIONS_RESOURCE);
				return;
			}
			OAuthTokenSuccessResponse auth = restoreState(request);
			
			if (auth == null) {
				_logger.info("No saved auth token found");
				forwardWithMessage(request,response,LOGIN_PAGE_RESOURCE,"Error:  No authentication token available");
				return;
			}
			
			/**
			 * take the input from the second form and update the session beans
			 */
		    updateSessionBeansPage2(request);

									
			forward(request,response,RUN_PAGE_RESOURCE);
			
		} catch (Throwable e) {
			try {
				handleExceptionFlow(request,response,e);
			} catch (ServletException e1) {
				_logger.warning(e1.getMessage());
			}
		}
	}


	/**
	 * Take the input from the first form and update the session beans
	 *  with additional context based on their choices on the first page
	 * @param request
	 * @throws Throwable 
	 */
	private void updateSessionBeansPage1(HttpServletRequest request) throws Throwable {
		NewsFeedOptions options = setDigestOptions(request,null);
		
		OAuthTokenSuccessResponse auth = restoreState(request);
		DigestMaker mgr = new DigestMaker();
		mgr.setAuthToken(auth);

		
		try {
			FeedDirectory feeds;
			if (options.getNetwork()==null || options.getNetwork().isEmpty()) {
				feeds = mgr.getFeedDirectory();
			} else {
				feeds = mgr.getCommunityFeedDirectory(options.getNetwork());
			}
			
			ConnectionContext bean = (ConnectionContext) request.getSession().getAttribute(CONTEXT_BEAN);
			bean.setNewsFeedOptions(options); // Must save the options from Page1
			bean.setFeeds(feeds);
			
			
			request.getSession().setAttribute(CONTEXT_BEAN, bean);

			
		} catch (Throwable e) {
			String msg = e.getMessage();
			if (msg == null) {
				msg = e.getClass().getName();
				msg += " " + e.getStackTrace()[0].toString();
			}
			_logger.log(Level.SEVERE,msg);
			throw e;
		}

		
	}
	
	/**
	 * Take the input from the first form and update the session beans
	 *  with additional context based on their choices on the first page
	 * @param request
	 * @throws Throwable 
	 */
	private void updateSessionBeansPage2(HttpServletRequest request) throws Throwable {
		
		OAuthTokenSuccessResponse auth = restoreState(request);
		DigestMaker mgr = new DigestMaker();
		mgr.setAuthToken(auth);

		
		try {			
			Object bean = request.getSession().getAttribute(CONTEXT_BEAN);
			if (bean == null) {
				throw new IllegalStateException("Expected bean of type ConnectionContext, found null");
				
			} else if (!(bean instanceof ConnectionContext)) {
				throw new IllegalStateException("Expected bean of type ConnectionContext");
			}
			
			ConnectionContext contextBean = (ConnectionContext) bean;
			
			if (request.getParameter("feedOption")!=null) {
				contextBean.setSelectedFeed(request.getParameter("feedOption"));
			}
			
			NewsFeedOptions options = contextBean.getNewsFeedOptions();
			contextBean.setNewsFeedOptions(setDigestOptions(request,options));

			
			request.getSession().setAttribute(CONTEXT_BEAN, bean);

			
		} catch (Throwable e) {
			_logger.log(Level.SEVERE,e.getMessage());
			throw e;
		}

		
	}


	/**
	 * Run the digest using the options specified by the user
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void processRunPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		try {
			String submitValue = request.getParameter("run");
			if (submitValue != null && submitValue.compareToIgnoreCase("Previous")==0) {
				forward(request,response,DIGEST_OPTIONS2_RESOURCE);
				return;
			}

			OAuthTokenSuccessResponse auth = restoreState(request);
			
			if (auth == null) {
				_logger.info("No saved auth token found");
				forwardWithMessage(request,response,LOGIN_PAGE_RESOURCE,"Error:  No authentication token available");
				return;
			}
			
			ConnectionContext contextBean = (ConnectionContext)request.getSession().getAttribute(CONTEXT_BEAN);
			NewsFeedOptions options = contextBean.getNewsFeedOptions();
			
			enqueueDigest(auth,options);
						
			forwardWithMessage(request,response,QUEUED_PAGE_RESOURCE,"Digest generation enqueued, please check your mailbox");
			
		} catch (Throwable e) {
			try {
				handleExceptionFlow(request,response,e);
			} catch (ServletException e1) {
				_logger.warning(e1.getMessage());
			}
		}
		
	}

	/**
	 * Send the user on to another page to handle the exception that has occurred
	 * 
	 * @param request
	 * @param response
	 * @param msg the message we'd like to include
	 * @throws IOException
	 * @throws ServletException
	 **/
	protected void handleExceptionFlow(HttpServletRequest request, HttpServletResponse response, Throwable t)
			throws IOException, ServletException {
		
		if (request == null) {
			throw new IllegalArgumentException("request cannot be null");
		}
		if (response == null) {
			throw new IllegalArgumentException("response cannot be null");
		}
		if (t == null) {
			throw new IllegalArgumentException("Exception cannot be null");
		}
		
		

		_logger.info("Forwarding to Url [" + ERROR_RESOURCE + "] with message [" + t + "]");
				
		String msg = renderMessageFromException(t);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(ERROR_RESOURCE + "?msg=" + URLEncoder.encode(msg,"UTF-8"));
		
		dispatcher.forward(request, response);
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	private String renderMessageFromException(Throwable t) {
		StringBuilder msg= new StringBuilder();
		
		if (t.getMessage()!=null) {
			msg.append(t.getMessage());		
		} else {
			msg.append(t.getClass().getName());
		}
		msg.append(HTML_LINEBREAK).append(HTML_LINEBREAK);
		
		for (StackTraceElement element : t.getStackTrace()) {
			msg.append(element.toString()).append(HTML_LINEBREAK);
		}
		
		return msg.toString();
	}

	/**
	 * Enqueue the digest to run in a separate thread
	 * @param auth
	 * @param options
	 */
	private void enqueueDigest(OAuthTokenSuccessResponse auth,
			NewsFeedOptions options) {
		
		Thread t = new Thread(new DigestWorker(auth,options));
		t.start();
		
	}

	/**
	 * Grab the Digest Settings from the request parameters
	 * Append to existing settings
	 * @param request
	 * @param options previously set options, if null a new one will be created
	 * @return
	 */
	protected NewsFeedOptions setDigestOptions(HttpServletRequest request, NewsFeedOptions options) {
		// Grab the Digest Settings from the request parameters
		if (options == null) {
		   options = new NewsFeedOptions();
		}
		
		Frequency frequency = Frequency.WEEKLY;
		if (request.getParameter("frequency") != null) {
			String value = request.getParameter("frequency");
			if (value.compareTo("D")==0) {
				frequency = Frequency.DAILY;
			} else if (value.compareTo("E")==0) {
				frequency = Frequency.EPOC;
			}
			options.setFrequency(frequency);
		}
		if (request.getParameter("depth")!=null) {
		   options.setDepth(request.getParameter("depth").compareTo("F")==0 ? Depth.FEWERUPDATES : Depth.ALLUPDATES);
		}
		
		if (request.getParameter("pageSize")!=null) {
		   options.setPageSize(getIntSetting(request,"pageSize",25,10,100));
		}
		if (request.getParameter("commentCount")!=null) {
		   options.setRecentCommentCount(getIntSetting(request,"commentCount",3,0,25));
		}
		
		if (request.getParameter("network")!=null) {
			options.setNetwork(request.getParameter("network"));
		}
		
		if (request.getParameter("feedOption")!=null) {
		   options.setFeedResourceOverride(request.getParameter("feedOption"));
		}
		
		if (request.getParameter("filter")!=null) {
			String value = request.getParameter("filter");
			NewsFeedOptions.FeedFilter filter= NewsFeedOptions.FeedFilter.NONE;
			if (value.compareTo(NewsFeedOptions.FeedFilter.NONE.getName())==0) {
				filter = NewsFeedOptions.FeedFilter.NONE;
			} else if (value.compareTo(NewsFeedOptions.FeedFilter.ALL_QUESTIONS.getName())==0) {
				filter = NewsFeedOptions.FeedFilter.ALL_QUESTIONS;
			} else if (value.compareTo(NewsFeedOptions.FeedFilter.SOLVED_QUESTIONS.getName())==0) {
				filter = NewsFeedOptions.FeedFilter.SOLVED_QUESTIONS;
			} else if (value.compareTo(NewsFeedOptions.FeedFilter.UNSOLVED_QUESTIONS.getName())==0) {
				filter = NewsFeedOptions.FeedFilter.UNSOLVED_QUESTIONS;
			} else if (value.compareTo(NewsFeedOptions.FeedFilter.UNANSWERED_QUESTIONS.getName())==0) {
				filter = NewsFeedOptions.FeedFilter.UNANSWERED_QUESTIONS;
			}
			options.setFilter(filter);
		}

		
		_logger.info(options.toString());
		
		return options;
	}

	/**
	 * Helper to safely check and initialize a Setting from Request that is an integer
	 * which is between the specified bounds
	 * @param request
	 * @param name
	 * @param defaultValue will be used if unspecified
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	private int getIntSetting(HttpServletRequest request, String name, int defaultValue,
			int lowerBound, int upperBound) {
		int retVal = defaultValue;
		
		if (request.getParameter(name)!=null) {
			try {
				retVal = Integer.parseInt(request.getParameter(name));
				retVal = Math.max(lowerBound, Math.min(upperBound, retVal));
			} catch (NumberFormatException e) {
				_logger.log(Level.WARNING,"Invalid parameter value " + name + " " + e.getMessage());
			}
			
		}
		return retVal;
	}

	/**
	 * Echo Request Parameters back to the caller as Text and the console
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	protected void echoRequest(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		
		Enumeration<String> params = request.getParameterNames();

		PrintWriter client = response.getWriter();
		client.println("---------Request Info ---------");
		client.println("URI="+request.getRequestURI());
		client.println("URL="+request.getRequestURL());
		client.println("ServletPath=" + request.getServletPath());
		client.println("---------URL Form Parameters---------");
		while (params.hasMoreElements()) {
			String thisParamName = params.nextElement();
			String thisParamValue = request.getParameter(thisParamName);
			String msg = "{" + thisParamName + " : " + thisParamValue + "}";
			System.out.println(msg);
			client.println(msg);
			
		}
		
		Enumeration<String> headers = request.getHeaderNames();
		
		client.println("---------Http Header Parameters---------");
		while (headers.hasMoreElements()) {
			String thisHeaderName = headers.nextElement();
			String thisHeaderValue = request.getHeader(thisHeaderName);
			String msg = "{" + thisHeaderName + " : " + thisHeaderValue + "}";
			System.out.println(msg);
			client.println(msg);
			
		}
		
		
	}
	
	/**
	 * Initiate the Overall OAuth2 Web Server Authentication Flow by Authorizing with Salesforce
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws Throwable 
	 */
	protected void processStartPage(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		
		try {
			String loginUrl = DEFAULT_LOGINURL;
			if (request.getParameter("login_url")!= null) {
				loginUrl = request.getParameter("login_url");
			}
			String authUrl = composeAuthorizeURL(loginUrl,getRedirectURL(request));
			_logger.info("Redirecting to: " + authUrl);
			response.sendRedirect(authUrl);

		} catch (URISyntaxException e) {
			e.printStackTrace(response.getWriter());
			_logger.warning(e.getMessage());
		}
		
		
	}

	/**
	 * @param loginUrl which Salesforce server should handle this login request
	 * @param redirectURL for the OAuth Callback
	 * @return a full URL to attempt
	 * @throws URISyntaxException
	 */
	protected String composeAuthorizeURL(String loginUrl, String redirectURL)
			throws URISyntaxException {
		URIBuilder theQuery;

		theQuery = new URIBuilder(loginUrl + OAUTH_AUTHORIZE_RESOURCE);
		theQuery.addParameter("response_type","code");
		theQuery.addParameter("client_id",this.consumerKey);
		theQuery.addParameter("state",loginUrl);
		theQuery.addParameter("redirect_uri", redirectURL);
		
		_logger.info("Authorize URL=" + theQuery.toString());
		return theQuery.toString();
	}
	
	/**
	 * Process the Response from OAuth Server initial Start Page Authentication
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	protected void processOAuthCodeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String code=request.getParameter("code");
	    String loginUrl=request.getParameter("state");
	    
	    AuthenticationHelper helper = new AuthenticationHelper();
	    OAuthTokenSuccessResponse result;
		try {
			result = helper.getAuthTokenFromCode(loginUrl, getRedirectURL(request), this.consumerKey, this.consumerSecret, code);
		    _logger.info(result.toString());
		    saveState(response,result);
		    
		    initializeSessionBeans(request, result);

		    
			response.sendRedirect(DIGEST_OPTIONS_RESOURCE);

		} catch (Throwable e) {
		    _logger.info(e.getMessage());
		    try {
				handleExceptionFlow(request, response, e);
			} catch (ServletException e1) {
				_logger.warning(e1.getMessage());
			}
		}
	        
	}

	/**
	 * Using the basic info from the Authorization, gather enough context to allow the User to set Page1 Digest Options
	 * 
	 * @param request
	 * @param auth
	 * @throws Throwable
	 */
	protected void initializeSessionBeans(HttpServletRequest request,
			OAuthTokenSuccessResponse auth) throws Throwable {
		/**
		 * Fetch the UserProfile and stash it in the Session for the JSP
		 * 
		 * it's needed to display who the user is + if there is a permissions issue,
		 *  this should expose it
		 */
			
		DigestMaker mgr = new DigestMaker();
		mgr.setAuthToken(auth);
		
		CommunitiesPage communitiesPage = mgr.getCommunitiesList();
					
		UserProfile profile=null;
		try {
			profile = mgr.getUserProfile(mgr.getUserID(),null);
		} catch (Exception e) {
			_logger.log(Level.WARNING,e.getMessage());
			if (communitiesPage != null && communitiesPage.getCommunities().length > 0 ) {
				for (Community current : communitiesPage.getCommunities()) {
					if (current.getId() !=null && !current.getId().isEmpty()) {
						profile = mgr.getUserProfile(mgr.getUserID(),current.getId());
						break;
					}
				}
			}
		}
		
		Organization orgInfo = mgr.getOrganizationInfo();
		
		
		ConnectionContext bean = new ConnectionContext(auth,profile,orgInfo,communitiesPage);
		
		request.getSession().setAttribute(CONTEXT_BEAN, bean);
		
		_logger.info("bean: " +bean.toString());
	}

	
	/**
	 * Store the AuthenticationToken in a Cookie
	 * 
	 * @param authTokenResponse
	 * @return
	 */
	private void saveState(HttpServletResponse response, OAuthTokenSuccessResponse authTokenResponse) {
		Cookie cookie = new Cookie(DIGEST_MAKER_ACCESS_TOKEN_COOKIE, authTokenResponse.encryptAuthToken());
		cookie.setSecure(false);
		cookie.setMaxAge(60*60*24*10); //10 days
		cookie.setComment("Chatter Digest Prototype state");
		response.addCookie(cookie);
	}
	

	/**
	 * Restore the AuthenticationToken state from Cookies
	 * 
	 * @param authTokenResponse
	 * @return null if not found, otherwise the last saved AuthToken
	 */
	private OAuthTokenSuccessResponse restoreState(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().compareTo(DIGEST_MAKER_ACCESS_TOKEN_COOKIE)==0) {
				   String encrypted = cookie.getValue();
				   return OAuthTokenSuccessResponse.decryptAuthToken(encrypted);	
				}
			}
		}
		return null;
	}


	/**
	 * What is the Redirect URL that is configured for this App?
	 * 
	 * It needs to be correct at runtime, so we must determine this dynamically
	 * 
	 * @return  fully qualified URL to the OAuth2.0 endpoint
	 */
	private String getRedirectURL(HttpServletRequest request) {
        String uri = request.getRequestURI();
        int localPort = request.getLocalPort();
        int remotePort = request.getRemotePort();
        String forwardedHeader = request.getHeader("x-forwarded-proto");
        String effectiveScheme = (forwardedHeader!=null ? forwardedHeader : request.getScheme());
        String effectivePort = ":" + (forwardedHeader!=null ? remotePort : localPort);
        String server = request.getServerName();
        
        if (forwardedHeader != null && effectiveScheme.compareToIgnoreCase("https")==0) {
     	   effectivePort = ""; // override
     	   _logger.fine("localPort="+localPort + " remotePort=" + remotePort + " effectiveScheme=" + effectiveScheme);
        }

        String url = effectiveScheme + "://" + server + effectivePort + uri;
        _logger.info("RedirectURL=" + url);
        return url;
	}
	

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		_logger.info("Init called");
		
		this.consumerKey = config.getInitParameter(CONSUMER_KEY);
		this.consumerSecret = config.getInitParameter(CONSUMER_SECRET);
		
		// override from environment if set
		if (System.getenv(CONSUMER_KEY) != null) {
			this.consumerKey = System.getenv(CONSUMER_KEY);
		}
		
		if (System.getenv(CONSUMER_SECRET) != null) {
			this.consumerSecret= System.getenv(CONSUMER_SECRET);
		}

		
		/**
		 * Initialize these "value lists" from the enums so they will be accessible to the JSP pages
		 */
		config.getServletContext().setAttribute("NewsFeedOptions.Frequency", NewsFeedOptions.Frequency.values());
		config.getServletContext().setAttribute("NewsFeedOptions.Depth", NewsFeedOptions.Depth.values());
		config.getServletContext().setAttribute("NewsFeedOptions.FeedFilter", NewsFeedOptions.FeedFilter.values());
	}
	
}
