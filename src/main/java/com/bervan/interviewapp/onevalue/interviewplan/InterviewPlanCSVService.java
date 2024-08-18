package com.bervan.interviewapp.onevalue.interviewplan;

import com.bervan.interviewapp.onevalue.OneValueFileService;
import org.springframework.stereotype.Service;

@Service
public class InterviewPlanCSVService extends OneValueFileService {
    public InterviewPlanCSVService() {
        super("InterviewPlan_", "InterviewPlan_db");
    }
}
