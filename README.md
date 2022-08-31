This repo contains my work for NineSixteen Project

*****How to Run the code******

Normally, just like any other java application, run the main method in Launcher class. 

****How to change the config*****

The properties for throttle rate and duration are set in application.properties in src/main/resources. These can be changed to any value between 0 < value < INT.MAX_SIZE


Below are some driving factors behind the code 

1. The code was written with a strict timeline of 3 hours or less to be spent on it. 
2. As a result, there was no time for writing test cases or adding more abstractions

If I had the time, I would have done the following

1. Implemented an interface for RateLimiters to be extended from. This way, the underlying rateLimiter is abtracted away and we can plug and play any rateLimiter with no change to the core code. -- 15 minutes 

2. Added log4j dependecy instead of console outputs -- 10 minutes

3. There is duplicate code between AcceptService and ElasticMQListenerImpl. That can be abstracted away to a common utility. -- 15 minutes

4. Places where static methods/constants were used, I would have preferred having them as objects, and that would have been part of next level of refactoring. -- 30 minutes

5. Write unit test cases -- Another 90 minutes.
