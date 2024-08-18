package com.bervan.interviewapp.view;

import com.bervan.interviewapp.codingtask.CodingTaskView;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionsView;
import com.bervan.interviewapp.onevalue.interviewplan.InterviewPlanView;
import com.bervan.interviewapp.questionconfig.QuestionConfigView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.Map;

public abstract class PageLayout extends VerticalLayout {
    private final Map<String, Button> buttons = new HashMap<>();
    private final String CURRENT_ROUTE_NAME;

    public PageLayout(String currentRouteName) {
        CURRENT_ROUTE_NAME = currentRouteName;

        HorizontalLayout menuRow = new HorizontalLayout();
        addButton(menuRow, MainView.ROUTE_NAME, "Home");
        addButton(menuRow, StartInterviewView.ROUTE_NAME, "Interview!");
        addButton(menuRow, InterviewQuestionsView.ROUTE_NAME, "Interview Questions");
        addButton(menuRow, CodingTaskView.ROUTE_NAME, "Coding Tasks");
        addButton(menuRow, InterviewPlanView.ROUTE_NAME, "Interview Plan");
        addButton(menuRow, QuestionConfigView.ROUTE_NAME, "Question Config");
        addButton(menuRow, ImportExportView.ROUTE_NAME, "Import/Export");

        add(menuRow);

    }

    public void notification(String message) {
        Notification.show(message);
    }

    public void addButton(HorizontalLayout menuRow, String routeName, String buttonText) {
        Button button = new Button(buttonText);
        button.addClickListener(buttonClickEvent ->
                button.getUI().ifPresent(ui -> ui.navigate(routeName)));

        buttons.put(routeName, button);

        if (routeName.equals(CURRENT_ROUTE_NAME)) {
            button.getStyle().set("background-color", "blue");
            button.getStyle().set("color", "white");
        }

        menuRow.add(button);
    }

}
