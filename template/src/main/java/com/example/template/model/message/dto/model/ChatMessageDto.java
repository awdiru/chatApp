package com.example.template.model.message.dto.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageDto implements Serializable {
    private String fromUser;
    private String toUser;
    private String message;
    private LocalDateTime time;
}
