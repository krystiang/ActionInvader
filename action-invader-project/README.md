# Action-Invader-Agent

Implements the game logic. Includes the state of all gestureblocks in the "pipeline", the lifepoints, the accompished points and a game timer.
It compares the input gesture with the first gesture in the pipeline. If it is the expected gesture 
it will be deleted from the gestureblock and points are added depending on the position the gestureblock had on the canvas at the time. If it is not the expected gesture the lifepoints will be reduced by 1.
If a gestureblocks reaches the bottom end of the canvas the game is drawn on it will be deleted and the lifepoints will be reduced by 1. If the first gestureblock
is proccessed before reaching the bottom end it will be deleted aswell without a reduction in the lifepoints.

The game is started when the bothHandsUp gesture is received and also resets with the same gesture. The game is over when lifepoints reach 0.

> Written by [Krystian Graczyk, Nick Diedrich]

### Changes
