package com.example.imagehub.application.port.out;

import com.example.imagehub.domain.User;

public interface LoadUserPort {

    User loadUser(String userId);

}
