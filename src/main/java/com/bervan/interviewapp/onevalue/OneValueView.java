package com.bervan.interviewapp.onevalue;

import com.bervan.interviewapp.view.PageLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.Optional;

public abstract class OneValueView extends PageLayout {
    protected final OneValueService service;
    protected OneValue item;

    public OneValueView(String routeName, String key, String headerValue, OneValueService service) {
        super(routeName);
        this.service = service;

        H3 header = new H3(headerValue);

        TextArea textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setHeight("500px");
        Optional<OneValue> oneValue = service.loadByKey(key);
        item = oneValue.orElseGet(OneValue::new);
        item.setName(key);
        textArea.setValue(item.getContent() == null ? "" : item.getContent());

        Button saveButton = new Button("Save");
        saveButton.addClickListener(event -> save(textArea.getValue()));

        add(header, textArea, saveButton);
    }

    protected void save(String value) {
        item.setContent(value);
        service.save(item);
    }
}