package com.twitter.vladimirbot.service.impl;

import com.google.common.collect.Lists;
import com.twitter.vladimirbot.service.TwitterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.PostConstruct;

@Component
public class TwitterServiceImpl implements TwitterService {

    // Don't ever check your credentials into GitHub!
    private static final String TWITTER_CONSUMER_KEY = "";
    private static final String TWITTER_CONSUMER_SECRET = "";
    private static final String TWITTER_ACCESS_TOKEN = "";
    private static final String TWITTER_ACCESS_SECRET = "";

    // Filter query indicating which hashtags to retweet
    private static final FilterQuery FILTER_QUERY = new FilterQuery( "#VladimirBot",
                                                                     "#ComodijoVladimir",
                                                                     "#vladimirbot",
                                                                     "#comodijovladimir",
                                                                     "#vladimirdijo"
    );
    @Autowired
    Environment environment;

    @Override

    @PostConstruct // Runs the bot when the server starts up
    public void runBot() {

        // Read API keys from environment variables (don't want to check these into Github! LOL!)
        ConfigurationBuilder configurationBuilder = buildConfig( TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,
                                                                 TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_SECRET );

        TwitterFactory tf = new TwitterFactory( configurationBuilder.build() );
        final Twitter twitter = tf.getInstance();

        configurationBuilder = buildConfig( TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,
                                            TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_SECRET );

        TwitterStream twitterStream = new TwitterStreamFactory( configurationBuilder.build() ).getInstance();
        StatusListener listener = new StatusListener() {
            public void onStatus( Status status ) {

                // Check if account is OK to retweet. Try and avoid obvious fake and spam accounts.
                if ( isAccountOk( status ) ) {
                           System.out.println( String.format( "Tweeteando: [%s]", status.getText() ) );
                    try {
                        sendTweet(cleanTweet(status.getText()));
                    }
                    catch ( Exception e ) {
                        System.out.println( e.getMessage() );
                    }
                }
                else {
                    System.out.println( String.format( "Skipping: [%s]", status.getText() ) );
                }
            }

            public void onDeletionNotice( StatusDeletionNotice statusDeletionNotice ) {

            }

            public void onTrackLimitationNotice( int numberOfLimitedStatuses ) {

            }

            public void onScrubGeo( long l, long l1 ) {

            }

            public void onStallWarning( StallWarning stallWarning ) {

            }

            public void onException( Exception ex ) {

                ex.printStackTrace();
            }
        };

        twitterStream.addListener( listener );
        twitterStream.filter(FILTER_QUERY);
    }


    private static ConfigurationBuilder buildConfig( String consumerKey,
                                                     String consumerSecret,
                                                     String accessToken,
                                                     String accessSecret ) {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled( true )
                .setOAuthConsumerKey( consumerKey )
                .setOAuthConsumerSecret( consumerSecret )
                .setOAuthAccessToken( accessToken )
                .setOAuthAccessTokenSecret( accessSecret );
        return cb;
    }

    /**
     * Counts the number of characters in the give String.
     */
    @Override
    public Integer numberCount( String string ) {

        int count = 0;
        for ( Character character : Lists.charactersOf( string ) ) {
            if ( Character.isDigit( character ) ) {
                count++;

            }
        }
        return count;
    }

    public static String cleanTweet(String tweet) {
         String finalTweet = "";
       String[] stringArray = new String[]{"#VladimirBot", "#ComoDijoVladimir","#vladimirbot","#comodijovladimir","#vladimirdijo"};

        int index = 0;
        while(index <stringArray.length){
            if(tweet.contains(stringArray[index].toString())){
                finalTweet = tweet.replaceAll(stringArray[index].toString(), "");
            }
            index++;
        }

        finalTweet.replaceAll("^\\p{Zs}+|\\p{Zs}+$", "");
        return finalTweet;
    }

    private boolean isAccountOk(Status status ) {


        // Skip accounts with too many numbers in the name. Fake accounts often have lots of numbers in the name.
        boolean tooManyNumberInAccountName =
                numberCount( status.getUser().getScreenName() ) >= 1;

        return  !status.isRetweetedByMe() && // Avoid infinite loop
                !tooManyNumberInAccountName;
    }

    /**
     * This method is currently not being used.
     * @param tweetBody
     */
    private static void sendTweet( String tweetBody ) {

        // Read API keys from environment variables (don't want to check these into Github! LOL!)
        ConfigurationBuilder configurationBuilder = buildConfig( TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,
                                               TWITTER_ACCESS_TOKEN, TWITTER_ACCESS_SECRET );

        try {
            TwitterFactory factory = new TwitterFactory( configurationBuilder.build() );
            Twitter twitter = factory.getInstance();

            System.out.println( twitter.getScreenName() );
            Status status = twitter.updateStatus("Como dijo Vladimir," + tweetBody + " y a dormir.");

        }
        catch ( TwitterException te ) {
            te.printStackTrace();
            System.exit( -1 );
        }
    }
}
