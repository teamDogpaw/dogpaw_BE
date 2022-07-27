package com.project.dogfaw.sse.repository;

import com.project.dogfaw.sse.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("select n from Notification n where n.receiver.id = :userId order by n.id desc")
    List<Notification> findAllByUserId(@Param("userId") Long userId);

    @Query("select count(n) from Notification n where n.receiver.id = :userId and n.isRead = false")
    Long countUnReadNotifications(@Param("userId") Long userId);

    Optional<Notification> findById(Long NotificationsId);
    Optional<Notification> findByReceiverId(Long receiverId);

//    void deleteAllByReceiverId(Long receiverId);
    void deleteAllByReceiverId(Long receiverId);

    void deleteById(Long notificationId);


}
