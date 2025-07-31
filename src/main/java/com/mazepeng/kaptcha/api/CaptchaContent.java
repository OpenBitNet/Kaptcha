package com.mazepeng.kaptcha.api;

public class CaptchaContent {
    private final String answer;
    private final String drawText;

    public CaptchaContent(String answer, String drawText) {
        this.answer = answer;
        this.drawText = drawText;
    }

    public String getAnswer() { return answer; }
    public String getDrawText() { return drawText; }
}
