# Bet-Bot
Bet-Bot is API that automatically collect information about future matches and make prediction
(Over/Under 2.5 goals) using pre-trained Neural Networks. Full workflow is created for Bundesliga
matches.

# Requirements
* Maven for the build
* Java 1.8

# Build
`mvn clean install`

# Not implemented things
* Acceptance tests for the API
* Espnfc support (required for new matches)
* Neural Networks
    * pick appropriate ML algorithm (NN probably with softmax)
    * test predictions precision (80%/20% train/test)
    * integrate the pre-trained NN to be used for predictions
     (Here only the weights of the final NN are required)