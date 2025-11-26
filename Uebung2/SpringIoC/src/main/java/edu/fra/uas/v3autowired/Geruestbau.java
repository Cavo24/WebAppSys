package edu.fra.uas.v3autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("myWeakness")
public class Geruestbau implements  Work{

    private static final Logger LOGGER = LoggerFactory.getLogger(Geruestbau.class);

    
    public void doWork(){
        LOGGER.info("Ich machen Geruestbau brate!");
    }

    
}
