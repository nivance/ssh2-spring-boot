package ganymed.autoconfigure;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.LocalPortForwarder;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(GanymedProperties.class)
public class GanymedAutoConfiguration implements InitializingBean, DisposableBean {

	private final GanymedProperties ganymedProperties;
	private Connection conn;
	private LocalPortForwarder lpf1;

	public GanymedAutoConfiguration(GanymedProperties ganymedProperties) {
		this.ganymedProperties = ganymedProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		start();
	}

	private void start() throws Exception {
		startDetectPort();
		conn = new Connection(ganymedProperties.getProxyHost(), ganymedProperties.getProxyPort());
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(ganymedProperties.getProxyUser(),
				ganymedProperties.getProxyPassword());
		System.out.printf("Ganymed_AutoConfiguration connect to ===>>> host: %s, port: %d, user: %s, Authenticated: %b \n",
				ganymedProperties.getProxyHost(), ganymedProperties.getProxyPort(), ganymedProperties.getProxyUser(),
				isAuthenticated);
		if (!isAuthenticated) {
			throw new IOException("Authentication failed.");
		}
		lpf1 = conn.createLocalPortForwarder(ganymedProperties.getLocalPort(), ganymedProperties.getDestHost(),
				ganymedProperties.getDestPort());
		System.out.printf("Ganymed_AutoConfiguration:::localhost:%d ===>>> %s:%d \n", ganymedProperties.getLocalPort(),
				ganymedProperties.getDestHost(), ganymedProperties.getDestPort());
	}

	private ScheduledExecutorService service = null;
	private String LOCALHOST = "127.0.0.1";

	/**
	 * 每隔60s转发端口发请求，如果失去连接，重新初始化
	 */
	private void startDetectPort() {
		service = Executors.newSingleThreadScheduledExecutor();
		Runnable runnable = new Runnable() {
			public void run() {
				try (Socket socket = new Socket(LOCALHOST, ganymedProperties.getLocalPort());
						OutputStream out = socket.getOutputStream();) {
					// 创建一个流套接字并将其连接到本地转发端口上
					// 向服务器端发送数据
					out.write("ping".getBytes());
					out.flush();
					System.out.printf("%1$tY-%1$tm-%1$td %tT ping---->%s:%d %s\n", new Date(), LOCALHOST, ganymedProperties.getLocalPort(), "is OK!");
				} catch (ConnectException e) {
					try { // 重新初始化
						shutdown();
						start();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} catch (Exception e) {
					System.out.println("ping -------exception:" + e.getMessage());
				}
			}
		};
		service.scheduleAtFixedRate(runnable, 60, 60, TimeUnit.SECONDS);
	}

	private void shutdown() throws Exception {
		if (lpf1 != null) {
			lpf1.close();
		}
		if (conn != null) {
			conn.close();
		} 
		System.out.println("Genymed_AutoConfiguration::: destory connection");
	}

	@Override
	public void destroy() throws Exception {
		shutdown();
	}
}
