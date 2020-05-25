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

public class SignUp extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String sql;
	private static Connection con;
	private static PrintWriter writer;
	private static PreparedStatement stmt1;
	private static ResultSet set;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ResourceBundle bundle = ResourceBundle.getBundle("com.abc.utilities.mysql");
		String url = bundle.getString("url");
		String user = bundle.getString("user");
		String pass = bundle.getString("password");
		
		String firstName = req.getParameter("fn");
		String middleName = req.getParameter("mn");
		String lastName = req.getParameter("ln");
		String userName = req.getParameter("un");
		String email = req.getParameter("em");
		String password = req.getParameter("pas");
		int age = Integer.parseInt(req.getParameter("age"));
		String phoneNumber = req.getParameter("pn");
		
		try {
			con  = DriverManager.getConnection(url,user,pass);
			
			
			writer = resp.getWriter();
			
			
			if(doesntExists(email)) {
				
				if(userNameDoesExist(userName)) {
					writer.println(userName+" already Taken.. Use other UserName");
				}else {
				
					addDetails(firstName,middleName,lastName,userName,email,password,age,phoneNumber);
				
					writer.println("Signed in successfully....");
					req.setAttribute("con", con);
					RequestDispatcher rd = req.getRequestDispatcher("home");
					rd.include(req, resp);
				}
			}else  {
				writer.println(email+" already exists... please try logging in....");
				
				resp.setContentType("text/html");
				RequestDispatcher rd = req.getRequestDispatcher("Login.html");
				rd.include(req, resp);
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

	private boolean userNameDoesExist(String userName) throws SQLException {
		sql = "select `username`  from `userinfo` where `username` = ? ";
		stmt1  = con.prepareStatement(sql);
		stmt1.setString(1, userName);
		set = stmt1.executeQuery();
		if(set.next()) {
			return true;
		}else {
			return false;
		}
	}

	private boolean doesntExists(String email) throws SQLException {
		sql = "select `email`  from `userinfo` where `email` = ? ";
		stmt1  = con.prepareStatement(sql);
		stmt1.setString(1, email);
		set = stmt1.executeQuery();
		if(set.next()) {
				return false;
		}else {
			return true;
		}	
	}
	
	private int addDetails(String firstName, String middleName , String lastName, String userName, String email, String password, int age, String phoneNumber) throws SQLException {
		sql = " insert into `userinfo`  (`firstname`,`middlename`,`lastname`,`username`,`email`,`password`,`age`,`phonenumber`) values (?,?,?,?,?,?,?,?);";
		stmt1  = con.prepareStatement(sql);
		stmt1.setString(1, firstName);
		stmt1.setString(2, middleName);
		stmt1.setString(3, lastName);
		stmt1.setString(4, userName);
		stmt1.setString(5, email);
		stmt1.setString(6, password);
		stmt1.setInt(7, age);
		stmt1.setString(8, phoneNumber);
		int i = stmt1.executeUpdate();
		return i;
	}

}
