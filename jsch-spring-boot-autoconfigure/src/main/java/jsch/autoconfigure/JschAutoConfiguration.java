package jsch.autoconfigure;

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
		System.out.println("Jsch_AutoConfiguration connect to:::host:" + jschProperties.getProxyHost() + ", port : "
				+ jschProperties.getProxyPort() + ", user: " + jschProperties.getProxyUser());
		session.connect();
		System.out.println("Jsch_AutoConfiguration:::" + session.getServerVersion());// 打印SSH服务器版本信息
		int assinged_port = session.setPortForwardingL(jschProperties.getLocalPort(), jschProperties.getDestHost(),
				jschProperties.getDestPort());
		System.out.println("Jsch_AutoConfiguration:::localhost:" + assinged_port + " -> " + jschProperties.getDestHost()
				+ ":" + jschProperties.getDestHost());
	}

	@Override
	public void destroy() throws Exception {
		if (session != null) {
			session.disconnect();
		}
		System.out.println("Jsch_AutoConfiguration::: destory connection");
	}
}
