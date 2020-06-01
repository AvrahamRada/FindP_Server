package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.UserEntity;

public interface UserDao extends PagingAndSortingRepository<UserEntity, String>{ 

	// SELECT * FROM USERS WHERE USERNAME LIKE ?
		public List<UserEntity> findAllByUsernameOrUserId(@Param("username") String username,
				@Param("userId") String userId, Pageable pageable);
}
