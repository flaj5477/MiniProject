package co.bucketstargram.command.myBucket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import co.bucketstargram.common.Command;
import co.bucketstargram.dao.BucketDao;
import co.bucketstargram.dao.ReplyDao;

public class GetReply implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		String userId = (String) session.getAttribute("userid");
		System.out.println("GetReply.java | usersId = " + userId);
		String imageId = request.getParameter("imageId");
		System.out.println("GetReply.java | imageId = " + imageId);
		getJSON(imageId, userId);
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(getJSON(imageId, userId));		
	}
	
	private String getJSON(String imageId, String userId) {

		BucketDao bDao = new BucketDao();
		ReplyDao reDao = new ReplyDao();

		ArrayList<HashMap<String, String>> bucketInfoList = bDao.getBucketInfo(imageId, userId);
		ArrayList<HashMap<String, String>> replyInfoList = reDao.getReplyInfo(imageId, userId);	
		
		Iterator<Map.Entry<String, String>> entries;
		Entry<String, String> entry;
		
		JSONArray jsonArray = new JSONArray();
		JSONObject outerJsonObj = null;
		JSONObject innerJsonObj = new JSONObject();
		
		for(int i = 0; i < bucketInfoList.size(); i++){
			for( Entry<String, String> element : bucketInfoList.get(i).entrySet() ) {
				String key = element.getKey();
				String value = element.getValue();
				
				System.out.println(String.format("["+i+"번] 키 : %s | 값 : %s", element.getKey(), element.getValue()));
				innerJsonObj.put(key, value);
			}
		}
		System.out.println(innerJsonObj.toString());
		
		for(HashMap<String, String> bucket : bucketInfoList) {
			entries = bucket.entrySet().iterator();
			entry = (Entry<String, String>)entries.next();
			
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println("key = " + key + " | " + "val = " + value);
			innerJsonObj = new JSONObject();
			outerJsonObj = new JSONObject();
			
			innerJsonObj.put(key, value);
			outerJsonObj.put("bucket", innerJsonObj);
			jsonArray.add(outerJsonObj);
		}
		
		for(HashMap<String, String> reply : replyInfoList) {
			entries = reply.entrySet().iterator();
			entry = (Entry<String, String>)entries.next();
			
			String key = entry.getKey();
			String value = entry.getValue();
			innerJsonObj = new JSONObject();
			outerJsonObj = new JSONObject();
			
			innerJsonObj.put(key, value);
			outerJsonObj.put("reply", innerJsonObj);
			jsonArray.add(outerJsonObj);
		}
		
		//System.out.println(jsonArray.toString());
		
		return jsonArray.toString();
	}
}












//package co.bucketstargram.command.myBucket;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.json.simple.JSONObject;
//
//import co.bucketstargram.common.Command;
//import co.bucketstargram.dao.ReplyDao;
//import co.bucketstargram.dto.ReplyDto;
//
//public class GetReply implements Command {
//
//	@Override
//	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		HttpSession session = request.getSession(true);
//		String userId = (String) session.getAttribute("userid");
//		System.out.println("GetReply.java | usersId = " + userId);
//		response.setContentType("text/html;charset=UTF-8");
//		String imageId = request.getParameter("imageId");
//		System.out.println("GetReply.java | imageId = " + imageId);
//		response.getWriter().write(getJSON(imageId, userId));
//		
//		String data;
//		JSONObject key = new JSONObject();
//		JSONObject value = new JSONObject();
//		
//	}
//	
//	private String getJSON(String imageId, String userId) {
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		if(imageId == null) {
//			imageId="";
//		}
//		
//		StringBuffer result = new StringBuffer("");
////		result.append("{\"result\":[");
////		ReplyDao dao = new ReplyDao();
////		ArrayList<ReplyDto> replyList = dao.select(imageId, userId);
////		for(int i =0; i<replyList.size();i++) {
////			//System.out.println("replyList.get("+i+").getReMemberId() = " + replyList.get(i).getReMemberId());
////			result.append("[{\"value\": \"" + replyList.get(i).getReMemberId()+ "\"},");
////			//System.out.println("replyList.get("+i+").getReReplyContents() = " + replyList.get(i).getReReplyContents());
////			if((i+1)!=replyList.size()) {
////				result.append("{\"value\": \"" + replyList.get(i).getReReplyContents()+ "\"}],");
////			}else {
////				result.append("{\"value\": \"" + replyList.get(i).getReReplyContents()+ "\"}]");
////			}
////		}
////		result.append("]}");
//		
//		//result.append("{\"result\":[");
//		ReplyDao dao = new ReplyDao();
//		ArrayList<ReplyDto> replyList = dao.select(imageId, userId);
//		for(int i =0; i<replyList.size();i++) {
//			if((i+1)!=replyList.size()) {
//				if(i==0) {
//					//댓글이 두개 이상인데 처음 시작일 때
//					result.append("[{\"reMemberId\":\"" + replyList.get(i).getReMemberId() + "\", \"reReplyContent\":\"" + replyList.get(i).getReReplyContents() + "\", \"reWriteDate\":\"" + replyList.get(i).getReWriteDate() +"\"}, ");
//				}else {
//					//댓글이 두개 이상인데 두 번째 이상 시작일 때
//					result.append("{\"reMemberId\":\"" + replyList.get(i).getReMemberId() + "\", \"reReplyContent\":\"" + replyList.get(i).getReReplyContents() + "\", \"reWriteDate\":\"" + replyList.get(i).getReWriteDate() +"\"}, 	");
//				}
//				
//			}else if((i+1) == replyList.size()){
//				if(replyList.size() == 1) {
//					//댓글 하나만 달렸을 경우
//					result.append("[{\"reMemberId\":\"" + replyList.get(i).getReMemberId() + "\", \"reReplyContent\":\"" + replyList.get(i).getReReplyContents() + "\", \"reWriteDate\":\"" + replyList.get(i).getReWriteDate() +"\"}]");;
//				}else {
//					//댓글이 두개 이상 달렸는데 마지막 녀석일 때
//					result.append("{\"reMemberId\":\"" + replyList.get(i).getReMemberId() + "\", \"reReplyContent\":\"" + replyList.get(i).getReReplyContents() + "\", \"reWriteDate\":\"" + replyList.get(i).getReWriteDate() +"\"}]");;	
//				}				
//			}
//		}
//		//result.append("]}");
//		return result.toString();
//	}
//}
