var rightHandBlue = new Image();
var leftHandBlue = new Image();
var rightFootBlue = new Image();
var leftFootBlue = new Image();
var rightHandGreen = new Image();
var leftHandGreen = new Image();
var rightFootGreen = new Image();
var leftFootGreen = new Image();
var rightHandRed = new Image();
var leftHandRed = new Image();
var rightFootRed = new Image();
var leftFootRed = new Image();
var qrCode = new Image();
var wrongGesture = 0;
var c = document.getElementById('myCanvas');
var cTop = document.getElementById('myCanvasTop');
var x = c.width;
var y = c.height;
var scaleX = 70;
var scaleY = 70;
var ctx = c.getContext('2d');
var backGroundColor = '#333';
var textColor = '#FFF';
var m_canvas = document.createElement('canvas');
var m_context = m_canvas.getContext('2d');
var framework = new net.devsupport.framework.JsFramework('ws://127.0.0.1:8080');
rightHandBlue.src = './pictures/rightHandBlue.png';
leftHandBlue.src = './pictures/leftHandBlue.png';
rightFootBlue.src = './pictures/rightFootBlue.png';
leftFootBlue.src = './pictures/leftFootBlue.png';
rightHandGreen.src = './pictures/rightHandGreen.png';
leftHandGreen.src = './pictures/leftHandGreen.png';
rightFootGreen.src = './pictures/rightFootGreen.png';
leftFootGreen.src = './pictures/leftFootGreen.png';
rightHandRed.src = './pictures/rightHandRed.png';
leftHandRed.src = './pictures/leftHandRed.png';
rightFootRed.src = './pictures/rightFootRed.png';
leftFootRed.src = './pictures/leftFootRed.png';
qrCode.src = './pictures/medienfassade.informatik.haw-hamburg.de.png';
m_canvas.width = x;
m_canvas.height = y;
m_context.textAlign = 'center';
m_context.fillStyle = backGroundColor;
m_context.fillRect(0, 0, m_canvas.width, m_canvas.height);
drawWelcomeScreen();
ctx.drawImage(m_canvas, 0, 0);
framework.subscribe('Gamestate', function (obj) {
  drawGameState(obj);
});
function drawGameState(gameState) {
  drawBackGround();
  if (gameState.lifepoints > 0) {
    var gestureBlockArray = gameState.gestureBlockArray;
    //draw all gestureBlocks in gestureBlockArray
    for (var i = 0; i < gestureBlockArray.length; i++) {
      if (i > 0) {
        drawGestureBlock(gestureBlockArray[i].gestureList, gestureBlockArray[i].x, gestureBlockArray[i].y, 0);
      } else {
        drawGestureBlock(gestureBlockArray[i].gestureList, gestureBlockArray[i].x, gestureBlockArray[i].y, 1);
      }
    }
    drawTime(gameState.timer, textColor, 10, 20);
    drawLifePoints(gameState.lifepoints, textColor, 10, 40);
    drawPoints(gameState.points, textColor, 10, 60);
  } else {
    //message displayed if lifepoits are below 0
    drawGameLost(gameState.points);
    setTimeout(function () {
        drawBackGround();
        drawWelcomeScreen();
        ctx.drawImage(m_canvas, 0, 0);
      }, 5000);
  }
  //copy from bufferCanvas to canvas

  interpreteState(gameState.state);
  ctx.drawImage(m_canvas, 0, 0);
}
function drawBackGround() {
  m_context.fillStyle = backGroundColor;
  m_context.fillRect(0, 0, x, y);
}
function interpreteState(state) {
  switch (state) {
      //right gesture
    case 1:
      new Audio('./sounds/audio1.wav').play();
      break;
      //wrong gesture
    case 2:
      new Audio('./sounds/audio2.wav').play();
      wrongGesture = 1;
      setTimeout(function () {
        wrongGesture = 0;
      }, 300);
      break;
      //game lost
    case 3:
      new Audio('./sounds/audio3.wav').play();
      break;
      //game reset
    case 4:
      new Audio('./sounds/audio2.wav').play();
      drawWelcomeScreen();
      break;
    default:
      console.log('State 0');
  }
}
//Draws a gestureBlock at Position X,Y on myCanvas

function drawGestureBlock(gestureBlock, x, y, typ) {
  var position = 0;
  for (var i = 0; i < gestureBlock.length; i++) {
    if (typ == 1 && i == 0) {
      //Blue
      drawGesture(gestureBlock[i], parseInt(x) + position, parseInt(y), 1);
    } else {
      //Green
      drawGesture(gestureBlock[i], parseInt(x) + position, parseInt(y), 0);
    }
    position += scaleX;
  }
}
//Draws the Pictures assigned to a gesture Type at position X,Y

function drawGesture(gestureTyp, x, y, typ) {
  var redScale = 1.1;
  switch (gestureTyp.tpe) {
    case 'net.devsupport.gesturedetection.api.RightHand':
      if (typ == 0) {
        m_context.drawImage(rightHandBlue, x, parseInt(y) - scaleY, scaleX, scaleY);
      } else {
        if (wrongGesture == 0) {
          m_context.drawImage(rightHandGreen, x, parseInt(y) - scaleY, scaleX, scaleY);
        } else {
          m_context.drawImage(rightHandRed, x, parseInt(y) - scaleY*redScale, scaleX*redScale, scaleY*redScale);
        }
      }
      break;
    case 'net.devsupport.gesturedetection.api.LeftHand':
      if (typ == 0) {
        m_context.drawImage(leftHandBlue, x, parseInt(y) - scaleY, scaleX, scaleY);
      } else {
        if (wrongGesture == 0) {
          m_context.drawImage(leftHandGreen, x, parseInt(y) - scaleY, scaleX, scaleY);
        } else {
          m_context.drawImage(leftHandRed, x, parseInt(y) - scaleY*redScale, scaleX*redScale, scaleY*redScale);
        }
      }
      break;
    case 'net.devsupport.gesturedetection.api.RightFoot':
      if (typ == 0) {
        m_context.drawImage(rightFootBlue, x, parseInt(y) - scaleY, scaleX, scaleY);
      } else {
        if (wrongGesture == 0) {
          m_context.drawImage(rightFootGreen, x, parseInt(y) - scaleY, scaleX, scaleY);
        } else {
          m_context.drawImage(rightFootRed, x, parseInt(y) - scaleY*redScale, scaleX*redScale, scaleY*redScale);
        }
      }
      break;
    case 'net.devsupport.gesturedetection.api.LeftFoot':
      if (typ == 0) {
        m_context.drawImage(leftFootBlue, x, parseInt(y) - scaleY, scaleX, scaleY);
      } else {
        if (wrongGesture == 0) {
          m_context.drawImage(leftFootGreen, x, parseInt(y) - scaleY, scaleX, scaleY);
        } else {
          m_context.drawImage(leftFootRed, x, parseInt(y) - scaleY*redScale, scaleX*redScale, scaleY*redScale);
        }
      }
      break;
    default:
      console.log('no Gesture match');
  }
}
//draw time on bufferCanvas

function drawTime(timer, color, x, y) {
  m_context.textAlign = 'start';
  m_context.font = '20px ARIAL';
  m_context.fillStyle = color;
  m_context.fillText('Time: ' + timer, x, y);
}
//draw lifePoints on bufferCanvas

function drawLifePoints(lifepoints, color, x, y) {
  m_context.textAlign = 'start';
  m_context.font = '20px ARIAL';
  m_context.fillStyle = color;
  m_context.fillText('Lifepoints: ' + lifepoints, x, y);
}
function drawPoints(points, color, x, y) {
  m_context.textAlign = 'start';
  m_context.font = '20px ARIAL';
  m_context.fillStyle = color;
  m_context.fillText('Points: ' + points, x, y);
}
function drawGameLost(points) {
  drawBackGround();
  m_context.textAlign = 'center';
  m_context.fillStyle = textColor;
  m_context.font = '60px ARIAL';
  m_context.fillText('POINTS: ' + points, x / 2, 160);
  m_context.font = '25px ARIAL';
  m_context.fillText('by Krystian Graczyk, Nick Diedrich', x / 2, 320);
  m_context.font = '20px ARIAL';
  m_context.fillText('supervised by Sascha Kluth, Tobias Eichler', x / 2, 350);
  m_context.fillText('medienfassade.informatik.haw-hamburg.de', x / 2, 370);
}
function drawWelcomeScreen() {
  drawBackGround();
  m_context.textAlign = 'center';
  m_context.fillStyle = textColor;
  m_context.font = '40px ARIAL';
  m_context.fillText('RAISE BOTH HANDS', x / 2, 110);
  m_context.fillText('TO PLAY', x / 2, 160);
  m_context.font = '20px ARIAL';
 m_context.drawImage(qrCode, x/2-65, 210, 130, 130);
  m_context.fillText('medienfassade.informatik.haw-hamburg.de', x / 2, 370);
}
function fullscreen() {
  if (c.webkitRequestFullScreen) {
    c.webkitRequestFullScreen();
  } else {
    c.mozRequestFullScreen();
  }
}
