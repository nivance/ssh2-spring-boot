package jsch.spring.boot.sample;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jsch.spring.boot.sample.mybatis.domain.Book;
import jsch.spring.boot.sample.mybatis.mapper.BookMapper;

@SpringBootApplication
public class JschSampleApplication implements CommandLineRunner {
	
	
	public static void main(String[] args) {
		SpringApplication.run(JschSampleApplication.class, args);
	}

	@Autowired
	private BookMapper bookMapper;
	
	
	@Override
	public void run(String... arg0) throws Exception {
		List<Book> books = this.bookMapper.select();
		books.forEach( b -> {
			System.out.println(b);
		});
	}
	
}
