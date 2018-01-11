<%@ page language="java" contentType="text/html; charset=ascii"
    pageEncoding="UTF-8" import="java.util.*, geo.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setCharacterEncoding("UTF-8"); %>

<%-- geoloation 관련 useBean --%>
<jsp:useBean id="g_dto" class="geo.MarkerDTO" /> 
<jsp:useBean id="g_dao" class="geo.GeoDAO" />
<jsp:setProperty name="g_dto"  property="*" />




<%
	String action = request.getParameter("action");
	String signal = request.getParameter("signal");

		if(action != null) {
			if(action.equals("list")){
				if(!g_dao.getMDBList().isEmpty()){
					dto = g_dao.getMDBList();
					request.setAttribute("datas", dto);
					
					pageContext.forward("dbTest.jsp?");
					
				}
				else
					throw new Exception("db오류+dtor");
				
			}
		}

%>