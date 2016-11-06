package com.digiup.sandbox.listener;

import com.digiup.sandbox.model.Person;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PersonWriterListener implements ItemWriteListener<Person> {

    @Autowired
    private KieContainer container;

    @Override
    public void beforeWrite(List<? extends Person> list) {
        KieSession session = container.newKieSession();
        list.stream().map(l -> session.insert(l));
        session.fireAllRules();
        session.dispose();
    }

    @Override
    public void afterWrite(List<? extends Person> list) {

    }

    @Override
    public void onWriteError(Exception e, List<? extends Person> list) {

    }
}
