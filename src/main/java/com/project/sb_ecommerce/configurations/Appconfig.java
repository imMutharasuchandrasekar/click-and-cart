package com.project.sb_ecommerce.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Appconfig {

    @Bean
    public ModelMapper getModelMapper()
    {
        return new ModelMapper();
    }
}

