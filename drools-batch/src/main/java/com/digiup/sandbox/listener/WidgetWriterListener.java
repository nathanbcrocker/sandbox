package com.digiup.sandbox.listener;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WidgetWriterListener implements ItemWriteListener {

    @Autowired
    private KieContainer container;

    @Override
    public void beforeWrite(List list) {
        KieSession session = container.newKieSession();
        list.stream().map(l -> session.insert(l));
        session.fireAllRules();
        session.dispose();
    }

    @Override
    public void afterWrite(List list) {

    }

    @Override
    public void onWriteError(Exception e, List list) {

    }
}
