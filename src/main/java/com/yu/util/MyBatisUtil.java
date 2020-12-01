package com.yu.util;

import com.yu.exception.RecordInsertionFailException;
import com.yu.model.IHasId;
import com.yu.model.IntegerId;

import java.util.function.Function;

public class MyBatisUtil {

    /**
     * helper method to call for id generation (mapper method) and fill to the model.
     * @param model to be filled with new ID
     * @param idGenerationFactory factory method that will return a new Id of the target model class.
     * @param <T>
     * @return same as the parameter model
     */
    public static <T extends IHasId> T generateIdForModel(
            T model,
            Function<IntegerId, Long> idGenerationFactory,
            Function<Long, String> idFormatter)
    {
        IntegerId integerId = new IntegerId();
        long affected = idGenerationFactory.apply(integerId);
        if (affected <= 0) {
            throw new RecordInsertionFailException();
        }
        String idInString = String.valueOf(integerId.getId());
        model.setId(idInString);
        return model;
    }

    public static <T extends IHasId> T generateIdForModel(
            T model,
            Function<IntegerId, Long> idGenerationFactory)
    {
        return MyBatisUtil.generateIdForModel(model,
                idGenerationFactory,
                String::valueOf);
    }

}
