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
package org.neo4j.cineasts.controller;

import org.neo4j.cineasts.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles and retrieves the login or denied page depending on the URI template
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    UserRepository userRepository;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "login_error", required = false) boolean error, Model model) {
        logger.debug("Received request to show login page, error "+error);
        if (error) {
            model.addAttribute("error", "You have entered an invalid username or password!");
        }
        return "/auth/loginpage";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(
            @RequestParam(value = "j_username") String login,
            @RequestParam(value = "j_displayname") String name,
            @RequestParam(value = "j_password") String password,
            Model model) {

        try {
            userRepository.register(login,name,password);
            return "forward:/user/"+login;
        } catch(Exception e) {
            model.addAttribute("j_username",login);
            model.addAttribute("j_displayname",name);
            model.addAttribute("error",e.getMessage());
            return "/auth/registerpage";
        }
    }

    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String denied() {
        logger.debug("Received request to show denied page");
        return "/auth/deniedpage";
    }

    @RequestMapping(value = "/registerpage", method = RequestMethod.GET)
    public String registerPage() {
        logger.debug("Received request to show register page");
        return "/auth/registerpage";
    }
}