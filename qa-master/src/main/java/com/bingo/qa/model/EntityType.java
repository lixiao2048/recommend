package com.bingo.qa.model;

/**
 */
public class EntityType {
    public static int ENTITY_QUESTION = 1;
    public static int ENTITY_COMMENT = 2;
    public static int ENTITY_USER = 3;

    public static int getEntityQuestion() {
        return ENTITY_QUESTION;
    }

    public static void setEntityQuestion(int entityQuestion) {
        ENTITY_QUESTION = entityQuestion;
    }

    public static int getEntityComment() {
        return ENTITY_COMMENT;
    }

    public static void setEntityComment(int entityComment) {
        ENTITY_COMMENT = entityComment;
    }

    public static int getEntityUser() {
        return ENTITY_USER;
    }

    public static void setEntityUser(int entityUser) {
        ENTITY_USER = entityUser;
    }
}
