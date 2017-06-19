package jsch.spring.boot.sample.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import jsch.spring.boot.sample.mybatis.domain.Book;

/**
 * @author Eddú Meléndez
 */
@Mapper
public interface BookMapper {

	@Select("select * from t_book order by id limit 10")
	List<Book> select();

}