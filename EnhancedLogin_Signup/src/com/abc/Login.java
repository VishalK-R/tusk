package com.abc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String sql;
	private static Connection con;
	private static PrintWriter writer;
	private static PreparedStatement stmt1;
	private static ResultSet set;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		
		ResourceBundle bundle = ResourceBundle.getBundle("com.abc.utilities.mysql");
		String url = bundle.getString("url");
		String user = bundle.getString("user");
		String pass = bundle.getString("password");
		
		String userName = req.getParameter("un");
		String password = req.getParameter("pas");
		
		
		try {
			con  = DriverManager.getConnection(url,user,pass);
			
			
			writer = resp.getWriter();
			
			
			if(doesntExists(userName)) {
				writer.println(userName+" doesn't exist... please input valid userName/email else try signing in....");
				
				resp.setContentType("text/html");
				RequestDispatcher rd = req.getRequestDispatcher("login.html");
				rd.include(req, resp);
			}else {
				sql = "select `invalidLogin` , `blocked` from `userinfo` where `username` = ? or `email` = ?";
				stmt1  = con.prepareStatement(sql);
				stmt1.setString(1, userName);
				stmt1.setString(2, userName);
				set = stmt1.executeQuery();
				set.next();
				int invalidLogin = set.getInt(1);
				String blocked = set.getString(2);
				if(blocked.equals("true")) {
					writer.println(userName+" is already blocked for security issues due to 3 Invalid Logins..Try contacting our support team to reset...");
				}else if(verify(userName,password)) {
					writer.println("Logged in successfully....");
					setInvalidLogin(userName);
					req.setAttribute("con", con);
					RequestDispatcher rd = req.getRequestDispatcher("home");
					rd.include(req, resp);
				}else {
						invalidLogin--;
						if(invalidLogin > 0) {
							writer.println(invalidLogin+" more Invalid Logins will block the account for security purpose.... Plz use valid password...");
							sql = "update `userinfo` set `invalidLogin` = ? where `username` = ? or `email` = ?;";
							stmt1 = con.prepareStatement(sql);
							stmt1.setInt(1, invalidLogin);
							stmt1.setString(2, userName);
							stmt1.setString(3, userName);
							stmt1.executeUpdate();
							
							
							resp.setContentType("text/html");
							RequestDispatcher rd = req.getRequestDispatcher("login.html");
							rd.include(req, resp);
						}else {
							writer.println(userName+" is blocked for security purpose due to 3 invalid logins.... Plz contact support team to verify your credentials...");
							sql = "update `userinfo` set `invalidLogin` = ? , `blocked` = 'true'   where `username` = ? or `email` = ?;";
							stmt1 = con.prepareStatement(sql);
							stmt1.setInt(1, invalidLogin);
							stmt1.setString(2, userName);
							stmt1.setString(3, userName);
							stmt1.executeUpdate();
						}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}finally {
			try {
				close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	private void close() throws SQLException {
		if(con!=null) {
			con.close();
		}
		
		if(stmt1!=null) {
			stmt1.close();
		}
		
		if(writer!=null) {
			writer.close();
		}
		
		if(set!=null) {
			set.close();
		}
	}

	
	private void setInvalidLogin(String userName) throws SQLException {
		sql = "update `userinfo` set `invalidlogin` = 3 where `username` = ? or `email` = ?;";
		stmt1 = con.prepareStatement(sql);
		stmt1.setString(1, userName);
		stmt1.setString(2, userName);
		stmt1.executeUpdate();
		
	}


	private boolean doesntExists(String userName) throws SQLException {
		sql = "select `email`  from `userinfo` where `email` = ? or `username` = ?";
		stmt1  = con.prepareStatement(sql);
		stmt1.setString(1, userName);
		stmt1.setString(2, userName);
		set = stmt1.executeQuery();
		if(set.next()) {
				return false;
		}else {
			return true;
		}	
	}
	
	private boolean verify(String userName, String password) throws SQLException {
		sql = "select `email`,`username`,`password` from `userinfo` where `email` = ? or `username` = ?;";
		stmt1  = con.prepareStatement(sql);
		stmt1.setString(1, userName);
		stmt1.setString(2, userName);
		set = stmt1.executeQuery();
		
			set.next();
			String em = set.getString(1);
			String un = set.getString(2);
			String pas = set.getString(3);
			if((em.equals(userName)||un.equals(userName))&&pas.equals(password)) {
				return true;
			}else {
				return false;
			}
	}
	
}
