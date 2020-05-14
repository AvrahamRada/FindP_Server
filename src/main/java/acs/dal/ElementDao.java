package acs.dal;

import org.springframework.data.repository.PagingAndSortingRepository;

import acs.data.ElementEntity;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, String>{ 
	

}
