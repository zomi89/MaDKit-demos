/*
 * Copyright 1997-2012 Fabien Michel, Olivier Gutknecht, Jacques Ferber
 * 
 * This file is part of MaDKit_Demos.
 * 
 * MaDKit_Demos is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MaDKit_Demos is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MaDKit_Demos. If not, see <http://www.gnu.org/licenses/>.
 */
package madkit.bees;

import static madkit.bees.BeeLauncher.BEE_ROLE;
import static madkit.bees.BeeLauncher.COMMUNITY;
import static madkit.bees.BeeLauncher.FOLLOWER_ROLE;
import static madkit.bees.BeeLauncher.QUEEN_ROLE;
import static madkit.bees.BeeLauncher.SIMU_GROUP;

import java.awt.Point;

import madkit.kernel.Message;
import madkit.message.ObjectMessage;

/**
 * The leader of a group.
 * 
 * @version 2.0.0.2
 * @author Fabien Michel, Olivier Gutknecht 
*/
public class QueenBee extends AbstractBee
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -179625947655014358L;
	static int border = 20;

	@Override
	protected void buzz()
	{
//		setLogLevel(Level.ALL);
		Message m = nextMessage();
		if(m != null){
			sendReply(m, new ObjectMessage<BeeInformation>(myInformation));
		}

		super.buzz();

		if (beeWorld != null) {
			// check to see if the queen hits the edge
			final Point location = myInformation.getCurrentPosition();
			if (location.x < border || location.x > (beeWorld.getWidth() - border)) {
				dX = -dX;
				location.x += (dX);
			}
			if (location.y < border	|| location.y > (beeWorld.getHeight() - border)) {
				dY = -dY;
				location.y += (dY);
			}
		if(logger != null){
			logger.fine("my env = "+beeWorld);
			logger.finest(myInformation.getPreviousPosition().toString());
			logger.finest(myInformation.getCurrentPosition().toString());
		}
	}
	}

	@Override
	protected void activate()
	{
//		setLogLevel(Level.ALL);
		requestRole(COMMUNITY,SIMU_GROUP,QUEEN_ROLE,null);
		requestRole(COMMUNITY,SIMU_GROUP,BEE_ROLE,null);
		broadcastMessage(COMMUNITY,SIMU_GROUP,FOLLOWER_ROLE, new ObjectMessage<BeeInformation>(myInformation));
		BeeViewer.nbOfBroadcast++;
		if(logger != null)
			logger.info("my initial location"+myInformation.getCurrentPosition());
	}

	@Override
	protected void end() {
		//informing follower that I am leaving
		BeeViewer.nbOfBroadcast++;
		broadcastMessage(COMMUNITY,SIMU_GROUP,FOLLOWER_ROLE, new ObjectMessage<BeeInformation>(myInformation));
	}

	@Override
	protected int getMaxVelocity() {
		if (beeWorld != null) {
			return beeWorld.getQueenVelocity().getValue();
		}
		return 0;
	}
	
	@Override
	protected void computeNewVelocities() {
		if (beeWorld != null) {
			int acc = beeWorld.getQueenAcceleration().getValue();
			dX += randomFromRange(acc);
			dY += randomFromRange(acc);
		}
	}


}