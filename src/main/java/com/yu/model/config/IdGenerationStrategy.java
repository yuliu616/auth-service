package com.yu.model.config;

/**
 * how to generate ID field for new record of a model class (table)
 */
public enum IdGenerationStrategy {

    /**
     * use a separate table with auto-number defined as its primary key (id).
     * create new record in it will return a new id which will be used as id
     * for the target class.
     */
    ID_TABLE,

}
