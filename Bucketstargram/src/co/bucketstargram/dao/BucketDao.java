package co.bucketstargram.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import co.bucketstargram.dto.BucketDto;

public class BucketDao {
    Connection conn = null; // DB연결된 상태(세션)을 담은 객체
    PreparedStatement psmt = null;  // SQL 문을 나타내는 객체
    ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
    
	public BucketDao() {
		// TODO Auto-generated constructor stub
		try {
            String user = "lee"; 
            String pw = "1234";
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            
            Class.forName("oracle.jdbc.driver.OracleDriver");        
            conn = DriverManager.getConnection(url, user, pw);
            
        } catch (ClassNotFoundException cnfe) {
            System.out.println("DB 드라이버 로딩 실패 :"+cnfe.toString());
        } catch (SQLException sqle) {
            System.out.println("DB 접속실패 : "+sqle.toString());
        } catch (Exception e) {
            System.out.println("Unkonwn error");
            e.printStackTrace();
        }
	}
	
	private void close() {
		// TODO Auto-generated method stub
		try {
			if(rs != null) rs.close();
			if(psmt != null) psmt.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean insert(BucketDto bucket) {
		// TODO Auto-generated method stub
		boolean insertSuccess = false;
		
		try {
			psmt = conn.prepareStatement("INSERT INTO bucket_info_tb(bucket_id, bucket_member_id, bucket_title, bucket_contents, bucket_type, bucket_image_path) values(?, ?, ?, ?, ?, ?)");
			// 매개변수로 전달된 데이터를 쿼리문의 물음표에 값 매핑
			psmt.setString(1, bucket.getBucketId());
			psmt.setString(2, bucket.getBucketMemberId());
			psmt.setString(3, bucket.getBucketTitle());
			psmt.setString(4, bucket.getBucketContents());
			psmt.setString(5, bucket.getBucketType());
			psmt.setString(6, bucket.getBucketImagePath());
			// 쿼리 수행
			int insertNum = psmt.executeUpdate();
			if(insertNum > 0) {
				insertSuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return insertSuccess;
		} finally {
			close();
		}

		return insertSuccess;
	}

	public ArrayList<BucketDto> select(String userid) {
		// TODO Auto-generated method stub
		ArrayList<BucketDto> bucketList = null;
		BucketDto bucket = null;
		String sql = "SELECT * FROM bucket_info_tb WHERE bucket_member_id = ?";
		
		try {
			bucketList = new ArrayList<BucketDto>();
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, userid);
			rs = psmt.executeQuery();
			while(rs.next()) {
				bucket = new BucketDto();
				bucket.setBucketId(rs.getString("BUCKET_ID"));
				bucket.setBucketMemberId(rs.getString("BUCKET_MEMBER_ID"));
				bucket.setBucketTitle(rs.getString("BUCKET_TITLE"));
				bucket.setBucketContents(rs.getString("BUCKET_CONTENTS"));
				bucket.setBucketType(rs.getString("BUCKET_TYPE"));
				bucket.setBucketLike(rs.getInt("BUCKET_LIKE"));
				bucket.setBucketImagePath(rs.getString("BUCKET_IMAGE_PATH"));
				bucket.setBucketTag(rs.getString("BUCKET_TAG"));
				bucket.setBucketWriteDate(rs.getString("BUCKET_WRITE_DATE"));
				
				bucketList.add(bucket);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return bucketList;
	}

	public ArrayList<HashMap<String, String>> getBucketInfo(String imageId, String userId) {
		// TODO Auto-generated method stub'
		ArrayList<HashMap<String, String>> bucketInfoList = null;
		HashMap<String, String> bucket = null;
		String likeYN = getLikeYN(imageId, userId);
		
		String sql = "SELECT * FROM bucket_info_tb WHERE bucket_member_id = ? AND bucket_id = ?";
		
		try {
			bucketInfoList = new ArrayList<HashMap<String, String>>();
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, userId);
			psmt.setString(2, imageId);
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				bucket = new HashMap<String, String>();
				
				bucket.put("bucketId",rs.getString("BUCKET_ID"));
				bucket.put("bucketMemberId",rs.getString("BUCKET_MEMBER_ID"));
				bucket.put("bucket_title",rs.getString("BUCKET_TITLE"));
				bucket.put("bucket_contents",rs.getString("BUCKET_CONTENTS"));
				bucket.put("bucket_type",rs.getString("BUCKET_TYPE"));
				bucket.put("bucket_compliation",rs.getString("BUCKET_COMPLIATION"));
				bucket.put("bucket_like",rs.getString("BUCKET_LIKE"));
				bucket.put("bucketImagePath",rs.getString("BUCKET_IMAGE_PATH"));
				bucket.put("bucketTag",rs.getString("BUCKET_TAG"));
				bucket.put("bucketWriteDate",rs.getString("BUCKET_WRITE_DATE"));
				bucket.put("likeYN", likeYN);
				
				bucketInfoList.add(bucket);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return bucketInfoList;
	}
	
	private String getLikeYN(String imageId, String userId) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM member_wish_list_tb WHERE mwl_bucket_id = ? AND mwl_bucket_id = ?";
		String likeYN = "N";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, imageId);
			psmt.setString(2, userId);
			
			rs = psmt.executeQuery();
			if(rs.next()) {
				likeYN = "Y";
			}else {
				likeYN = "N";
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return likeYN;
	}

	public String insert(String bucketId, String userId) {
		// TODO Auto-generated method stub
		String reulst = "";
		String sql = "";
		
		try {
			sql = "INSERT INTO member_wish_list_tb VALUES(?, ?)";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, userId);
			psmt.setString(2, bucketId);
			
			int insertNum = psmt.executeUpdate();
			
			if(insertNum>0) {
				reulst = "InsertSuccess";
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			reulst = "InsertFail";
			
			return reulst;
		}finally {
			close();
		}
		return reulst;
	}
}
