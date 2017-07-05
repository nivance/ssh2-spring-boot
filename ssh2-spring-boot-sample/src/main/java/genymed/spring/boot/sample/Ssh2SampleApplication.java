package genymed.spring.boot.sample;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import genymed.spring.boot.sample.mybatis.domain.Book;
import genymed.spring.boot.sample.mybatis.mapper.BookMapper;

@SpringBootApplication
public class Ssh2SampleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Ssh2SampleApplication.class, args);
	}

	@Autowired
	private BookMapper bookMapper;

	private int times = 200;
	private int sleepMills = 30000;

	@Override
	public void run(String... arg0) throws Exception {
		int count = 10;
		CountDownLatch downLatch = new CountDownLatch(count);
		for (int i=0;i<count;i++) {
			new Thread() {
				public void run() {
					int i = 0;
					while (i < times) {
						List<Book> books = bookMapper.select();
						System.out.println(new Date() +  "  " + Thread.currentThread().getName() + "   times:::" + i);
						books.forEach(b -> {
							System.out.println(b);
						});
						try {
							Thread.sleep(sleepMills);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						i++;
					}
					downLatch.countDown();
				}
			}.start();
		}
		downLatch.await();
		System.out.println("done");
	}

}
