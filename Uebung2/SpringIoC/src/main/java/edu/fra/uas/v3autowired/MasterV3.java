package edu.fra.uas.v3autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MasterV3 {
    @Autowired
    @Qualifier("JourneymanA")
    private JourneymanA journeymanA;

    @Autowired
    @Qualifier("JourneymanB")
    private JourneymanB journeymanB;

    public void delegateWorkA() {
        journeymanA.performWork();
    }

    public void delegateWorkB() {
        journeymanB.performWork();
    }
}
