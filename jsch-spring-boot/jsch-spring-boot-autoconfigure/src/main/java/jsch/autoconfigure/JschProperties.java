package jsch.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "spring.jsch")
public class JschProperties {
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
	 * default is no
	 */
	private String strictHostKeyChecking = "no";
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
