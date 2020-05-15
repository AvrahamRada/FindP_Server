package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.ElementEntity;
import acs.util.Location;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, String> {

	// SELECT * FROM ELEMENTS WHERE NAME LIKE ?
	public List<ElementEntity> findAllByNameLike(@Param("name") String name, Pageable pageable);

	// SELECT * FROM ELEMENTS WHERE Type LIKE ?
	public List<ElementEntity> findAllByTypeLike(@Param("type") String type, Pageable pageable);
	
	// SELECT * FROM ELEMENTS WHERE Type LIKE ?
	public List<ElementEntity> findAllByLocation(@Param("location") Location location, Pageable pageable);

}
