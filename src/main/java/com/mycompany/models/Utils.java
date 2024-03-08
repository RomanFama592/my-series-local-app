package com.mycompany.models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.IOException;

/**
 *
 * @author famar
 */
public class Utils {

    public static void openDefaultNavigator(String url) throws IOException {
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else if (osName.contains("Linux")) {
            Runtime.getRuntime().exec("xdg-open " + url);
        } else if (osName.contains("Mac OS X")) {
            Runtime.getRuntime().exec("open " + url);
        } else { //INFORMAR SISTEMA NO SOPORTADO }
            System.err.println("The operative system no supported");
        }
    }
}
