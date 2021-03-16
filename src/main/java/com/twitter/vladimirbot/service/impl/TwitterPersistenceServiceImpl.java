package com.twitter.vladimirbot.service.impl;

import com.twitter.vladimirbot.service.TwitterPersistenceService;

import java.util.List;

/**
 * Responsible for all bot persistence operations. All methods should be transactional.
 */
public class TwitterPersistenceServiceImpl implements TwitterPersistenceService {

    /**
     * Persist the usage of the indicated hashtags with the date/time.
     * @param hashtags a string list of hashtags.
     */
    @Override
    public void persistHashtagUsages( List<String> hashtags ) {

        // TODO Implement
    }
}
