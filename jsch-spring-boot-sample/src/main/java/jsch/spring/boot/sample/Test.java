package jsch.spring.boot.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Test {

	static int lport = 3306;//本地端口
	static String rhost = "rm-2ze97u8a6gl6xjr6d.mysql.rds.aliyuncs.com";//远程MySQL服务器
	static int rport = 3306;//远程MySQL服务端口

	public static void go() {
		String user = "dev";//SSH连接用户名
		String password = "everoboved";//SSH连接密码
		String host = "101.201.55.232";//SSH服务器
		int port = 22;//SSH访问端口
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			System.out.println(session.getServerVersion());//这里打印SSH服务器版本信息
			int assinged_port = session.setPortForwardingL(lport, rhost, rport);
			System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sql() {
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/everobo_ready", "liuzongling", "everobo2016!");
			st = conn.createStatement();
			String sql = "SELECT COUNT(1) FROM t_book";
			rs = st.executeQuery(sql);
			while (rs.next())
				System.out.println(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//rs.close();st.close();conn.close();
		}
	}

	public static void main(String[] args) {
		go();
		sql();
	}

}
