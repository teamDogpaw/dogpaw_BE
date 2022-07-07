//package com.project.dogfaw.common.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.project.dogfaw.post.model.Post;
//import com.project.dogfaw.user.model.User;
//import lombok.Getter;
//import net.minidev.json.annotate.JsonIgnore;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//public class StackList {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @ManyToOne
//    @JoinColumn(name = "USER_ID")
//    @JsonIgnore
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "POST_ID")
//    @JsonBackReference(value = "post-fk")
//    private Post post;
//
//}
