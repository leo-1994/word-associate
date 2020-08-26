package com.leo.word;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author leo
 * @version 1.0.0
 * @description
 * @create 2020/8/26 19:03
 */
public class WordAssociateToolTest {

    @Test
    public void associate() {
        List<String> thesaurus = Arrays.asList("和田玉", "核桃");
        WordAssociateTool wordAssociateTool = new WordAssociateTool(thesaurus, null);
        List<String> result = wordAssociateTool.associate("h", 10);
        result.forEach(System.out::println);
    }
}