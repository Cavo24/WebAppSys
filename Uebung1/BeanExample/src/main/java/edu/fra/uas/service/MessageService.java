package edu.fra.uas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private Integer counter = 0;
    private String message;

    public String getMessage() {
        log.debug("getMessage() aufgerufen: Gebe aktuelle Nachricht zurück: {}", message);
        return message;
    }

    public void setMessage(String message) {
            log.debug("setMessage() aufgerufen: Neue Nachricht '{}' wird gesetzt");
        this.message=message;
    }

    public void increment(){
        counter = counter+1;
        log.debug("increment aufgerufen: counter um 1 erhöht: ", counter);

    }

    public Integer getCounter(){
        return counter;
    }
}
