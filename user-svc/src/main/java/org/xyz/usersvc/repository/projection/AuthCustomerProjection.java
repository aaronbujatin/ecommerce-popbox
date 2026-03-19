package org.xyz.usersvc.repository.projection;

import java.util.List;

public interface AuthCustomerProjection {
    String getEmail();
    String getPassword();
    List<String> getRoles();

}
