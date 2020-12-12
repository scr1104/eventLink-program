package controller.DisplayScheduleStrategy;

import Presenter.DisplaySchedulePresenter;

import java.time.format.DateTimeFormatter;

public class DisplayMyScheduleController implements Displayer{

    DisplaySchedulePresenter displaySchedulePresenter;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DisplayMyScheduleController(DisplaySchedulePresenter dsp){
        displaySchedulePresenter = dsp;
    }


    /**
     * Display the current user's upcoming event schedule
     */
    public boolean display(){
        System.out.println(displaySchedulePresenter.myScheduleDisplay());
        return true;
    }



}
