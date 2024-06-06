package uz.pdp.springsecurity.shoxjaxon.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoRequest {
    private String api_secret_key;
    private String add = "login_session";
    private String create_account;
    private String email_address;
    private String full_name;
    private String username;
    private String password;
    private String avatarURL;
    private String site_role;

    // getters and setters
}
