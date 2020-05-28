package com.gsh.common.enums;

/**
 * 试题类型
 * 对应系统字典值：subject_category
 * @author gsh
 */
public enum SubjectCategory
{
    SINGLE_Q("1","单选题"),
    MULTI_Q("2","多选题"),
    BLANKS_Q("3","填空题"),
    JUDGEMENT_Q("4","判断题"),
    ANSWER_Q("5","简答题");

    private final String dictId;
    private final String name;

    SubjectCategory(String dictId, String name) {
        this.dictId = dictId;
        this.name = name;
    }

    /**
     * 根据字典值获取 题目类型
     * @param dictId
     * @return
     */
    public static SubjectCategory getSubjectType(String dictId) {
        SubjectCategory[] types = SubjectCategory.values();
        for (int i = 0; i < types.length; i++) {
            if (types[i].getDictId().equals(dictId)) {
                return types[i];
            }
        }
        return SubjectCategory.SINGLE_Q;//default
    }

    public String getDictId() {
        return dictId;
    }

    public String getName() {
        return name;
    }
}
