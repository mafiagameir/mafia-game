/*
 *     Copyright (c) 2018 Isa Hekmatizadeh.
 *     This file is part of mafiagame.
 *
 *     Mafiagame is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Mafiagame is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Mafiagame.  If not, see <http://www.gnu.org/licenses/>.
 */

import co.mafiagame.engine.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Esa Hekmatizadeh
 */
public class SimpleGameTest {
	@Test
	public void test() throws InterruptedException {
		final GameResult[] gameResult = new GameResult[1];
		Configuration configuration = new Configuration.Builder()
				.registerDetectiveTurnListener((game) -> System.out.println(game.getGameId() + ": detective turn"))
				.registerDoctorTurnListener((game) -> System.out.println(game.getGameId() + ": doctor turn"))
				.registerMafiaTurnListener((game) -> System.out.println(game.getGameId() + ": mafia turn"))
				.registerSunriseListener((game, nightResult) ->
						System.out.println(game.getGameId() + ": sunrise with result: " + nightResult))
				.registerGameFinishListener((game, result) -> {
					gameResult[0] = result;
					System.out.println(game.getGameId() + ": game finished with result: " + result);
				}).build();

		Game game = new Game("1", 3, 2, true, true, configuration);
		Assert.assertFalse(game.registerPlayer("1"));
		Assert.assertFalse(game.registerPlayer("2"));
		Assert.assertFalse(game.registerPlayer("3"));
		Assert.assertFalse(game.registerPlayer("4"));
		Assert.assertFalse(game.registerPlayer("5"));
		Assert.assertFalse(game.registerPlayer("6"));
		Assert.assertTrue(game.registerPlayer("7"));
		//game started
		TestHelper helper = new TestHelper(game.alivePlayer());
		//start primary election
		game.startPrimaryElection();

		Assert.assertNull(game.vote(new Vote("1", Arrays.asList("2", "3"))));
		Assert.assertNull(game.vote(new Vote("2", Collections.singletonList("2"))));
		Assert.assertNull(game.vote(new Vote("3", Collections.singletonList(Player.NOBODY.getUserId()))));
		Assert.assertNull(game.vote(new Vote("4", Collections.singletonList("2"))));
		Assert.assertNull(game.vote(new Vote("5", Collections.singletonList("4"))));
		Assert.assertNull(game.vote(new Vote("6", Collections.singletonList("4"))));
		ElectionResult electionResult = game.vote(new Vote("7", Collections.singletonList("4")));
		Assert.assertFalse(electionResult.isSingleResult());
		System.out.println("primary election result:");
		System.out.println(electionResult.getResult());
		Assert.assertTrue(electionResult.getElects().contains("2"));
		Assert.assertTrue(electionResult.getElects().contains("4"));
		//equal votes

		//start final election
		game.startFinalElection();
		Assert.assertNull(game.vote(new Vote("1", Collections.singletonList(helper.mafia(0)))));
		Assert.assertNull(game.vote(new Vote("2", Collections.singletonList(helper.citizen(0)))));
		Assert.assertNull(game.vote(new Vote("3", Collections.singletonList(Player.NOBODY.getUserId()))));
		Assert.assertNull(game.vote(new Vote("4", Collections.singletonList(helper.citizen(0)))));
		Assert.assertNull(game.vote(new Vote("5", Collections.singletonList(helper.citizen(1)))));
		Assert.assertNull(game.vote(new Vote("6", Collections.singletonList(helper.citizen(1)))));
		ElectionResult finalElectionResult = game.vote(new Vote("7", Collections.singletonList(helper.citizen(1))));
		Assert.assertTrue(finalElectionResult.isSingleResult());
		System.out.println("final election result:");
		System.out.println(finalElectionResult.getResult());
		Assert.assertEquals(finalElectionResult.getElects().size(), 1);
		Assert.assertEquals(finalElectionResult.getElects().get(0), helper.citizen(1));
		//citizen 1 killed

		Assert.assertNull(game.kill(new Vote(helper.mafia(0), Collections.singleton(helper.citizen(0)))));
		ElectionResult kill1Result = game.kill(new Vote(helper.mafia(1), Collections.singleton(helper.citizen(2))));
		Assert.assertFalse(kill1Result.isSingleResult());

		//second try
		Assert.assertNull(game.kill(new Vote(helper.mafia(0), Collections.singleton(helper.citizen(0)))));
		ElectionResult kill2Result = game.kill(new Vote(helper.mafia(1), Collections.singleton(helper.citizen(0))));
		Assert.assertTrue(kill2Result.isSingleResult());
		Assert.assertEquals(kill2Result.getElects().get(0), helper.citizen(0));

		//detective ask
		Assert.assertFalse(game.ask(helper.detective(), helper.doctor()));

		//doctor heal correct
		game.heal(helper.doctor(), helper.citizen(0));

		//sunrise
		Assert.assertEquals(game.alivePlayer().size(), 6); // nobody killed at night

		game.startFinalElection();
		Assert.assertNull(game.vote(new Vote(helper.citizen(0), Collections.singleton(helper.mafia(0)))));
		Assert.assertNull(game.vote(new Vote(helper.citizen(2), Collections.singleton(helper.mafia(0)))));
		Assert.assertNull(game.vote(new Vote(helper.mafia(0), Collections.singleton(helper.mafia(0)))));
		Assert.assertNull(game.vote(new Vote(helper.mafia(1), Collections.singleton(helper.mafia(0)))));
		Assert.assertNull(game.vote(new Vote(helper.doctor(), Collections.singleton(helper.mafia(0)))));
		ElectionResult finalElection2 = game.vote(new Vote(helper.detective(), Collections.singleton(helper.mafia(0))));
		//mafia 0 killed

		game.kill(new Vote(helper.mafia(1), Collections.singleton(helper.doctor())));
		Assert.assertFalse(game.ask(helper.detective(), helper.citizen(0)));
		game.heal(helper.doctor(), helper.detective());

		//sunrise - doctor killed

		game.startFinalElection();
		Assert.assertNull(game.vote(new Vote(helper.citizen(0), Collections.singleton(helper.citizen(0)))));
		Assert.assertNull(game.vote(new Vote(helper.citizen(2), Collections.singleton(helper.citizen(0)))));
		Assert.assertNull(game.vote(new Vote(helper.mafia(1), Collections.singleton(helper.citizen(0)))));
		ElectionResult finalElection3 = game.vote(new Vote(helper.detective(), Collections.singleton(helper.mafia(1))));
		Assert.assertEquals(finalElection3.getElects().get(0), helper.citizen(0));
		//citizen 0 killed

		game.kill(new Vote(helper.mafia(1), Collections.singleton(helper.detective())));
		Assert.assertFalse(game.ask(helper.detective(), helper.citizen(2)));

		//sunrise - detective killed
		//game finished
		Thread.sleep(5000L);
		Assert.assertEquals(GameResult.MAFIAS_WIN, gameResult[0]);

	}
}
