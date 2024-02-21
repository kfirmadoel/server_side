package kfirmadoel.server_side.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import kfirmadoel.server_side.documents.ParentConInfo;

public interface ParentConRepository extends MongoRepository<ParentConInfo, String> {
    ParentConInfo findByMacAddr(String macAddr);
}
