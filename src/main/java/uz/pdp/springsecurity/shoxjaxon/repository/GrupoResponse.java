package uz.pdp.springsecurity.shoxjaxon.repository;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class  GrupoResponse {
    // Getters and Setters
    private boolean success;
    private String autoLoginUrl;
    private String errorMessage;
    private int unread_group_messages;
    private boolean play_sound_notification;

}
