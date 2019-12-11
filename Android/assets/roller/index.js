window.loadFile = window.__execute || function() {
};
window.loadFile('file:///asset/AppEngine.bundle/three.min.js');
window.loadFile('file:///asset/AppEngine.bundle/promise.js');
window.loadFile('file:///asset/AppEngine.bundle/WebVR.js');
window.loadFile('file:///asset/AppEngine.bundle/VREffect.js');
window.loadFile('file:///asset/AppEngine.bundle/VRControls.js');
window.loadFile('RollerCoaster.js');

if (WEBVR.isLatestAvailable() === false) {
	document.body.appendChild(WEBVR.getMessage());
}

//

var renderer = new THREE.WebGLRenderer({
	antialias : true
});
renderer.setClearColor(0xf0f0ff);
renderer.setPixelRatio(window.devicePixelRatio);
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);

var scene = new THREE.Scene();

var light = new THREE.HemisphereLight(0xfff0f0, 0x606066);
light.position.set(1, 1, 1);
scene.add(light);

var train = new THREE.Object3D();
scene.add(train);

var camera = new THREE.PerspectiveCamera(40, window.innerWidth / window.innerHeight, 1, 5000);
camera.rotation.y = Math.PI;
train.add(camera);

// environment

var geometry = new THREE.PlaneGeometry(5000, 5000, 15, 15);
geometry.applyMatrix(new THREE.Matrix4().makeRotationX(-Math.PI / 2));
var material = new THREE.MeshLambertMaterial({
	color : 0x407000,
	shading : THREE.FlatShading
});

for (var i = 0; i < geometry.vertices.length; i++) {

	var vertex = geometry.vertices[i];

	vertex.x += Math.random() * 100 - 50;
	vertex.z += Math.random() * 100 - 50;

	var distance = (vertex.distanceTo(scene.position) / 5) - 250;

	vertex.y = Math.random() * Math.max(0, distance);

}

geometry.computeFaceNormals();

var mesh = new THREE.Mesh(geometry, material);
scene.add(mesh);

var geometry = new TreesGeometry(mesh);
var material = new THREE.MeshBasicMaterial({
	side : THREE.DoubleSide,
	vertexColors : THREE.VertexColors
});
var mesh = new THREE.Mesh(geometry, material);
scene.add(mesh);

var geometry = new SkyGeometry();
var material = new THREE.MeshBasicMaterial({
	color : 0xffffff
});
var mesh = new THREE.Mesh(geometry, material);
scene.add(mesh);

//

var PI2 = Math.PI * 2;

var curve = (function() {

	var vector = new THREE.Vector3();
	var vector2 = new THREE.Vector3();

	return {

		getPointAt : function(t) {

			t *= Math.PI;

			var x = Math.sin(t * 4) * Math.cos(t * 6) * 1000;
			var y = Math.cos(t * 8) * 80 + Math.cos(t * 20 * Math.sin(t)) * 40 + 200;
			var z = Math.sin(t * 5) * Math.sin(t * 3) * 1000;

			return vector.set(x, y, z);

		},

		getTangentAt : function(t) {

			var delta = 0.0001;
			var t1 = Math.max(0, t - delta);
			var t2 = Math.min(1, t + delta);

			return vector2.copy(this.getPointAt(t2)).sub(this.getPointAt(t1)).normalize();

		}

	};

})();

var geometry = new RollerCoasterGeometry(curve, 1500);
var material = new THREE.MeshStandardMaterial({
	roughness : 0.1,
	metalness : 0,
	vertexColors : THREE.VertexColors
});
var mesh = new THREE.Mesh(geometry, material);
scene.add(mesh);

var geometry = new RollerCoasterLiftersGeometry(curve, 100);
var material = new THREE.MeshStandardMaterial({
	roughness : 0.1,
	metalness : 0
});
var mesh = new THREE.Mesh(geometry, material);
mesh.position.y = 1;
scene.add(mesh);

var geometry = new RollerCoasterShadowGeometry(curve, 500);
var material = new THREE.MeshBasicMaterial({
	color : 0x000000,
	opacity : 0.1,
	depthWrite : false,
	transparent : true
});
var mesh = new THREE.Mesh(geometry, material);
mesh.position.y = 1;
scene.add(mesh);

var funfairs = [];

//

var geometry = new THREE.CylinderGeometry(100, 100, 50, 15);
var material = new THREE.MeshLambertMaterial({
	color : 0xff8080,
	shading : THREE.FlatShading
});
var mesh = new THREE.Mesh(geometry, material);
mesh.position.set(-800, 100, -700);
mesh.rotation.x = Math.PI / 2;
scene.add(mesh);

funfairs.push(mesh);

var geometry = new THREE.CylinderGeometry(50, 60, 40, 10);
var material = new THREE.MeshLambertMaterial({
	color : 0x8080ff,
	shading : THREE.FlatShading
});
var mesh = new THREE.Mesh(geometry, material);
mesh.position.set(500, 20, 300);
scene.add(mesh);

funfairs.push(mesh);

//

var controls = new THREE.VRControls(camera);
var effect = new THREE.VREffect(renderer);

if (WEBVR.isAvailable() === true) {

	document.body.appendChild(WEBVR.getButton(effect));

}

//

window.addEventListener('resize', function() {

	camera.aspect = window.innerWidth / window.innerHeight;
	camera.updateProjectionMatrix();

	renderer.setSize(window.innerWidth, window.innerHeight);

}, false);

//

var position = new THREE.Vector3();
var tangent = new THREE.Vector3();

var lookAt = new THREE.Vector3();

var velocity = 0;
var progress = 0;

var clock = new THREE.Clock();

function animate(time) {

	effect.requestAnimationFrame(animate);

	var delta = clock.getDelta() * 60;

	for (var i = 0; i < funfairs.length; i++) {

		funfairs[i].rotation.y = time * 0.0002;

	}

	//

	progress += velocity * delta;
	progress = progress % 1;

	position.copy(curve.getPointAt(progress));
	position.y += 3;

	train.position.copy(position);

	tangent.copy(curve.getTangentAt(progress));

	velocity -= tangent.y * 0.0000015 * delta;
	velocity = Math.max(velocity, 0.00004);

	train.lookAt(lookAt.copy(position).sub(tangent));

	//

	controls.update();

	effect.render(scene, camera);

};

effect.requestAnimationFrame(animate);
