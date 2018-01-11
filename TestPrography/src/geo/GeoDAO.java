package geo;

import java.util.ArrayList;
//import org.jcp.xml.dsig.internal.dom.*;
import java.sql.*;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.json.simple.*;

import java.io.*;
import java.net.URLEncoder;
import javax.servlet.jsp.JspWriter;

public class GeoDAO {
	
	Connection conn = null;
	PreparedStatement pstmp = null;
	ResultSet rs = null; 
	
	String jdbc_driver="com.mysql.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://localhost/test";
	
	void connect() {
		try {
			Class.forName(jdbc_driver);
			conn=DriverManager.getConnection(jdbc_url,"****","****");		
			System.out.println("접속 성공");
		}catch(Exception e) {
			System.out.println("접속 실패");
			e.printStackTrace();
		}
	}
	
	void disconnect() {
		if(pstmp != null) {
			try {
				pstmp.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null) {
			try {
				conn.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//select * from all 
	public ArrayList<MarkerDTO> getMDBList(){
		connect();
		ArrayList<MarkerDTO> dto = new ArrayList<MarkerDTO>();
		
		String sql="select tvname,latitude,longitude,catagory from travelDB";
		
		try {
			pstmp = conn.prepareStatement(sql);
			ResultSet rs = pstmp.executeQuery();
			JSONArray jarr = new JSONArray();	
			JSONObject obj = new JSONObject();
		
			MarkerDTO temp = new MarkerDTO();
			
			while(rs.next()) {
				MarkerDTO markerDTO = new MarkerDTO();
				
				
				markerDTO.setName(rs.getNString("tvname"));
				markerDTO.setLat(rs.getDouble("latitude"));
				markerDTO.setLon(rs.getDouble("longitude"));
				markerDTO.setCatagory(rs.getString("catagory"));
				dto.add(markerDTO);
				
				//JSONObject obj =new JSONObject();
				obj.put("tvname", temp.name);
				obj.put("age", temp.catagory);
				if(obj != null)
					jarr.add(obj);
				
			}
			
			obj.put("sendData", jarr);
			
			
			
			rs.close();
			
			
		}catch(SQLException e) {
			
			e.printStackTrace();
			
		}finally {
			disconnect();
		}
		
		return dto;
		
	}
	
	
	

}
