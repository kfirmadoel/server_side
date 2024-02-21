package kfirmadoel.server_side.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import kfirmadoel.server_side.documents.users;

public interface UserRepository extends MongoRepository<users, String> {
    users findByEmail(String email);
}
