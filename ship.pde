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

  void exist() {
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
  void updateCameraPosition() {
    transX = -position.x + width/2;
    transY = -position.y + height/2;
  }
  void calculateMovement() {
    PVector difference = new PVector(cursorPosition.x - position.x, cursorPosition.y - position.y);

    difference.normalize();

    difference.mult(speed);

    position.add(difference);


    position.x = constrain(position.x, 0, mapWidth);
    position.y = constrain(position.y, 0, mapHeight);
  }
  void calculateRotation() {

    //calculate rotation

    PVector difference = new PVector(cursorPosition.x - position.x, position.y - cursorPosition.y);

    PVector zeroVector = new PVector(0, 1);

    rotation = (180/PI)*PVector.angleBetween(zeroVector, difference);

    if (cursorPosition.x < position.x) {
      rotation = 360 - rotation;
    }
  }

  void drawShip() {
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
  void shootBullet() {
    if (!Bill.offScreen) {
      Bill = new bullet(position.x, position.y, rotation);
    }
  }
}