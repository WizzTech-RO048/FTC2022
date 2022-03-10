import cv2
import numpy as np
import math

def calculate_dis(p1, p2):
    x1, y1 = p1
    x2, y2 = p2
    Distance = math.sqrt((x2-x1)**2 + (y2-y1)**2)
    return round(Distance)

def detect_pos(distance):
    if(distance<=300):
        return "Detected LEFT"
    if(distance<=700 and distance>=300):
        return "Detected MIDDLE"
    if(distance<=1000 and distance>=700):
        return "Detected RIGHT"

img = cv2.imread('right.jpg')
 
# facem resize la imagine pt HD astfel incat mereu sa putem lucra cu aceleasi dimensiuni, !IMPORTANT
img = cv2.resize(img, (1280, 720), interpolation = cv2.INTER_AREA)
center_point = (100,390)

#masti, segmentare, identificare
kernel = np.ones((7,7),np.uint8)
hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
lower_bound = np.array([20, 80, 80])     
upper_bound = np.array([55, 255, 255])
mask = cv2.inRange(hsv, lower_bound, upper_bound)
mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel)
mask = cv2.morphologyEx(mask, cv2.MORPH_OPEN, kernel)
segmented_img = cv2.bitwise_and(img, img, mask=mask)
contours, hierarchy = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

# aceasta functie genereaza chenarul si stabileste pozitia obiectului (x,y)
index=1
for c in contours:
    if cv2.contourArea(c) <= 5000 :
        continue    
    x,y,w,h = cv2.boundingRect(c)
    cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255,0), 2)
    center = (x,y)
    print(detect_pos(calculate_dis(center_point,center)))
    index+=1


output = cv2.drawContours(img, contours, -1, (0, 0, 255), 3)
output = cv2.circle(img, center_point, radius=5, color=(200, 100, 155), thickness=-1)

cv2.imshow("Output", output)

cv2.waitKey(0)
cv2.destroyAllWindows()

