package ru.vasiljev.UfanetTestTask;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String REGISTER = "REG";
    public static final String CANCEL = "CCL";
    public static final String TAKEN_TO_WORK = "WRK";
    public static final String READY = "RDY";
    public static final String ISSUED = "ISS";
    public static final List<String> statusList = new ArrayList<>();

    public Constants() {
        statusList.add(REGISTER);
        statusList.add(CANCEL);
        statusList.add(TAKEN_TO_WORK);
        statusList.add(READY);
        statusList.add(ISSUED);
    }
}
