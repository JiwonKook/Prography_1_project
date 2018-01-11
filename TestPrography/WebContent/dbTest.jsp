<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*, geo.*,org.json.simple.*,java.net.URLEncoder"%>
    <% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:useBean id="datas" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="g_dto" class="geo.MarkerDTO" /> 
<jsp:useBean id="g_dao" class="geo.GeoDAO" />
<jsp:setProperty name="g_dto"  property="*" />
<title>Insert title here</title>
</head>
<body>

<%
	ArrayList<MarkerDTO> markerList = (ArrayList<MarkerDTO>)g_dao.getMDBList();
	JSONArray jarr = new JSONArray();	
	JSONObject obj = new JSONObject();
	
	
	// index를 부여하여 해당 페이지에서 10개의 항목만 출력하도록 함
	int count = markerList.size();
	JSONObject jsonMain = new JSONObject();
				
				
		for (int j = 0; j < count; j++) {
			MarkerDTO list = markerList.get(j);
			JSONObject jsonObject = new JSONObject();
					
			jsonObject.put("tvname", URLEncoder.encode(list.getName()));
			jsonObject.put("laitude",URLEncoder.encode(list.getLat()+""));
			jsonObject.put("longitude",URLEncoder.encode(list.getLon()+""));
			jsonObject.put("catagory", URLEncoder.encode(list.getCatagory()));
				
			jarr.add(jsonObject); 
		}
			
		
		jsonMain.put("sendData", jarr);
	
		out.print(jsonMain);
		
		
%>
</body>
</html>