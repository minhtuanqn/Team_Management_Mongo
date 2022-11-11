//package com.nli.probation.repository;
//
//import com.nli.probation.entity.UserAccountEntity;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.repository.CrudRepository;
//
//import java.util.List;
//
//public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Integer>, JpaSpecificationExecutor<UserAccountEntity> {
//    boolean existsByEmail(String email);
//
//    boolean existsByPhone(String phone);
//
//    boolean existsByEmailAndIdNot(String email, int id);
//
//    boolean existsByPhoneAndIdNot(String phone, int id);
//
//    List<UserAccountEntity> findAllByIdIn(List<Integer> userIds);
//}
