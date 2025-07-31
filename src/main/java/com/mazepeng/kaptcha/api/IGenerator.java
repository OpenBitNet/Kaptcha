package com.mazepeng.kaptcha.api;

/**
 * 验证码内容生成器接口
 * 定义了如何生成验证码的答案以及如何在图片上绘制的文本
 */
public interface IGenerator {

    CaptchaContent generate();

}
