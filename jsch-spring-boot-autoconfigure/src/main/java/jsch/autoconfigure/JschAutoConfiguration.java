package jsch.autoconfigure;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(JschProperties.class)
public class JschAutoConfiguration {

	private final JschProperties jschProperties;

	public JschAutoConfiguration(JschProperties jschProperties) {
		this.jschProperties = jschProperties;
	}

	@PostConstruct
	public void init() {
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(jschProperties.getProxyUser(), jschProperties.getProxyHost(),
					jschProperties.getProxyPort());
			session.setPassword(jschProperties.getProxyPassword());
			session.setConfig("StrictHostKeyChecking", jschProperties.getStrictHostKeyChecking());
			session.connect();
			System.out.println("Jsch AutoConfiguration:::" + session.getServerVersion());// 打印SSH服务器版本信息
			int assinged_port = session.setPortForwardingL(jschProperties.getLocalPort(), jschProperties.getDestHost(),
					jschProperties.getDestPort());
			System.out.println("Jsch AutoConfiguration:::localhost:" + assinged_port + " -> " + jschProperties.getDestHost() + ":"
					+ jschProperties.getDestHost());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
