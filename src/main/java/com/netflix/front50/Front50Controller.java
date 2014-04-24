package com.netflix.front50;

import com.netflix.front50.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.netflix.front50")
public class Front50Controller {

    public static void main(String[] args) {
        SpringApplication.run(Front50Controller.class, args);
    }

    @Autowired
    Application application;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Application> index() {
        try {
            return application.findAll();
        } catch (NotFoundException e) {
            throw new NoApplicationsFoundException(e);
        } catch (Throwable thr) {
            throw new ApplicationException(thr);
        }
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public Application getByName(@PathVariable String name) {
        try {
            return application.findByName(name);
        } catch (NotFoundException e) {
            throw new ApplicationNotFoundException(e);
        } catch (Throwable thr) {
            thr.printStackTrace();
            throw new ApplicationException(thr);
        }
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Exception, baby")
    class ApplicationException extends RuntimeException {
        public ApplicationException(Throwable cause) {
            super(cause);
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No applications found")
    class NoApplicationsFoundException extends RuntimeException {
        public NoApplicationsFoundException(Throwable cause) {
            super(cause);
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Application not found")
    class ApplicationNotFoundException extends RuntimeException {
        public ApplicationNotFoundException(Throwable cause) {
            super(cause);
        }
    }
}
