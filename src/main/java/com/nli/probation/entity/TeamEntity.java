//package com.nli.probation.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@NoArgsConstructor @AllArgsConstructor @Getter @Setter
//@Entity
//@Table(name = "team")
//public class TeamEntity {
//    @Column(name = "id")
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(name = "short_name")
//    private String shortName;
//
//    @Column(name = "name")
//    private String name;
//
//    @Column(name = "status")
//    private int status;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teamEntity")
//    private Set<UserAccountEntity> userList;
//}
