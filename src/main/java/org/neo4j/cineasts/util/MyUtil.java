package org.neo4j.cineasts.util;

import org.neo4j.cineasts.domain.User;

public class MyUtil {

	public boolean areFriends(User user, User loggedIn) {
        return user!=null && user.isFriend(loggedIn);
    }
}
