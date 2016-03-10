/*
 * Copyright [2011-2016] "Neo Technology"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */
package org.neo4j.cineasts.service;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.cineasts.domain.Movie;
import org.neo4j.cineasts.domain.User;
import org.neo4j.cineasts.movieimport.MovieDbImportService;
import org.neo4j.cineasts.repository.MovieRepository;
import org.neo4j.cineasts.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mh
 * @since 04.03.11
 */
@Service
public class DatabasePopulator {

    private final static Logger log = LoggerFactory.getLogger(DatabasePopulator.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    MovieDbImportService importService;

    @Transactional
    public List<Movie> populateDatabase() {
        importService.importImageConfig();
        User me = userRepository.save(new User("micha", "Micha", "password", User.SecurityRole.ROLE_ADMIN, User.SecurityRole.ROLE_USER));
        User ollie = new User("ollie", "Olliver", "password", User.SecurityRole.ROLE_USER);
        me.addFriend(ollie);
        userRepository.save(me);
        List<Integer> ids = asList(19995 , 194, 600, 601, 602, 603, 604, 605, 606, 607, 608, 609, 13, 20526, 11, 1893, 1892, 1894, 168, 193, 200, 157, 152, 201, 154, 12155, 58, 285, 118, 22, 392, 5255, 568, 9800, 497, 101, 120, 121, 122);
        List<Movie> result = new ArrayList<Movie>(ids.size());
        for (Integer id : ids) {
            result.add(importService.importMovie(String.valueOf(id)));
        }

      /*  me.rate(movieRepository.findById("13"), 5, "Inspiring");
        final Movie movie = movieRepository.findById("603");
        me.rate(movie, 5, "Best of the series");*/
        return result;
    }

    @Transactional
    public void cleanDb() {
        new Neo4jDatabaseCleaner().cleanDb();
    }
}
