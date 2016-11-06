package com.digiup.sandbox.processor;

import com.digiup.sandbox.model.Person;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

public class PersonProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person p) throws Exception {
        int age = -1;
        if (p != null && p.getDateOfBirth() != null) {
            age = new Date().getYear() - p.getDateOfBirth().getYear();
            p.setAge(age);
        }

        return p;
    }
}
