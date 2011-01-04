/*
 * Copyright 2011 Harald Wellmann
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.googlecode.refit.spring.eg.music;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MusicSpringConfig {
    
    @Bean
    public Browser browser() {
        return new Browser();
    }

    @Bean
    public Dialog dialog() {
        return new Dialog();
    }

    @Bean
    public Display display() {
        return new Display();
    }

    @Bean
    public MusicLibrary musicLibrary() {
        return new MusicLibrary();
    }

    @Bean
    public MusicPlayer musicPlayer() {
        return new MusicPlayer();
    }
    
    @Bean
    public Realtime realtime() {
        return new Realtime();
    }
    
    @Bean
    public Simulator simulator() {
        return new Simulator();
    }

}
