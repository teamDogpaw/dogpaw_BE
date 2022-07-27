package com.project.dogfaw.sse.dto;


import com.project.dogfaw.sse.model.NotificationType;
import com.project.dogfaw.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private User receiver;
    private NotificationType notificationType;
    private String notificationContent;
    private String url;
}
