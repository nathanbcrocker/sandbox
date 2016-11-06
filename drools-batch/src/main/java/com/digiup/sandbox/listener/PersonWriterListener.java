package com.digiup.sandbox.listener;

import com.digiup.sandbox.model.Person;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class PersonWriterListener implements ItemWriteListener<Person> {

    @Autowired
    private KieContainer kieContainer;

    @Override
    public void beforeWrite(List<? extends Person> list) {
        KieSession kieSession = kieContainer.newKieSession();
        for (Person p : list) {
            p.setAge(null);
            kieSession.insert(p);
        }

        kieSession.fireAllRules();
        kieSession.dispose();
        kieSession.destroy();
    }

    @Override
    public void afterWrite(List<? extends Person> list) {

    }

    @Override
    public void onWriteError(Exception e, List<? extends Person> list) {

    }
}
