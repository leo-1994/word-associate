package com.leo.word.util;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leo
 * @version 1.0.0
 * @description
 * @create 2020/8/27 10:16
 */
@Slf4j
public class PinYinUtils {

    private static final Map<String, String> pinyinDicMap = new HashMap<>();

    private static final String DELIMITER = "=";

    static {
        InputStream in = PinYinUtils.class.getClassLoader().getResourceAsStream("user_dic.dict");
        try {
            List<String> readLines = IOUtils.readLines(in);
            for (String line : readLines) {
                String[] split = line.split(DELIMITER);
                pinyinDicMap.put(split[0], split[1]);
            }
        } catch (IOException e) {
            log.error("加载用户词典失败,error:", e);
        }
    }

    public static String convertToPinyinString(String word) {
        while (StringUtils.isNotBlank(word)) {
            try {
                return PinyinHelper.convertToPinyinString(word, "", PinyinFormat.WITHOUT_TONE);
            } catch (PinyinException e) {
                String badCase = getBadCase(e.getMessage());
                String s = rewriteBadCase(badCase);
                if (StringUtils.isBlank(s)) {
                    return null;
                }
                word = word.replace(badCase, s);
            }
        }
        return null;
    }

    public static String getShortPinyin(String word) {
        while (StringUtils.isNotBlank(word)) {
            try {
                return PinyinHelper.getShortPinyin(word);
            } catch (PinyinException e) {
                String badCase = getBadCase(e.getMessage());
                String s = rewriteBadCase(badCase);
                if (StringUtils.isBlank(s)) {
                    return null;
                }
                s = s.substring(0, 1);
                word = word.replace(badCase, s);
            }
        }
        return null;
    }

    private static String getBadCase(String errorMsg) {
        return errorMsg.replace("Can't convert to pinyin: ", "");
    }

    private static String rewriteBadCase(String badCase) {
        String pinyin = pinyinDicMap.get(badCase);
        if (StringUtils.isBlank(pinyin)) {
            log.error("word:{} can't convert to pinyin", badCase);
        }
        return pinyin;
    }

    public static void addPinyinDic(String word, String pinyin) {
        pinyinDicMap.put(word, pinyin);
    }

}
