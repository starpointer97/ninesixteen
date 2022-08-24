This repo contains my work for NineSixteen Project

Below are some driving factors behind the code 

1. The code was written with a strict timeline of 3 hours or less to be spent on it. 
2. As a result, there was no time for writing test cases or adding more abstractions

If I had the time, I would have done the following

1. Implemented an interface for RateLimiters to be extended from. This way, the underlying rateLimiter is abtracted away and we can plug and play any rateLimiter with no change to the core code. 

2. Added log4j dependecy instead of console outputs

3. There is duplicate code between AcceptService and ElasticMQListenerImpl. That can be abstracted away to a common utility.
