package kfirmadoel.server_side.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kfirmadoel.server_side.documents.ParentConInfo;

@Repository
public interface ParentConRepository extends MongoRepository<ParentConInfo, String> {
    ParentConInfo findByEmail(String email);
}
