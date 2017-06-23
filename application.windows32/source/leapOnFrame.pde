
int yBottom = 200;
int yTop = 500;

int xBottom = 150;
int xTop = 650;

boolean indexThere = false;

void leapOnFrame() {
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