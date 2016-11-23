var framework = new net.devsupport.framework.JsFramework("ws://127.0.0.1:8080");

document.getElementById("leftHand").addEventListener("click", function(){
    var msg = {
		tpe: "net.devsupport.gesturedetection.api.LeftHand"
	};
	framework.publish("Gesture", msg);
});

document.getElementById("rightHand").addEventListener("click", function(){
    var msg = {
		tpe: "net.devsupport.gesturedetection.api.RightHand"
	};
	framework.publish("Gesture", msg);
});

document.getElementById("leftFoot").addEventListener("click", function(){
    var msg = {
		tpe: "net.devsupport.gesturedetection.api.LeftFoot"
	};
	framework.publish("Gesture", msg);
});

document.getElementById("rightFoot").addEventListener("click", function(){
    var msg = {
		tpe: "net.devsupport.gesturedetection.api.RightFoot"
	};
	framework.publish("Gesture", msg);
});

document.getElementById("bothHandsUp").addEventListener("click", function(){
    var msg = {
		tpe: "net.devsupport.gesturedetection.api.BothHandsUp"
	};
	framework.publish("Gesture", msg);
});
