window.loadFile = window.__execute || function() {
};
window.loadFile('Tween.js');
window.loadFile('file:///asset/AppEngine.bundle/pixi.js');

var mSelects = [ 15, 10, 5, 10 ];
var mPencils = [ //
0xC80000, 0xFF0000, 0xFF4400, 0xFFCACC, 0xC66212, 0xFF8000, 0xFFC800, 0xFFC800, //
0xFFFF34, 0xA2FF00, 0x00FF00, 0x808000, 0x006400, 0x00FFFF, 0x8CCCAB, 0x007ECC, //
0x0000FF, 0xD3A0FF, 0x8A2AE2, 0x800080, 0xB0841D, 0x6E501E, 0x4E4E4E, 0x000000, //
];

var mSIdx = 0;
var mPIdx = 0;
var mSize = 50;
var mStop = true;
var mSels = [];
var mPens = [];
var mList = [];

var w = window.innerWidth;
var h = window.innerHeight;
var app = new PIXI.Application(w, h, {
	backgroundColor : 0xFFFFFF
});
app.stage.interactive = true;

var texId, image, thing, back, mMenuTop, mMenuBtm;
texId = PIXI.RenderTexture.create(w, h);
image = new PIXI.Sprite(this.texId);
image.interactive = true;
image.on('touchend', onTouchUp);
image.on('touchstart', onTouchDown);
image.on('touchmove', onTouchMove);
image.on('touchcancel', onTouchUp);
app.stage.addChild(image);

thing = new PIXI.Graphics();
thing.interactive = false;
app.stage.addChild(thing);

back = PIXI.Sprite.fromImage('icon/goback.png');
back.x = 20;
back.y = -20;
back.interactive = true;
back.on('pointertap', onBack);
app.stage.addChild(back);

mMenuTop = new PIXI.Container();
mMenuTop.y = -25;
app.stage.addChild(mMenuTop);
for (var i = 0, l = mSelects.length; i < l; i++) {
	var sel = PIXI.Sprite.fromImage('option/' + i + '.png');
	sel.x = w - 99 * (i + 1);
	sel.index = i;
	sel.interactive = true;
	sel.on('pointertap', onChangeSelect);
	mSels.push(sel);
	mMenuTop.addChild(sel);
}

mMenuBtm = new PIXI.Container();
mMenuBtm.y = h;
app.stage.addChild(mMenuBtm);
for (var i = 0, l = mPencils.length; i < l; i++) {
	var pen = PIXI.Sprite.fromImage('pencil/' + mSIdx + '/' + (i + 1) + '.png');
	pen.anchor.set(0.5, 0.5);
	pen.x = w * (i + 0.5) / l;
	pen.index = i;
	pen.interactive = true;
	pen.on('pointertap', onChangePencil);
	mPens.push(pen);
	mMenuBtm.addChild(pen);
}

app.ticker.add(function() {
	TWEEN.update();
	if (mList.length <= 0) {
		return;
	}

	var width = mSelects[mSIdx];
	var color = mSIdx == 3 ? 0xFFFFFF : mPencils[mPIdx];
	thing.clear();
	thing.lineStyle(width, color);

	if (mStop) {
		for (var i = 0, l = mList.length; i < l; i++) {
			var p = mList[i];
			if (i == 0) {
				thing.moveTo(p.x, p.y);
			}
			else {
				thing.lineTo(p.x, p.y);
			}
		}
		app.renderer.render(thing, texId, false);

		thing.clear();
		mList.length = 0;
		return;
	}

	if (mList.length > mSize) {
		var arr = mList.slice(0, mSize);
		for (var i = 0, l = arr.length; i < l; i++) {
			var p = arr[i];
			if (i == 0) {
				thing.moveTo(p.x, p.y);
			}
			else {
				thing.lineTo(p.x, p.y);
			}
		}
		app.renderer.render(thing, texId, false);

		thing.clear();
		thing.lineStyle(width, color);
		mList = mList.slice(mSize - 1);
	}

	for (var i = 0, l = mList.length; i < l; i++) {
		var p = mList[i];
		if (i == 0) {
			thing.moveTo(p.x, p.y);
		}
		else {
			thing.lineTo(p.x, p.y);
		}
	}
});
document.body.appendChild(app.view);
PIXI.settings.SCALE_MODE = PIXI.SCALE_MODES.NEAREST;

function onTouchDown(event) {
	mStop = false;
	this.data = event.data;
	if (this.data) {
		var p = this.data.getLocalPosition(this);
		mList.push({
			x : p.x,
			y : p.y
		});
	}
	new TWEEN.Tween({
		x : -25,
		y : h
	}).to({
		x : -125,
		y : h + 100
	}, 500).easing(TWEEN.Easing.Quartic.InOut).onUpdate(function() {
		mMenuTop.y = this.x;
		mMenuBtm.y = this.y
	}).start();
}
function onTouchMove() {
	if (this.data) {
		var p = this.data.getLocalPosition(this);
		mList.push({
			x : p.x,
			y : p.y
		});
	}
}
function onTouchUp() {
	mStop = true;
	if (this.data) {
		var p = this.data.getLocalPosition(this);
		mList.push({
			x : p.x,
			y : p.y
		});
	}
	this.data = null;

	new TWEEN.Tween({
		x : -125,
		y : h + 100
	}).to({
		x : -25,
		y : h
	}, 500).easing(TWEEN.Easing.Quartic.InOut).onUpdate(function() {
		mMenuTop.y = this.x;
		mMenuBtm.y = this.y;
	}).start();
}

function onBack() {
	console.log('go to back.');
}
function onChangePencil(event) {
	for (var i = 0, l = mPens.length; i < l; i++) {
		mPens[i].y = 0;
	}
	event.target.y = -25;
	mPIdx = event.target.index;
}
function onChangeSelect(event) {
	for (var i = 0, l = mSels.length; i < l; i++) {
		mSels[i].y = 0;
	}
	event.target.y = 25;
	mSIdx = event.target.index;
}
