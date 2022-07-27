package com.project.dogfaw.sse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    APPLY, ACCEPT,
    REJECT, REPLY;

}
