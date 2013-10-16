package weibo4j.examples.timeline;

import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

public class UpdateStatus {

	public static void main(String[] args) {
		String access_token = "2.006R5mzD68vy_C0c8d0b15a2zwJqUE";
		String statuses = "javacode发第一条微薄";
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		try {
			Status status = tm.UpdateStatus(statuses);
			Log.logInfo(status.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}	}

	
	public static void getCode()
	{
		try {
			Oauth oauth = new Oauth();
			oauth.authorize("code","hello","");
			
			
			
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}
