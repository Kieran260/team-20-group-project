package com.iamin.views.helpers;

import com.vaadin.flow.component.html.Div;


public class Styling {
    public static void styleSquareBox(Div div) {
        div.getStyle().set("background-color", "white");
        div.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        div.setClassName("card");
    }
    
    public static void styleHalfSquareBox(Div div) {
        div.getStyle().set("background-color", "white");
        div.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        div.getStyle().set("height", "120px");
        div.setClassName("card");

    }
}
