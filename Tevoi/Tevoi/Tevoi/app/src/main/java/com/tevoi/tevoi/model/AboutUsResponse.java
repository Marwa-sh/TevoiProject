package com.tevoi.tevoi.model;

public class AboutUsResponse extends  IResponse
{
    private  String Text;

    public void setText(String text) {
        Text = text;
    }

    public String getText() {
        return Text;
    }
}
