package com.bervan.interviewapp.onevalue.interviewplan;

import com.bervan.interviewapp.onevalue.OneValueService;
import com.bervan.interviewapp.onevalue.OneValueView;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(InterviewPlanView.ROUTE_NAME)
public class InterviewPlanView extends OneValueView {
    public static final String ROUTE_NAME = "interview-plan";


    public InterviewPlanView(@Autowired OneValueService service) {
        super(ROUTE_NAME, "interview-plan", "Interview Plan", service);
    }
}