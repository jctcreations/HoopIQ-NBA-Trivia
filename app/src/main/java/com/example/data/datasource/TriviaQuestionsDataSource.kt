package com.example.data.datasource

import com.example.data.model.QuestionDifficulty
import com.example.data.model.TriviaCategory
import com.example.data.model.TriviaQuestion

object TriviaQuestionsDataSource {

    val allQuestions = listOf(
        // Daily Clutch Challenge Questions
        TriviaQuestion(
            id = "d1",
            prompt = "Who is the only player in NBA history to surpass 40,000 career regular-season points?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("Kareem Abdul-Jabbar", "LeBron James", "Michael Jordan", "Kobe Bryant"),
            correctIndex = 1,
            explanation = "LeBron James crossed the 40,000 career point threshold in March 2024 against the Denver Nuggets.",
            statContext = "Regular season all-time points leader (>40,000 pts).",
            difficulty = QuestionDifficulty.EASY
        ),
        TriviaQuestion(
            id = "d2",
            prompt = "Which NBA franchise has won the most NBA Championships in league history?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("Los Angeles Lakers (17)", "Boston Celtics (18)", "Chicago Bulls (6)", "Golden State Warriors (7)"),
            correctIndex = 1,
            explanation = "The Boston Celtics won their 18th championship banner in the 2024 NBA Finals, breaking a tie with the Lakers.",
            statContext = "Boston Celtics: 18 titles (1957–2024).",
            difficulty = QuestionDifficulty.EASY
        ),
        TriviaQuestion(
            id = "d3",
            prompt = "What was Nikola Jokić's draft position in the 2014 NBA Draft?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("15th overall", "30th overall", "41st overall", "Undrafted"),
            correctIndex = 2,
            explanation = "Nikola Jokić was picked 41st overall in the 2nd round, famously during a Taco Bell commercial broadcast.",
            statContext = "Lowest drafted MVP in modern NBA draft history.",
            difficulty = QuestionDifficulty.MEDIUM
        ),
        TriviaQuestion(
            id = "d4",
            prompt = "Who holds the NBA single-game scoring record with 100 points?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("Kobe Bryant", "Wilt Chamberlain", "David Thompson", "Devin Booker"),
            correctIndex = 1,
            explanation = "Wilt Chamberlain scored 100 points for the Philadelphia Warriors against the Knicks on March 2, 1962.",
            statContext = "Kobe Bryant holds 2nd place with 81 points (2006).",
            difficulty = QuestionDifficulty.EASY
        ),
        TriviaQuestion(
            id = "d5",
            prompt = "Which player holds the regular season record for most three-pointers made in a single season (402)?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("Klay Thompson", "James Harden", "Stephen Curry", "Damian Lillard"),
            correctIndex = 2,
            explanation = "Stephen Curry shattered the record during his unanimous 2015-16 MVP season, making 402 three-pointers.",
            statContext = "Golden State finished 73-9 that historic season.",
            difficulty = QuestionDifficulty.MEDIUM
        ),
        TriviaQuestion(
            id = "d6",
            prompt = "In the 2023-24 NBA season, who led the entire NBA in total blocked shots as a rookie?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("Chet Holmgren", "Rudy Gobert", "Victor Wembanyama", "Brook Lopez"),
            correctIndex = 2,
            explanation = "Victor Wembanyama led the NBA with 254 blocks (3.6 blocks per game) in his sensational rookie year.",
            statContext = "Youngest player to record a 5x5 stat line in NBA history.",
            difficulty = QuestionDifficulty.MEDIUM
        ),
        TriviaQuestion(
            id = "d7",
            prompt = "Which NBA head coach has won the most championships in NBA history (11)?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("Red Auerbach", "Phil Jackson", "Gregg Popovich", "Pat Riley"),
            correctIndex = 1,
            explanation = "Phil Jackson won 6 rings with the Chicago Bulls and 5 with the Los Angeles Lakers.",
            statContext = "Phil Jackson: 11 rings; Red Auerbach: 9 rings.",
            difficulty = QuestionDifficulty.HARD
        ),
        TriviaQuestion(
            id = "d8",
            prompt = "Who was the youngest MVP winner in NBA history at age 22?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("LeBron James", "Derrick Rose", "Magic Johnson", "Wes Unseld"),
            correctIndex = 1,
            explanation = "Derrick Rose won the 2010-11 NBA MVP with the Chicago Bulls at 22 years and 7 months old.",
            statContext = "Rose averaged 25.0 PPG and 7.7 APG leading the Bulls to 62 wins.",
            difficulty = QuestionDifficulty.HARD
        ),
        TriviaQuestion(
            id = "d9",
            prompt = "Which player scored 73 points in a single game in January 2024 against the Atlanta Hawks?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("Joel Embiid", "Luka Dončić", "Giannis Antetokounmpo", "Donovan Mitchell"),
            correctIndex = 1,
            explanation = "Luka Dončić put on a masterclass shooting 25-of-33 from the field for 73 points.",
            statContext = "Tied for 4th highest individual scoring game in NBA history.",
            difficulty = QuestionDifficulty.CLUTCH
        ),
        TriviaQuestion(
            id = "d10",
            prompt = "Which player is the only one in NBA history to lead the league in scoring AND assists in the exact same season (since merger)?",
            category = TriviaCategory.DAILY_CHALLENGE,
            options = listOf("Russell Westbrook", "James Harden", "Trae Young", "Nate 'Tiny' Archibald"),
            correctIndex = 3,
            explanation = "Nate 'Tiny' Archibald accomplished this feat with the Kansas City-Omaha Kings in 1972-73 (34.0 PPG, 11.4 APG).",
            statContext = "Historic milestone that hasn't been matched since.",
            difficulty = QuestionDifficulty.CLUTCH
        ),

        // Live Stats & Real-Time Box Score Questions
        TriviaQuestion(
            id = "ls1",
            prompt = "In tonight's live game between GSW and LAL, Stephen Curry has 34 points. How many 3-pointers has he made in the current box score?",
            category = TriviaCategory.LIVE_STATS,
            options = listOf("5 3PM", "6 3PM", "8 3PM", "11 3PM"),
            correctIndex = 2,
            explanation = "Curry is currently shooting 8-of-13 from downtown in the 4th quarter against Los Angeles.",
            statContext = "Live GSW @ LAL Box Score: Curry 34 PTS, 8 3PM.",
            difficulty = QuestionDifficulty.MEDIUM
        ),
        TriviaQuestion(
            id = "ls2",
            prompt = "Nikola Jokić is currently battling Dallas with a stat line of 22 PTS, 14 REB, and 12 AST. What milestone does this represent?",
            category = TriviaCategory.LIVE_STATS,
            options = listOf("Double-Double", "Triple-Double", "Quadruple-Double", "5x5 Game"),
            correctIndex = 1,
            explanation = "Reaching double digits in three categories (Points, Rebounds, Assists) marks another Jokić triple-double.",
            statContext = "Jokić ranks 3rd all-time in career regular season triple-doubles.",
            difficulty = QuestionDifficulty.EASY
        ),
        TriviaQuestion(
            id = "ls3",
            prompt = "Which live matchup on tonight's board is currently in the 4th quarter with under a minute left?",
            category = TriviaCategory.LIVE_STATS,
            options = listOf("SAS vs PHX", "LAL vs GSW", "BOS vs MIL", "DEN vs DAL"),
            correctIndex = 1,
            explanation = "Lakers vs Warriors is at Q4 0:42 with a score of 112 - 116 in a tight clutch finish.",
            statContext = "Live arena feed tracking active Q4 game.",
            difficulty = QuestionDifficulty.EASY
        ),
        TriviaQuestion(
            id = "ls4",
            prompt = "Who holds the highest field goal percentage among all active centers with over 25 PPG this season?",
            category = TriviaCategory.LIVE_STATS,
            options = listOf("Giannis Antetokounmpo (61.1%)", "Nikola Jokić (56.5%)", "Anthony Davis (54.8%)", "Bam Adebayo (51.0%)"),
            correctIndex = 0,
            explanation = "Giannis is shooting a blistering 61.1% from the field while averaging 31.4 points per game.",
            statContext = "Dominance in the paint and fast-break transition scoring.",
            difficulty = QuestionDifficulty.MEDIUM
        ),
        TriviaQuestion(
            id = "ls5",
            prompt = "Which dynamic guard is currently leading the NBA in both points per game (31.1) and steals per game (2.0) on the OKC Thunder?",
            category = TriviaCategory.LIVE_STATS,
            options = listOf("Jalen Williams", "Shai Gilgeous-Alexander", "Lu Dort", "Josh Giddey"),
            correctIndex = 1,
            explanation = "SGA is posting MVP-level two-way numbers leading Oklahoma City to the top of the Western Conference.",
            statContext = "2.0 steals per contest while scoring over 31 points.",
            difficulty = QuestionDifficulty.MEDIUM
        ),

        // Player Showdown: Who Averaged More?
        TriviaQuestion(
            id = "ps1",
            prompt = "Head-to-head career averages: Who has a higher career points-per-game (PPG) average?",
            category = TriviaCategory.PLAYER_SHOWDOWN,
            options = listOf("Stephen Curry (24.8 PPG)", "Kevin Durant (27.3 PPG)", "They are exactly tied", "Curry has 28.5 PPG"),
            correctIndex = 1,
            explanation = "Kevin Durant boasts a 27.3 career PPG mark, the 4th highest in NBA history, topping Curry's 24.8 career PPG.",
            statContext = "Durant: 4 scoring titles; Curry: 2 scoring titles.",
            difficulty = QuestionDifficulty.MEDIUM
        ),
        TriviaQuestion(
            id = "ps2",
            prompt = "Single-season assists: Who recorded the highest single-season assists per game average between these two legends?",
            category = TriviaCategory.PLAYER_SHOWDOWN,
            options = listOf("Magic Johnson (13.1 APG in 1983-84)", "John Stockton (14.5 APG in 1989-90)", "Chris Paul (11.6 APG in 2007-08)", "Steve Nash (11.6 APG in 2006-07)"),
            correctIndex = 1,
            explanation = "John Stockton's 14.5 APG in 1989-90 is the all-time single-season record in NBA history.",
            statContext = "Stockton is the all-time assists leader with 15,806.",
            difficulty = QuestionDifficulty.HARD
        ),
        TriviaQuestion(
            id = "ps3",
            prompt = "Rebound showdown: Who holds the higher career rebounds per game (RPG) in NBA history?",
            category = TriviaCategory.PLAYER_SHOWDOWN,
            options = listOf("Wilt Chamberlain (22.9 RPG)", "Bill Russell (22.5 RPG)", "Dennis Rodman (13.1 RPG)", "Moses Malone (12.2 RPG)"),
            correctIndex = 0,
            explanation = "Wilt Chamberlain holds the all-time record with 22.89 RPG, edging out Bill Russell's 22.45 RPG.",
            statContext = "Both grabbed over 21,000 career rebounds.",
            difficulty = QuestionDifficulty.HARD
        ),
        TriviaQuestion(
            id = "ps4",
            prompt = "Free throw sniper: Who has the higher career Free Throw Percentage (FT%)?",
            category = TriviaCategory.PLAYER_SHOWDOWN,
            options = listOf("Stephen Curry (91.0%)", "Steve Nash (90.4%)", "Damian Lillard (89.8%)", "Ray Allen (89.4%)"),
            correctIndex = 0,
            explanation = "Stephen Curry is the all-time NBA leader in career free-throw percentage at 91.0%, just ahead of Steve Nash.",
            statContext = "Curry once made 80 consecutive free throws.",
            difficulty = QuestionDifficulty.MEDIUM
        ),

        // Who Am I? Guess the Player
        TriviaQuestion(
            id = "g1",
            prompt = "Who am I? I was drafted #15 overall in 2013, won 2 MVPs, a Finals MVP, and scored 50 points in Game 6 to clinch my franchise's first title in 50 years.",
            category = TriviaCategory.GUESS_THE_PLAYER,
            options = listOf("Kawhi Leonard", "Giannis Antetokounmpo", "Dirk Nowitzki", "Jimmy Butler"),
            correctIndex = 1,
            explanation = "Giannis Antetokounmpo 'The Greek Freak' powered the Milwaukee Bucks to the 2021 championship.",
            statContext = "50 points, 14 rebounds, 5 blocks in Game 6.",
            difficulty = QuestionDifficulty.EASY
        ),
        TriviaQuestion(
            id = "g2",
            prompt = "Who am I? I wore #77, was EuroLeague MVP at age 19, recorded a 60-point 21-rebound 10-assist triple double, and led my team to the 2024 NBA Finals.",
            category = TriviaCategory.GUESS_THE_PLAYER,
            options = listOf("Luka Dončić", "Bogdan Bogdanović", "Goran Dragić", "Kristaps Porziņģis"),
            correctIndex = 0,
            explanation = "Luka Dončić pulled off the iconic 60-21-10 overtime miracle against the Knicks in 2022.",
            statContext = "First 60-20-10 game in NBA history.",
            difficulty = QuestionDifficulty.EASY
        ),
        TriviaQuestion(
            id = "g3",
            prompt = "Who am I? I am a 7-foot-4 French phenom with an 8-foot wingspan, won Rookie of the Year unanimously, and registered 5 blocks and 5 steals in a single game.",
            category = TriviaCategory.GUESS_THE_PLAYER,
            options = listOf("Rudy Gobert", "Alex Sarr", "Victor Wembanyama", "Nicolas Batum"),
            correctIndex = 2,
            explanation = "Victor Wembanyama took the NBA by storm in 2023-24 playing for Gregg Popovich and the San Antonio Spurs.",
            statContext = "Youngest player with a 5x5 game.",
            difficulty = QuestionDifficulty.EASY
        ),
        TriviaQuestion(
            id = "g4",
            prompt = "Who am I? Nicknamed 'The Truth', I won 2008 Finals MVP with Boston, drained 2,143 career threes, and scored 26,397 points.",
            category = TriviaCategory.GUESS_THE_PLAYER,
            options = listOf("Ray Allen", "Paul Pierce", "Kevin Garnett", "Reggie Miller"),
            correctIndex = 1,
            explanation = "Paul Pierce was given the moniker 'The Truth' by Shaquille O'Neal after a 42-point game in 2001.",
            statContext = "10x NBA All-Star and Hall of Famer.",
            difficulty = QuestionDifficulty.MEDIUM
        ),

        // Franchise & Finals Lore
        TriviaQuestion(
            id = "fl1",
            prompt = "Which team came back from a 3-1 deficit in the 2016 NBA Finals to capture their first championship in franchise history?",
            category = TriviaCategory.FRANCHISE_LORE,
            options = listOf("Golden State Warriors", "Cleveland Cavaliers", "Miami Heat", "Toronto Raptors"),
            correctIndex = 1,
            explanation = "The Cavaliers won Games 5, 6, and 7 behind LeBron James and Kyrie Irving to stun the 73-9 Warriors.",
            statContext = "Only 3-1 comeback in NBA Finals history.",
            difficulty = QuestionDifficulty.EASY
        ),
        TriviaQuestion(
            id = "fl2",
            prompt = "Which player won Finals MVP despite his team losing the NBA Finals series in 1969?",
            category = TriviaCategory.FRANCHISE_LORE,
            options = listOf("Elgin Baylor", "Jerry West", "Wilt Chamberlain", "Julius Erving"),
            correctIndex = 1,
            explanation = "Jerry West 'The Logo' is the only player in NBA history to win Finals MVP while on the losing team (Lakers vs Celtics).",
            statContext = "Averaged 37.9 PPG in that 7-game series.",
            difficulty = QuestionDifficulty.HARD
        ),
        TriviaQuestion(
            id = "fl3",
            prompt = "Which coach holds the record for most regular-season wins in NBA history (>1,380 wins)?",
            category = TriviaCategory.FRANCHISE_LORE,
            options = listOf("Don Nelson", "Gregg Popovich", "Lenny Wilkens", "Pat Riley"),
            correctIndex = 1,
            explanation = "Gregg Popovich surpassed Don Nelson in March 2022 to claim the all-time winningest coaching crown.",
            statContext = "5 NBA Championships with the San Antonio Spurs.",
            difficulty = QuestionDifficulty.MEDIUM
        ),
        TriviaQuestion(
            id = "fl4",
            prompt = "What year was the three-point line first introduced to the NBA?",
            category = TriviaCategory.FRANCHISE_LORE,
            options = listOf("1976-77 season", "1979-80 season", "1984-85 season", "1990-91 season"),
            correctIndex = 1,
            explanation = "The NBA adopted the 3-point line for the 1979-80 season, Chris Ford made the first one on October 12, 1979.",
            statContext = "Larry Bird and Magic Johnson also debuted as rookies that season.",
            difficulty = QuestionDifficulty.HARD
        )
    )

    /**
     * Periodic 3-Day Cycle Epoch:
     * Rotates and dynamically shuffles question banks every 3 days (259,200,000 ms).
     */
    fun getCurrentCycleNumber(): Long {
        val threeDaysInMillis = 3L * 24 * 60 * 60 * 1000
        return System.currentTimeMillis() / threeDaysInMillis
    }

    fun getDaysUntilNextRegeneration(): Int {
        val threeDaysInMillis = 3L * 24 * 60 * 60 * 1000
        val millisIntoCycle = System.currentTimeMillis() % threeDaysInMillis
        val remainingMillis = threeDaysInMillis - millisIntoCycle
        val remainingDays = (remainingMillis / (24 * 60 * 60 * 1000)).toInt() + 1
        return remainingDays.coerceIn(1, 3)
    }

    fun getQuestionsForCategory(category: TriviaCategory): List<TriviaQuestion> {
        val cycle = getCurrentCycleNumber()
        val random = java.util.Random(cycle + category.ordinal.toLong())
        val matches = allQuestions.filter { it.category == category }
        val pool = if (matches.isNotEmpty()) matches else allQuestions
        // Pseudo-random deterministic rotation for the active 3-day cycle
        return pool.shuffled(random)
    }

    fun getQuestionsForDifficultyLevel(level: Int, count: Int = 5): List<TriviaQuestion> {
        val cycle = getCurrentCycleNumber()
        val random = java.util.Random(cycle + level * 31L)

        // As level increases, bias questions toward HARD and CLUTCH
        val targetDifficulties = when (level) {
            1 -> listOf(QuestionDifficulty.EASY, QuestionDifficulty.MEDIUM)
            2 -> listOf(QuestionDifficulty.EASY, QuestionDifficulty.MEDIUM, QuestionDifficulty.HARD)
            3 -> listOf(QuestionDifficulty.MEDIUM, QuestionDifficulty.HARD)
            4 -> listOf(QuestionDifficulty.HARD, QuestionDifficulty.CLUTCH)
            else -> listOf(QuestionDifficulty.HARD, QuestionDifficulty.CLUTCH)
        }

        val matchingPool = allQuestions.filter { it.difficulty in targetDifficulties }
        val pool = if (matchingPool.size >= count) matchingPool else allQuestions
        return pool.shuffled(random).take(count)
    }
}
