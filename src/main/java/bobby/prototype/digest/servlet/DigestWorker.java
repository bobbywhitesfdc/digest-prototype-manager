/**
 * 
 */
package bobby.prototype.digest.servlet;

import java.util.logging.Level;
import java.util.logging.Logger;

import bobby.sfdc.prototype.DigestMaker;
import bobby.sfdc.prototype.NewsFeedOptions;
import bobby.sfdc.prototype.Scheme4Factory;
import bobby.sfdc.prototype.json.FeedElementPage;
import bobby.sfdc.prototype.json.UserProfile;
import bobby.sfdc.prototype.mail.SimpleMailer;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.oauth.json.OAuthTokenSuccessResponse;


/**
 * 
 * DigestWorker is responsible for running the Digest
 * 
 * @author bobby.white
 *
 */
public class DigestWorker implements Runnable {

	private final OAuthTokenSuccessResponse auth;
	private final NewsFeedOptions options;
	private static final Logger _logger = Logger.getLogger(DigestWorker.class.getName());
	private static final String HTML_LINE_BREAK = "<br/>\n";
	
	public DigestWorker(OAuthTokenSuccessResponse auth, NewsFeedOptions options) {
		this.auth=auth;
		this.options=options;
	}

	/**
	 * Run the Digest
	 */
	public void run() {
		DigestMaker maker = new DigestMaker();
		maker.setAuthToken(auth);
		FeedElementPage news=null;
		UserProfile userProfile=null;
		try {

			news = maker.getNewsFeed(options);
			
			userProfile = maker.getUserProfile(maker.getUserID(),options.getNetwork());

			if (news == null) {
				_logger.log(Level.WARNING,"News is null");
				sendErrorEmail("Error Fetching NewsFeed",userProfile,news,null);
				return;
			} else if (news.getElements().length==0) {
				_logger.info("News is empty, no elements");
				sendErrorEmail("News feed is empty",userProfile,news,null);
				return;
			}
			maker.fetchExtraRecordContext(news);
			maker.produceDigest(news, new Scheme4Factory(maker.getUserID()), options, userProfile, null);
			_logger.info("Digest sent to: " + userProfile.toString());
		} catch (AuthenticationException e) {
			_logger.log(Level.SEVERE,e.getMessage());
			sendErrorEmail("Error Authenticating",userProfile,news,e);

		} catch (Throwable t) {
			_logger.log(Level.SEVERE,t.getMessage());
			sendErrorEmail("Exception Caught",userProfile,news,t);	
		}
	}

	/**
	 * Send an Error Email to aid in diagnostics
	 * 
	 * @param subject
	 * @param userProfile
	 * @param news
	 * @param t
	 */
	private void sendErrorEmail(String subject, UserProfile userProfile, FeedElementPage news, Throwable t) {
		String toAddress = "bobby.white@salesforce.com";
		String ccAddress = toAddress;
		
		if (userProfile!=null && userProfile.getUserDetail() != null
				&&  userProfile.getUserDetail().getEmail() != null) {
			toAddress = userProfile.getUserDetail().getEmail();
		}
		
		StringBuffer msg = new StringBuffer("<html><body><h1>Error generating digest</h1><br/>");
		if (userProfile != null) {
			msg.append(userProfile.toString()).append(HTML_LINE_BREAK);
		}
		
		if (this.options != null) {
			msg.append(HTML_LINE_BREAK);
			msg.append(options.toString()).append(HTML_LINE_BREAK);
		}
		
		if (this.auth != null) {
			msg.append(this.auth.getId()).append(HTML_LINE_BREAK);
			msg.append(this.auth.getInstance_url()).append(HTML_LINE_BREAK);
		}
		
		if (news != null) {
			msg.append(news.toString()).append(HTML_LINE_BREAK);
		}
		
		if (t != null) {
			if (t.getMessage() != null) {
				msg.append("Exception Message: " + t.getMessage());
			} else {
				msg.append("Exception Type:" + t.getClass().getName());
			}
			msg.append(HTML_LINE_BREAK);
			
			for(StackTraceElement elem : t.getStackTrace()) {
				msg.append(elem.toString()).append(HTML_LINE_BREAK);
			}

		}
		
		msg.append(HTML_LINE_BREAK);
		msg.append("</body></html>");

		SimpleMailer mailer = new SimpleMailer();
		mailer.sendEmailMessage("Digest Prototype Error Mail " + subject
				, msg.toString()
				, toAddress
				, ccAddress);
		
	}
}
