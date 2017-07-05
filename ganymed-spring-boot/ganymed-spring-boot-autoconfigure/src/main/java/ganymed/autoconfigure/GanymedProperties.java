package ganymed.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "spring.ssh2")
public class GanymedProperties {
	/**
	 * proxy host
	 */
	private String proxyHost;
	/**
	 * the port to access proxy host 
	 */
	private int proxyPort;
	/**
	 * the user login to the proxy host
	 */
	private String proxyUser;
	/**
	 * the user's password
	 */
	private String proxyPassword;
	/**
	 * romte destination host
	 */
	private String destHost;
	/**
	 * romte destination port 
	 */
	private int destPort;
	/**
	 * local port 
	 */
	private int localPort;
	
}
