package ganymed.autoconfigure;

import java.io.DataOutputStream;
import java.io.IOException;
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
		conn = new Connection(ganymedProperties.getProxyHost(), ganymedProperties.getProxyPort());
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(ganymedProperties.getProxyUser(),
				ganymedProperties.getProxyPassword());
		System.out.printf("Ganymed_AutoConfiguration connect to:::host: %s , port: %d, user: %s, Authenticated: %b \n",
				ganymedProperties.getProxyHost(), ganymedProperties.getProxyPort(), ganymedProperties.getProxyUser(),
				isAuthenticated);
		if (!isAuthenticated) {
			throw new IOException("Authentication failed.");
		}
		lpf1 = conn.createLocalPortForwarder(ganymedProperties.getLocalPort(), ganymedProperties.getDestHost(),
				ganymedProperties.getDestPort());
		System.out.printf("Ganymed_AutoConfiguration:::localhost:%d -> %s:%d \n", ganymedProperties.getLocalPort(),
				ganymedProperties.getDestHost(), ganymedProperties.getDestPort());
	}

	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	private String LOCALHOST = "127.0.0.1";

	/**
	 * 本地转发请求的端口如果长时间不用，会在一定时间内退出监听， 从而使转发请求失败， 所以启动一个线程定期(每隔30s)给这个端口发请求。
	 */
	private void startDetectPort() {
		// 创建一个流套接字并将其连接到本地转发端口上
		try (Socket socket = new Socket(LOCALHOST, ganymedProperties.getLocalPort());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());) {
			// 向服务器端发送数据
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						System.out.printf("%1$tY-%1$tm-%1$td %tT ping---->%s:%d\n", new Date(), LOCALHOST, 3307);
						out.writeUTF("ping");
					} catch (Exception e) {
					}
				}
			};
			service.scheduleAtFixedRate(runnable, 60, 30, TimeUnit.SECONDS);
		} catch (Exception e) {
			System.out.printf("%1$tY-%1$tm-%1$td %tT ping---->%s:%d 请求检测异常, 退出ping. errmsg: %s\n", new Date(),
					LOCALHOST, 3307, e.getMessage());
		}
	}

	@Override
	public void destroy() throws Exception {
		if (conn != null) {
			conn.cancelRemotePortForwarding(3307);
		} else {
			return;
		}
		if (lpf1 != null) {
			lpf1.close();
		}
		conn.close();
		System.out.println("Genymed_AutoConfiguration::: destory connection");
	}
}
