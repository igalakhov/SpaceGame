boolean checkBox(float x1, float y1, float x, float y, float w, float h) {
  if (x1 > x && y1 > y && x1 < x + w && y1 < y + h) {
    return true;
  } else {
    return false;
  }
}