package jsch.autoconfigure;

import java.io.DataOutputStream;
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

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(JschProperties.class)
public class JschAutoConfiguration implements InitializingBean, DisposableBean {

	private final JschProperties jschProperties;
	private Session session;

	public JschAutoConfiguration(JschProperties jschProperties) {
		this.jschProperties = jschProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		JSch jsch = new JSch();
		session = jsch.getSession(jschProperties.getProxyUser(), jschProperties.getProxyHost(),
				jschProperties.getProxyPort());
		session.setPassword(jschProperties.getProxyPassword());
		session.setConfig("StrictHostKeyChecking", jschProperties.getStrictHostKeyChecking());
		System.out.printf("Jsch_AutoConfiguration connect to:::host: %s , port: %d, user: %s\n",
				jschProperties.getProxyHost(), jschProperties.getProxyPort(), jschProperties.getProxyUser());
		session.setDaemonThread(true);
		session.connect();
		System.out.printf("Jsch_AutoConfiguration::: %s \n", session.getServerVersion());// 打印SSH服务器版本信息
		int assinged_port = session.setPortForwardingL(jschProperties.getLocalPort(), jschProperties.getDestHost(),
				jschProperties.getDestPort());
		System.out.printf("Jsch_AutoConfiguration:::localhost:%d -> %s:%d \n", assinged_port,
				jschProperties.getDestHost(), jschProperties.getDestPort());
		startDetectPort();
	}

	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	private String LOCALHOST = "127.0.0.1";

	/**
	 * 本地转发请求的端口如果长时间不用，会在一定时间内退出监听， 
	 * 从而使转发请求失败， 所以启动一个线程定期(每隔30s)给这个端口发请求。
	 */
	private void startDetectPort() {
		// 创建一个流套接字并将其连接到本地转发端口上
		try (Socket socket = new Socket(LOCALHOST, jschProperties.getLocalPort());
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
		service.shutdownNow();
		if (session != null) {
			session.disconnect();
		}
		System.out.println("Jsch_AutoConfiguration::: destory connection");
	}
}
