import de.voidplus.leapmotion.*;

LeapMotion leap;

PVector cursorPosition = new PVector(0, 0); //position where the cursor is 

int mapWidth = 5000; //width of the map
int mapHeight = 5000; //height of the map

float transX = 50;
float transY = 50;

PFont mainFont;

int gameState = 1;


/*
 0 - game itself
 1 - death screen 
 2 - main menu
 3 - game start screen
 */

int backTint = 255;

ship Mike;
bullet Bill;
asteroid Test;

ArrayList<asteroid> asteroids = new ArrayList<asteroid>();
IntList deleteList = new IntList();

void setup() {

  size(800, 800);

  frameRate(60);

  //load font 

  mainFont = loadFont("VirtualDJ-48.vlw");

  textFont(mainFont);
  //intiate the leap 
  leap = new LeapMotion(this).allowGestures(); 

  //initiation
  Mike = new ship(400, 400);

  Bill = new bullet(-width, -height, 0);

  for (int i = 0; i < waves[curGameCycle]; i++) {
    float radius = 50 + randomGaussian()*10;
    asteroid temp = new asteroid(random(0, mapWidth), random(0, mapHeight), radius);
    asteroids.add(temp);
  }
}
void draw() {

  switch(gameState) {
  case 0:
    drawMap();

    ArrayList<asteroid> keep = new ArrayList<asteroid>();
    for (asteroid curAsteroid : asteroids) {
      curAsteroid.exist();
      if (curAsteroid.deadCycle > 0) {
        keep.add(curAsteroid);
      }
    }
    asteroids = keep;
    if (asteroids.size() == 0) {
      curGameCycle++; 
      for (int i = 0; i < waves[curGameCycle]; i++) {
        float radius = 50 + randomGaussian()*10;
        asteroid temp = new asteroid(random(0, mapWidth), random(0, mapHeight), radius);
        asteroids.add(temp);
      }
    }

    Bill.exist();
    drawMiniMap();
    drawScoring();
    fill(0, 0, 0, backTint);
    backTint -= 10;
    stroke(0);
    rect(-width, -height, mapWidth + width, mapHeight + height);
    Mike.exist();
    break;
  case 1:
    deathScreen();
    break;

  case 2:
    mainMenu();
    break;

  case 3:
    waitMenu();
  }
  fill(255, 255, 255, whiteFlash);
  stroke(255, 255, 255, whiteFlash);
  rect(-width, -height, mapWidth + width, mapHeight + height);
  whiteFlash = constrain(whiteFlash, 0, 255);
  whiteFlash -= 5;
}