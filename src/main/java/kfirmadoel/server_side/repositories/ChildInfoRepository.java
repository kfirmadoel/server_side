package kfirmadoel.server_side.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import kfirmadoel.server_side.documents.ChildInfo;

public interface ChildInfoRepository extends MongoRepository<ChildInfo, String> {
    ChildInfo findByMacAddr(String macAddr);
}