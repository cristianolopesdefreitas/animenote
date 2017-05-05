package br.com.test.animenote;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import br.com.animenote.ApplicationConfig;

@Configuration
@Import(value = { ApplicationConfig.class })
@ComponentScan(basePackages = { "br.com.test.animenote" })
public class ApplicationTestConfig {

}
