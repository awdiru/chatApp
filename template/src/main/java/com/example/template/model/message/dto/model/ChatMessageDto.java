package com.example.template.model.message.dto.model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageDto {
    private LocalDateTime time;
    private String fromUser;
    private String toUser;
    private String message;
}
