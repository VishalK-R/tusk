package com.abc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Home extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String sql;
	private static PreparedStatement stmt;
	private static Connection con;
	private static ResultSet set;
	private static PrintWriter writer;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		
		try {
			con = (Connection) req.getAttribute("con");
			writer = resp.getWriter();
			String userName_email = req.getParameter("un");
			display(userName_email);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void display(String userName_email) throws SQLException {
		sql = "select * from `userinfo` where `email` = ? or `username` = ?";
		stmt = con.prepareStatement(sql);
		stmt.setString(1,userName_email);
		stmt.setString(2,userName_email);
		set = stmt.executeQuery();
		String s1=String.format("%1$-15s%2$-15s%3$-15s%4$-15s%5$-45s%6$-20s%7$-4s%8$-15s","First Name","Middle Name","Last Name","User Name","Email","Password","Age","Phone Number");
		writer.println(s1);
		
		set.next();
		String fn = set.getString(1);
		String mn = set.getString(2);
		if(mn.length()> 0) {

		}else {
			mn = "Not Specified";
		}
		String ln = set.getString(3);
		if(ln.length()> 0)  {

		}else {
			ln = "Not Specified";
		}
		String un = set.getString(4);
		String []emal = set.getString(5).split("@");
		String em = "";
			for(int i = 0; i < emal[0].length();i++) {
			if(i<3) {
				em+=emal[0].charAt(i);
			}else {
				em+='*';
			}
		}
		em+='@'+emal[1];
		String pas1 = set.getString(6);
		String pas = "";
		for (int i = 0; i < pas1.length(); i++) {
			pas+="*";
		}
		int age = set.getInt(7);
		String pn = set.getString(8);
		
		String s2 = String.format("%1$-15s%2$-15s%3$-15s%4$-15s%5$-45s%6$-20s%7$-4s%8$-15s",fn,mn,ln,un,em,pas,age,pn);
		
		writer.println(s2);
		
	}
	
}
