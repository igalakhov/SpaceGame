import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.voidplus.leapmotion.*; 
import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SpaceGame extends PApplet {




LeapMotion leap;

SoundFile shoot, start, explosion;

PVector cursorPosition = new PVector(0, 0); //position where the cursor is 

int mapWidth = 5000; //width of the map
int mapHeight = 5000; //height of the map

float transX = 50;
float transY = 50;

PFont mainFont;

int gameState = 2;

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

ArrayList<PVector> stars = new ArrayList<PVector>();

public void setup() {

  

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
  for(int i = 0; i < 1000; i++){
   stars.add(new PVector(random(0, mapWidth), random(0, mapHeight))); 
  }
  shoot = new SoundFile(this, "laser.mp3");
  start = new SoundFile(this, "gameStart.mp3");
  explosion = new SoundFile(this, "Explosion.mp3");
}
public void draw() {

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
class asteroid {
  PVector position, velocity;
  float radius, rotation;
  FloatList radii;
  boolean dead;
  boolean special;
  int deadCycle;
  asteroid(float x, float y, float r) {
    radii = generateJumps();
    position = new PVector(x, y);
    if (random(0, 1) > 0.5f) {
      velocity = new PVector(random(5, 15), random(5, 15));
    } else {
      velocity = new PVector(random(-5, -15), random(-5, -15));
    }
    radius = r;
    dead = false;
    deadCycle = 255;
    special = false;
  }
  public void exist() {
    if (!dead) {
      updatePosition();
      checkDeath();
    }
    drawShape();
  }
  public void checkDeath() {
    //asteroid death
    if (dist(position.x, position.y, Bill.position.x, Bill.position.y) < radius) {
      explosion.play();
      dead = true; 
      gameScore += floor(radius*10);
      addOn = str(floor(radius*10));
      addOff = 0;
      addFa = 255;
    }
    //player death
    if (dist(Mike.position.x, Mike.position.y, position.x, position.y) < radius + 70) {
      Mike.dead = true;
      backTint = 0;
    }
  }
  public void updatePosition() {
    position.add(velocity);
    if (!special) {
      if (position.y >= mapHeight - radius) {
        velocity.y = random(-5, -15);
      }
      if (position.x >= mapWidth - radius) {
        velocity.x *= -1;
      }
    } else {
           if (position.y >= height - radius) {
        velocity.y = random(-5, -15);
      }
      if (position.x >= width - radius) {
        velocity.x *= -1;
      } 
    }
    if (position.y <= radius) {
      velocity.y *= -1;
    }
    if (position.x <= radius) {
      velocity.x *= -1;
    }

    position.x = constrain(position.x, radius, mapWidth - radius);
    position.y = constrain(position.y, radius, mapHeight - radius);
  }
  public void drawShape() {
    rotation += randomGaussian()*0.1f;
    if (dead) {
      deadCycle -= 5; 
      radius += 5;
    }
    strokeWeight(10);
    stroke(random(deadCycle), random(deadCycle), random(deadCycle), deadCycle);
    fill(0);

    pushMatrix();

    translate(position.x, position.y);
    rotate(rotation);
    for (int i = 0; i < 361; i++) {
      rotate(PI/180);

      if (dead) {
        point(0, -1*(radii.get(i) + radius) + random(-20, 20));
      } else {
        point(0, -1*(radii.get(i) + radius));
      }
    }
    popMatrix();
  }
  public FloatList generateJumps() {
    FloatList returnList = new FloatList();
    for (float i = 0; i < 361; i++) {
      returnList.append(noise(i*0.1f)*random(20, 30));
    }
    return returnList;
  }
}
class bullet {

  PVector position, velocity;

  float rotation, speed;

  boolean offScreen = false;

  bullet(float x, float y, float angle) {
    speed = 100;
    position = new PVector(x, y);
    velocity = new PVector(0, 1);
    velocity.rotate(angle*(PI/180));
    velocity.mult(-speed);
  }

  public void exist() {
    drawBullet();   
    position.x += velocity.x;
    position.y += velocity.y;

    if (checkBox(position.x, position.y, -transX, -transY, width, height)) {
      offScreen = true;
    } else {
      offScreen = false;
    }
  }  
  public void drawBullet() {
    stroke(255, 0, 0);


    strokeWeight(10);

    line(position.x, position.y, position.x + velocity.x, position.y + velocity.y);
  }
}
public boolean checkBox(float x1, float y1, float x, float y, float w, float h) {
  if (x1 > x && y1 > y && x1 < x + w && y1 < y + h) {
    return true;
  } else {
    return false;
  }
}
int hueThing = 0;

boolean canClick = true;

public void deathScreen() {
  background(0);

  textAlign(CENTER, CENTER);

  textSize(110);

  fill(200);

  text("YOU DIED", width/2, 200);

  textSize(50);

  text("Your score is", width/2, 300);

  textSize(75);

  colorMode(HSB);

  fill(hueThing, 255, 255);

  hueThing++;

  if (hueThing > 255) {
    hueThing = 0;
  }

  text(gameScore, width/2, 400);

  colorMode(RGB);

  textSize(50);

  stroke(255);

  strokeWeight(5);

  if (checkBox(mouseX, mouseY, 200, 450, 400, 100) && mousePressed && canClick) {
    indexThere = false;
    hueChanger = 0;
    streak = 255;
    whiteFlash = 0;
    gameState = 3;
  } else if (checkBox(mouseX, mouseY, 200, 450, 400, 100)) {
    fill(255, 255, 255, 100); 
    rect(200, 450, 400, 100);
  } else {
    fill(255, 255, 255, 200);
    rect(200, 450, 400, 100);
  }

  if (checkBox(mouseX, mouseY, 200, 600, 400, 100) && mousePressed && canClick) {
    gameState = 2;
    canClick = false;
  } else if (checkBox(mouseX, mouseY, 200, 600, 400, 100)) {
    fill(255, 255, 255, 100); 
    rect(200, 600, 400, 100);
  } else {
    fill(255, 255, 255, 200);
    rect(200, 600, 400, 100);
  }


  fill(255);

  text("REPLAY", width/2, 500);

  text("MAIN MENU", width/2, 650);
}
public void mouseReleased(){
 canClick = true;   
}
public void drawCursor() {

  pushMatrix();

  translate(cursorPosition.x, cursorPosition.y);

  rotate(cursorRotation);

  scale(sin(cursorRotation*2)/5 + 1.5f);

  strokeWeight(2);

  line(0, -20, 0, -5);
  line(0, 20, 0, 5);
  line(-20, 0, -5, 0);
  line(20, 0, 5, 0);

  cursorRotation += cursorSpeed;

  popMatrix();

  strokeWeight(1);
}
float cursorSpeed = 0.1f;
float cursorRotation = 0;
int gridSize = 100;
public void drawMap() {
  //translate!!!

  translate(transX, transY);

  //background is black
  background(0);

  //map border

  strokeWeight(10);

  stroke(255);

  noFill();

  rect(0, 0, mapWidth, mapHeight, 50);

  float deltaX = cursorPosition.x + transX;
  float deltaY = cursorPosition.y + transY;

  cursorPosition.x = -transX + deltaX;
  cursorPosition.y = -transY + deltaY;
  drawCursor(); //draw the cursor

  fill(255);

  strokeWeight(1);

  stroke(255, 255, 255, 50);

  for (int i = 0; i < mapWidth; i += gridSize) {
    line(i, 0, i, mapHeight);
  }
  for (int i = 0; i < mapHeight; i += gridSize) {
    line(0, i, mapWidth, i);
  }
  
  stroke(255);
  
  for(PVector star : stars){
   point(star.x, star.y); 
  }
}
public void drawMiniMap() {
  stroke(255);
  strokeWeight(5);

  rect(-transX + 0.75f*width, -transY + 0.75f*height, 0.2f*width, 0.2f*height);

  float playerX = map(Mike.position.x, 0, mapWidth, 0, 0.2f*width);
  float playerY = map(Mike.position.y, 0, mapHeight, 0, 0.2f*height);

  point(-transX + 0.75f*width + playerX, -transY + 0.75f*height + playerY);

  for (asteroid curAsteroid : asteroids) {
    float xPos = map(curAsteroid.position.x, 0, mapWidth, 0, 0.2f*width);
    float yPos = map(curAsteroid.position.y, 0, mapHeight, 0, 0.2f*height);

    stroke(random(255), random(255), random(255));
    point(-transX + 0.75f*width + xPos, -transY + 0.75f*height + yPos);
  }
}           
String addOn = "+100";
int addOff = 0;
int addFa = 0;
public void drawScoring() {
  textAlign(CENTER, CENTER);

  fill(200);

  textSize(90);

  text(gameScore, width/2 - transX, 50 - transY);

  fill(random(255), random(255), random(255), addFa); 

  text(addOn, width/2 - transX, 50 - transY + addOff);

  addOff += 5;
  addFa -= 10;
}
int curGameCycle = 0;

int[] waves = {10, 5, 6, 7, 10, 12, 15, 20, 21, 22};

int gameScore = 0;
public void leapOnKeyTapGesture(KeyTapGesture g) {
  Mike.shootBullet();
}

int yBottom = 200;
int yTop = 500;

int xBottom = 150;
int xTop = 650;

boolean indexThere = false;

public void leapOnFrame() {
  //find the right hand
  Hand rightHand = null;

  for (Hand curHand : leap.getHands()) {
    if (curHand.isRight()) {
      rightHand = curHand;
      break;
    }
  }

  //check if we found the right hand, if not, break out 
  if (rightHand == null) {
    indexThere = false;
    return;
  }

  //find the index finger on the right hand

  Finger indexFinger = null;

  for (Finger curFinger : rightHand.getFingers()) {
    if (curFinger != null && curFinger.getType() == 1) {
      indexFinger = curFinger;
    }
  }

  //check if we have index finger, if not, break out
  if (indexFinger == null) {
    indexThere = false;
    return;
  }

  PVector position = indexFinger.getStabilizedPosition();

  float newX = map(position.x, xBottom, xTop, 0, width);

  float newY = map(position.y, yBottom, yTop, 0, height);

  PVector newPosition = new PVector(newX - transX, newY - transY);

  cursorPosition = newPosition;
  
  indexThere = true;
}
asteroid testAsteroid = new asteroid(random(200, 600), random(200, 600), 50);

public void mainMenu() {

  background(0);
  
  testAsteroid.exist();
  testAsteroid.special = true;
 
  textSize(100);
  stroke(255);
  strokeWeight(5);
  fill(255, 255, 255, 200);

  text("Asteroids", width/2, 200);

  textSize(50);
  textAlign(CENTER, CENTER);
  
  text("(remastered)", width/2, 300);

  if (checkBox(mouseX, mouseY, 200, 450, 400, 150) && mousePressed && canClick) {
    indexThere = false;
    hueChanger = 0;
    streak = 255;
    whiteFlash = 0;
    gameState = 3;
  } else if (checkBox(mouseX, mouseY, 200, 450, 400, 150)) {
    fill(255, 255, 255, 100); 
    rect(200, 450, 400, 150);
  } else {
    fill(255, 255, 255, 200);
    rect(200, 450, 400, 150);
  }
  
  fill(255, 255, 255, 100);
  
  text("PLAY", width/2, 525);
}
public void resetGame() {
  gameScore = 0;
  
  Mike = new ship(400, 400);

  Bill = new bullet(-width, -height, 0);
  
  asteroids = new ArrayList();
  
  curGameCycle = 0;
 
  for (int i = 0; i < waves[curGameCycle]; i++) {
    float radius = 50 + randomGaussian()*10;
    asteroid temp = new asteroid(random(0, mapWidth), random(0, mapHeight), radius);
    asteroids.add(temp);
  }
  start.play();
}
class ship {

  PVector position;

  float rotation, speed;

  int shipW, shipH;

  boolean dead;


  ship(float x, float y) {

    position = new PVector(x, y);

    rotation = 0;

    shipW = 72;
    shipH = 96;

    speed = 10;

    dead = false;
  }

  public void exist() {
    if (!dead) {
      calculateRotation();
      calculateMovement();
      updateCameraPosition();
      drawShip();
    } else {
     tint(backTint);
     drawShip();
     backTint += 20;
     position.x += random(0, 50);
     position.y += random(0, 50);
     if(backTint > 255){
      gameState = 1; 
     }
    }
  }
  public void updateCameraPosition() {
    transX = -position.x + width/2;
    transY = -position.y + height/2;
  }
  public void calculateMovement() {
    PVector difference = new PVector(cursorPosition.x - position.x, cursorPosition.y - position.y);

    difference.normalize();

    difference.mult(speed);

    position.add(difference);


    position.x = constrain(position.x, 0, mapWidth);
    position.y = constrain(position.y, 0, mapHeight);
  }
  public void calculateRotation() {

    //calculate rotation

    PVector difference = new PVector(cursorPosition.x - position.x, position.y - cursorPosition.y);

    PVector zeroVector = new PVector(0, 1);

    rotation = (180/PI)*PVector.angleBetween(zeroVector, difference);

    if (cursorPosition.x < position.x) {
      rotation = 360 - rotation;
    }
  }

  public void drawShip() {
    pushMatrix();

    translate(position.x, position.y); 

    rotate(rotation*(PI/180));

    fill(0);

    strokeWeight(5);

    stroke(255);

    beginShape();

    vertex(0, -48);
    vertex(-36, 48);
    vertex(0, 36);
    vertex(36, 48);
    vertex(0, -48);


    endShape();



    popMatrix();
  }
  public void shootBullet() {
    if (!Bill.offScreen && gameState == 0) {
      shoot.play();
      Bill = new bullet(position.x, position.y, rotation);
    }
  }
}
float hueChanger = 0;
float streak = 255;

float whiteFlash = 0;

public void waitMenu(){
 colorMode(HSB);
 
 background(hueChanger, 255, streak);
 hueChanger += 0.1f;
 
 if(indexThere){
  streak--;
 } else {
  streak += 5;
 }
 streak = constrain(streak, -10, 255);
  
 colorMode(RGB);
 
 fill(255);
 
 textAlign(CENTER, CENTER);
 
 text("You are almost ready, hold your hand over the leap motion controller to begin.", 0, 0, 800, 800);
 
 if(streak == -10){
  whiteFlash = 255;
  resetGame();
  gameState = 0;  
 }   
}
  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "SpaceGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
