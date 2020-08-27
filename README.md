# 词语联想工具

##### 主要用于搜索框输入词时的词语联想场景

支持中文、拼音、首字母联想

支持基于拼音的错别字改写

支持自定义词典以及排序规则

##### 使用方法

```
/**
 * 根据关键词做联想
 *
 * @param keyword 关键词
 * @param size    需要的数量
 * @return 联想结果集
 */
List<String> associate(String keyword, int size);

 /**
 * 设置词库
 *
 * @param thesaurus 词库
 */
 void setThesaurus(List<String> thesaurus);
 
 /**
 * 设置排序器
 *
 * @param comparator 排序器
 */
void setComparator(Comparator<String> comparator);

/**
* 重新构建索引
*/
void rebuildIndex();
```

###### demo

```
List<String> thesaurus = Arrays.asList("和田玉", "核桃");
WordAssociateTool wordAssociateTool = new WordAssociateTool(thesaurus, null);
List<String> result = wordAssociateTool.associate("h", 10);
result.forEach(System.out::println);
```

###### 自定义拼音词典
在resources目录下添加user_dic.dict文件

每行一个词，格式: 朩=pin

也可以通过 PinYinUtils.addPinyinDic("朩","pin") 的方式动态添加
