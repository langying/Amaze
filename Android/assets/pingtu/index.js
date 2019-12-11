window.loadFile = window.__execute || function() {
};
window.loadFile('Tween.js');
window.loadFile('file:///asset/AppEngine.bundle/pixi.js');

var w = window.innerWidth;
var h = window.innerHeight;
var mCurIdx = 0;
var mNames = [];
var mImages = [];
var mBounds = [];
var mFrames = [];
var mIndexs = [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ];
for (var j = 0; j < 3; j++) {
	for (var i = 0; i < 3; i++) {
		mNames.push((3 * j + i) + '.png');
		mFrames.push({
			x : (i - 1) * 256,
			y : (j - 1) * 256
		});
		mBounds.push({
			l : (i - 1.5) * 256,
			r : (i - 0.5) * 256,
			t : (j - 1.5) * 256,
			b : (j - 0.5) * 256
		});
	}
}

var mNode = new PIXI.Container();
mNode.x = w / 2;
mNode.y = h / 2;

var app = new PIXI.Application(w, h, {
	backgroundColor : 0xFFFFFF
});
app.stop();
app.stage.interactive = true;
app.stage.addChild(mNode);
app.ticker.add(function() {
	TWEEN.update();
});
document.body.appendChild(app.view);

PIXI.loader.add('spritesheet', 'img/meta.json').load(onAssetsLoaded);
function onAssetsLoaded() {
	for (var i = 0, l = mFrames.length; i < l; i++) {
		var index = mIndexs[i];
		var corrd = mFrames[i];
		var image = PIXI.Sprite.fromFrame(mNames[index]);
		image.i = i;
		image.x = corrd.x;
		image.y = corrd.y;
		image.anchor.set(0.5);
		image.buttonMode = true;
		image.interactive = true;
		image.on('pointerup', onDragEnd);
		image.on('pointermove', onDragMove);
		image.on('pointerdown', onDragStart);
		image.on('pointerupoutside', onDragEnd);

		mImages.push(image);
		mNode.addChild(image);
	}
	app.start();
}

function onDragEnd() {
	if (this.data) {
		var pt = this.data.getLocalPosition(this.parent);
		this.alpha = 1;
		this.data = null;

		var idx1 = mCurIdx;
		var idx2 = indexOfPosition(this.x, this.y);
		console.log('onDragEnd:' + idx1 + ',' + idx2);
		if (idx2 < 0) {
			var b = mBounds[idx1];
			var p1 = {
				x : this.x,
				y : this.y
			};
			var p2 = {
				x : (b.l + b.r) / 2,
				y : (b.t + b.b) / 2
			};
			var tt = this;
			new TWEEN.Tween(p1).to(p2, 500).easing(TWEEN.Easing.Quartic.InOut).onUpdate(function() {
				tt.x = this.x;
				tt.y = this.y
			}).start();
			return;
		}
		else {
			var b = mBounds[idx1];
			var img1 = this;
			var img2 = imageOfIndex(img1, idx2);
			var pt11 = {
				x : img1.x,
				y : img1.y
			};
			var pt12 = {
				x : img2.x,
				y : img2.y
			};
			var pt21 = {
				x : img2.x,
				y : img2.y
			};
			var pt22 = {
				x : (b.l + b.r) / 2,
				y : (b.t + b.b) / 2
			};
			new TWEEN.Tween(pt11).to(pt12, 500).easing(TWEEN.Easing.Quartic.InOut).onUpdate(function() {
				img1.x = this.x;
				img1.y = this.y
			}).start();

			var tt = this;
			new TWEEN.Tween(pt21).to(pt22, 500).easing(TWEEN.Easing.Quartic.InOut).onUpdate(function() {
				img2.x = this.x;
				img2.y = this.y
			}).start();
		}
	}
}
function onDragMove() {
	if (this.data) {
		var pt = this.data.getLocalPosition(this.parent);
		this.x = pt.x;
		this.y = pt.y;
	}
}
function onDragStart(event) {
	this.data = event.data;
	if (this.data) {
		this.alpha = 0.5;
		var pt = this.data.getLocalPosition(this.parent);
		mCurIdx = indexOfPosition(pt.x, pt.y);
		console.log('mCurIdx:' + mCurIdx);
	}
}

function indexOfPosition(x, y) {
	for (var i = 0, l = mBounds.length; i < l; i++) {
		var b = mBounds[i];
		if (b.l <= x && x < b.r && b.t <= y && y < b.b) {
			return i;
		}
	}
	return -1;
}
function imageOfIndex(other, idx) {
	var b = mBounds[idx];
	for (var i = 0, l = mImages.length; i < l; i++) {
		var img = mImages[i];
		var x = img.x;
		var y = img.y;
		if (img != other && b.l <= x && x < b.r && b.t <= y && y < b.b) {
			return img;
		}
	}
	return null;
}
