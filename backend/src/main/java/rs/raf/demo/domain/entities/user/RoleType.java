package rs.raf.demo.domain.entities.user;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {

    CAN_CREATE("CAN_CREATE"),
    CAN_READ("CAN_READ"),
    CAN_UPDATE("CAN_UPDATE"),
    CAN_DELETE("CAN_DELETE"),



    CAN_SEARCH_VACUUM("CAN_SEARCH_VACUUM"),
    CAN_START_VACUUM("CAN_START_VACUUM"),
    CAN_STOP_VACUUM("CAN_STOP_VACUUM"),
    CAN_DISCHARGE_VACUUM("CAN_DISCHARGE_VACUUM"),
    CAN_ADD_VACUUM("CAN_ADD_VACUUM"),
    CAN_REMOVE_VACUUM("CAN_REMOVE_VACUUM");


    private final String role;

    RoleType(String role){
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
