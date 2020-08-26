package com.leo.word;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.leo.word.trie.TrieTree;
import com.leo.word.util.CollectionUtils;
import com.leo.word.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author leo
 * @version 1.0.0
 * @description 抽象的词语联想工具
 * @create 2020/8/26 16:12
 */
public class WordAssociateTool {

    private static final Logger log = LoggerFactory.getLogger(WordAssociateTool.class);
    /**
     * 词典树
     */
    private TrieTree trieTree;

    /**
     * 全拼map
     */
    private Map<String, Set<String>> qpMap;

    /**
     * 全拼列表
     */
    private List<String> qpList;

    /**
     * 首字母map
     */
    private Map<String, Set<String>> szmMap;

    /**
     * 词库
     */
    private List<String> thesaurus;

    /**
     * 设置词库
     *
     * @param thesaurus 词库
     */
    public void setThesaurus(List<String> thesaurus) {
        this.thesaurus = thesaurus;
    }

    /**
     * 排序器
     */
    private Comparator<String> comparator;

    /**
     * 设置排序器
     *
     * @param comparator 排序器
     */
    public void setComparator(Comparator<String> comparator) {
        this.comparator = comparator;
    }

    public WordAssociateTool(List<String> thesaurus, Comparator<String> comparator) {
        this.thesaurus = thesaurus;
        this.comparator = comparator;
        buildIndex();
    }

    /**
     * 重新构建索引
     */
    public void rebuildIndex() {
        buildIndex();
    }

    private void buildIndex() {
        long start = System.currentTimeMillis();
        if (CollectionUtils.isEmpty(thesaurus)) {
            log.error("buildIndex fail,thesaurus is empty");
            return;
        }
        trieTree = new TrieTree();
        qpMap = new HashMap<>();
        qpList = new ArrayList<>();
        szmMap = new HashMap<>();
        try {
            for (String word : thesaurus) {
                // 构建Trie
                trieTree.insert(word);
                // 构建全拼索引
                String qp;
                qp = PinyinHelper.convertToPinyinString(word, "", PinyinFormat.WITHOUT_TONE);
                qpMap.computeIfAbsent(qp, k -> new HashSet<>()).add(word);
                // 构建首字母索引
                String szm = PinyinHelper.getShortPinyin(word);
                szmMap.computeIfAbsent(szm, k -> new HashSet<>()).add(word);
            }
            // 构建全拼列表，根据长度正序排序
            qpList.addAll(qpMap.keySet());
            qpList.sort(Comparator.comparingInt(String::length));
            log.info("build word index end,cost:{},words:{}", System.currentTimeMillis() - start, thesaurus.size());
        } catch (PinyinException e) {
            log.error("build index PinyinException:", e);
        }
    }


    /**
     * 根据关键词做联想
     *
     * @param keyword 关键词
     * @param size    需要的数量
     * @return 联想结果集
     */
    public List<String> associate(String keyword, int size) {
        // 匹配词语
        List<String> words = findInIndex(keyword, size);
        // 排序
        if (comparator != null) {
            words.sort(comparator);
        }
        // 截取并输出结果
        return ListUtils.subList(words, 0, size);
    }

    /**
     * 匹配关联的词语
     *
     * @param keyword 关键词
     * @param size    需要的数量
     * @return 匹配结果集
     */
    private List<String> findInIndex(String keyword, int size) {
        // 根据关键词前缀找关联
        HashSet<String> wordsForPrefix = trieTree.getWordsForPrefix(keyword);
        wordsForPrefix.remove(keyword);
        if (wordsForPrefix.size() < 10) {
            try {
                // 转换成拼音
                keyword = PinyinHelper.convertToPinyinString(keyword, "", PinyinFormat.WITHOUT_TONE);
            } catch (PinyinException e) {
                log.error("转化拼音异常,keyword:{},e:", keyword, e);
            }
        }
        if (wordsForPrefix.size() < size) {
            // 如果关联结果为空，根据全拼找关联
            Set<String> qpResult = qpMap.get(keyword.toLowerCase());
            if (CollectionUtils.isEmpty(qpResult)) {
                qpResult = new HashSet<>();
                // 先从左向右关联
                for (String qp : qpList) {
                    if (qp.startsWith(keyword)) {
                        qpResult.addAll(qpMap.get(qp));
                        if (qpResult.size() >= 10) {
                            break;
                        }
                    }
                }
                // 再全关联
                if (qpResult.size() < size) {
                    for (String qp : qpList) {
                        if (qp.contains(keyword)) {
                            qpResult.addAll(qpMap.get(qp));
                            if (qpResult.size() >= 10) {
                                break;
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(qpResult)) {
                for (String s : qpResult) {
                    wordsForPrefix.addAll(trieTree.getWordsForPrefix(s));
                }
            }
        }
        if (wordsForPrefix.size() < size) {
            // 如果关联结果为空，再根据首字母找关联
            Set<String> szmResult = szmMap.get(keyword.toLowerCase());
            if (CollectionUtils.isNotEmpty(szmResult)) {
                for (String s : szmResult) {
                    wordsForPrefix.addAll(trieTree.getWordsForPrefix(s));
                }
            }
        }
        return new ArrayList<>(wordsForPrefix);
    }
}
