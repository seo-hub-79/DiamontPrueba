package com.example.androiduber;

import com.example.androiduber.Model.DriverInfoModel;

public class Common {
    public static final String DRIVER_INFO_REFERENCE = "DriverInfo";
    public static final String DRIVERS_LOCATION_REFERENCES = "DriversLocation" ;

    public static DriverInfoModel currentUser;

    public static String buildWelcomeMessage() {
        if (Common.currentUser != null){
            return new StringBuilder("Bienvenido: ")
                    .append(Common.currentUser.getNombre())
                    .append(" ")
                    .append(Common.currentUser.getApellido()).toString();
        }
        else {
            return "";
        }
    }
}
