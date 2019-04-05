package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.IssueDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueDocumentDAO extends CrudRepository<IssueDocument,Integer> {
}
