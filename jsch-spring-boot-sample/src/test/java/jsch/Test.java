package jsch.spring.boot.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Test {

	static int lport = 3306;//本地端口
	static String rhost = "1232132.mysql.aliyuncs.com";//远程MySQL服务器
	static int rport = 3306;//远程MySQL服务端口

	public static void go() {
		String user = "ldld";//SSH连接用户名
		String password = "dfdfd";//SSH连接密码
		String host = "23.45.67.89";//SSH服务器
		int port = 1024//SSH访问端口
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
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/eady", "root", "");
			st = conn.createStatement();
			String sql = "SELECT COUNT(1) FROM dual";
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
