package kfirmadoel.server_side.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kfirmadoel.server_side.documents.ChildInfo;

@Repository
public interface ChildInfoRepository extends MongoRepository<ChildInfo, String> {
    ChildInfo findByMacAddr(String macAddr);
}