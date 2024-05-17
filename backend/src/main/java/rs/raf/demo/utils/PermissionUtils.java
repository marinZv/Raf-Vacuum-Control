package rs.raf.demo.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import rs.raf.demo.domain.entities.user.RoleType;
import rs.raf.demo.domain.exceptions.PermissionException;

import java.util.Collection;

public class PermissionUtils {

    public static final String permissionMessage = "Don't have permission";

    public static boolean hasPermission(RoleType roleType){
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.contains(roleType);
    }

    public static void checkRole(RoleType roleType){
        if(!hasPermission(roleType)){
            throw new PermissionException(permissionMessage);
        }
    }

}
