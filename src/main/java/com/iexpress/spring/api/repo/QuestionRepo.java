package com.iexpress.spring.api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.iexpress.spring.domain.Question;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {

	@Modifying
	@Query("delete from Question q where q.id in ?1 ")
	void deleteById(List<Integer> questionIds);

}
