package qlsach;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection{
	
	private PreparedStatement pst = null; // phương thức thực thi các câu truy vấn
	private Connection con = null;; //tạo biến connection ban đầu 
	private Statement stmt = null; //dùng để hỗ trợ thực thi câu lệnh truy vấn 
	private ResultSet rs = null;
	
	public DBConnection() {
		//đk sql vs drivermanager:>>>quản li hết tất cả
		try {
			String userName = "sa";
			String password = "12345";
			String url = "jdbc:sqlserver://LAPTOP-JPJBE60E:1433;databaseName=QLSach";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//tạo kết nối , grtconection là lấy ra để kết nối
			con = DriverManager.getConnection(url,userName,password);
			System.out.println("1. Connected ok");
		} catch (Exception e) {
			System.out.println("2. Connected error");
		}
	}
	
	public ResultSet Login(String username) {
		 // cung cấp đối tượng đê truy vấn cho resultset
		try {
			
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT Mk FROM Login where Ten='"+username+"'");
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}

}
