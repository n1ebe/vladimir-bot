package com.twitter.vladimirbot.service;

import java.util.List;

public interface TwitterPersistenceService {

    void persistHashtagUsages( List<String> hashtags );
}
