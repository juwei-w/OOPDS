****# Part 2

## A. Member Contributions

No | ID         | Name          | Task descriptions (Part 1) | Task descriptions (Part 2) | Contribution %
-- | ---------- | ------------- | -------------------------- | -------------------------- | --------------
1  | 1211104210 | Wong Ju Wei   |      3 4 5 6               |      7 8                   | 25%
2  | 1211103705 | Ter Zheng Bin |      1 2 8                 |      4 5                   | 25%
3  | 1211104730 | Lim Ye Xin    |      5 6 7                 |      1 2                   | 25%
4  | 1211105182 | Yap Rui Ern   |      3 4 7                 |      3 6                   | 25%
 

## B. Part 1 Feature Completion (Latest)

Mark Y for Complete, P for Partial done, N for Not implemented.

No | Feature                                                                         | Completed (Y/P/N)
-- | ------------------------------------------------------------------------------- | -----------------
1  | All cards should be faced up to facilitate checking.                            | Y (ZhengBin, YeXin)
2  | Start a new game with randomized 52 cards.                                      | Y (ZhengBin)
3  | The first card in the deck is the first lead card and is placed at the center.  | Y (JuWei, RuiErn)
4  | The first lead card determines the first player.                                | Y (JuWei, RuiErn)
5  | Deal 7 cards to each of the 4 players.                                          | Y (JuWei, YeXin)
6  | All players must follow the suit or rank of the lead card.                      | Y (JuWei, YeXin)
7  | The highest-rank card with the same suit as the lead card wins the trick.       | Y (YeXin, RuiErn)
8  | The winner of a trick leads the next card.                                      | Y (ZhengBin)


## C. Part 2 Feature Completion

Mark Y for Complete, P for Partial done, N for Not implemented.

No | Feature                                                                                                                     | Completed (Y/P/N)
-- | --------------------------------------------------------------------------------------------------------------------------- | -----------------
1  | If a player cannot follow suit or rank, the player must draw from the deck until a card can be played.                                | Y (YeXin)
2  | When the remaining deck is exhausted and the player cannot play, the player does not play in the trick.                               | Y (YeXin)
3  | Finish a round of game correctly. Display the score of each player.                                                                   | Y (RuiErn)
4  | Can exit and save the game (use file or database).                                                                                    | Y (ZhengBin)
5  | Can resume the game. The state of the game is restored when resuming a game (use file or database).                                   | Y (ZhengBin)
6  | Reset the game. All scores become zero. Round and trick number restart from 1.                                                        | Y (RuiErn)
7  | Support GUI playing mode (cards should be faced up or down as in the real game). The GUI can be in JavaFX, Swing, Spring, or Android. | Y (JuWei)
8  | Keep the console output to facilitate checking. The data in console output and the GUI must tally.                                    | Y (JuWei)


## D. Link to Part 2 GitHub Repo
https://github.com/juwei-w/OOPDS

## E. Link to Presentation Video
https://drive.google.com/file/d/1v4AhgTzrYMlVxAR5GWuPSXyefoEVENjX/view?usp=sharing