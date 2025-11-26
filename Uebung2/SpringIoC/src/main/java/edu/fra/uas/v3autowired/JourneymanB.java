package edu.fra.uas.v3autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("JourneymanB")
public class JourneymanB {
    
    @Autowired
    @Qualifier("myWeakness")
    Work work;

    public void performWork() {
        work.doWork();
   }
}
