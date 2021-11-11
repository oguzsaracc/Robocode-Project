package Beast;

import robocode.*;
import robocode.util.*;
import java.awt.Color;
 
public class Beast extends AdvancedRobot{
	
	 //  Made By GJWHF GROUP.   BEAST v4.0
	 //	 1)Simplified distance-control.
	 //  2)Our rotation string fixed(Thanks to WeekendObsession)
	 //  3)Replaced the gun with Username: WeekendObsession's we are not using Username: FunkyChicken's guns anymore. Managed to add distance-control and setAdjustGun.
	 //  4)On that version we turned back to orginal random movement, kept setAdjust but left out the distance-control. We also change to consuming energy managment added.
	 //  5)Stop and Go death limit now is 2, before it was 3. 
	 //  
	 //  v3.5 Updates;
	 //  1)Simplified distance-control.
	 //  2)Our rotation string fixed(Thanks to WeekendObsession)
	 // 
	 //  v3.0 Updates:
	 //  1)Simplified distance-control.
	 //
	 //  v2.0 Updates: 
	 //  1)Added 'final' to our stables. (line 18-21)
	 //  2)Added some fresh colors to our Robot :)
	 //  3)Chaned the (Math.random()*10000-4700) to (Math.random()*10000-5000) So. Robot will be move more carefully when it is stall.
     //  Credits:
     //   RoboWiki.net - Website for RoboCode. Any information can find in this website.
     //   AdvancedRobot Properties sourceforge - Any code properties for 'AdvancedRobot'.
 
    static String logEnemy = "000000000000000000000000000000888888888888888765432100888765432101234567888765432100";//The list where enemy lateral velocities are stored. (Thanks to WeekendObsession)
 
     //Stables for our robot.
    static final int    deathLimit = 1; 				// Do not use the Stop and Go until 2 deaths
 	static final int    wide_Pattern = 30; 				// Pattern length for finding enemy.
    static final double bulletPower = 2.5; 				// Bullet power of our robot.
    static final double veloBullet = 20-3*bulletPower; 	// The fire speed of bullet. bullet fired at BULLETPOWER part.
    static final double amoAction = 36; 				// Pixel of our robot where it will go and stop again.
 
     //Variables.
    static int countDeath;
	static double enegryPrev;  //Storing a energy
    static double direction = amoAction;
 
    public void run(){
        
		setTurnRadarRight(Double.POSITIVE_INFINITY); //Radar-lock infinite.
    
        setColors(Color.RED, Color.WHITE, Color.RED, Color.RED, Color.WHITE); // Adding some good color :) 
	}
 
    public void onScannedRobot(ScannedRobotEvent e){
   	 
	 //Variables for local.
	double bAbsolute; 				//absolute bearing
    int indxRobot; 					//index of the match our gun
    int lenMatch = wide_Pattern;	//The number of data a pattern have it, then in bAbsolute its update.
	int i;
			
	 //Thanks to the WeekendObsession. It is located under the movement now. The robot is save codesize on bAbsolute.
     
	 //How to moves? The robot Stop and Go, if it fails start to simple random movement.
	 
			if(enegryPrev > (enegryPrev = e.getEnergy())){
				if(countDeath > deathLimit){
					direction = (Math.random()*100000-50000);
					//direction = (40000.0*(Math.random() - Math.random())); -> Also you can try that.
				}
				setAhead(direction);
			}
			setTurnRightRadians((Math.cos(bAbsolute = e.getBearingRadians())));
	 
	 //We used the new lateral velocity in here. 
			logEnemy = String.valueOf((char)Math.round(e.getVelocity()*Math.sin(e.getHeadingRadians() - (bAbsolute+=getHeadingRadians())))).concat(logEnemy);
	 
	 
	 //Store the index of the match. Reducing pattern depth.
			while((indxRobot = logEnemy.indexOf(logEnemy.substring(0, lenMatch--), (i = (int)((e.getDistance())/veloBullet)))) < 0);
	
	 //We added angular velocities of the first lateral velocities.		
			do{
				bAbsolute += Math.sin(((byte)logEnemy.charAt(indxRobot--))/e.getDistance());
			}
			while(--i >0);

	 //Turning the robot's gun.
            setTurnGunRightRadians(Utils.normalRelativeAngle(bAbsolute-getGunHeadingRadians()));
 
     //Attacking to the enemy.
            setFire(bulletPower);
			
			setTurnRadarLeft(getRadarTurnRemaining());

    }
    public void onHitWall(HitWallEvent e){
            direction=-direction;
    }
 
    public void onDeath(DeathEvent e){
            countDeath++;
    }
}