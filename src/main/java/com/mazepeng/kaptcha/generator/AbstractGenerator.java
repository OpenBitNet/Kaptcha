package com.mazepeng.kaptcha.generator;

import com.mazepeng.kaptcha.api.CaptchaContent;
import com.mazepeng.kaptcha.api.IGenerator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 抽象生成器基类，提供一些通用工具
 */
public abstract class AbstractGenerator implements IGenerator {

    protected ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

}
